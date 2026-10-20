package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.entity.dto.*;
import com.example.project.exception.BusinessException;
import com.example.project.exception.ErrorCode;
import com.example.project.exception.ForbiddenException;
import com.example.project.exception.NotFoundException;
import com.example.project.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class RouteService {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final PlaceRepository placeRepository;
    private final RouteItemRepository routeItemRepository;
    private final PlaceItemRepository placeItemRepository;
    private final MemoRepository memoRepository;
    private final MemoItemRepository memoItemRepository;
    private final InviteRepository inviteRepository;
    private final DistributedLockService distributedLockService;
    private final PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @PostConstruct
    private void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public Long addRoute(String username, String title) {
        log.info("루트 생성 요청: 제목='{}', 사용자={}", title, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Route route = new Route();
        route.setTitle(title);
        route.setOwner(user);
        Route saved = routeRepository.save(route);
        routeRepository.addRouteUser(saved.getId(), user.getId());
        log.info("루트 생성 완료: ID={}, 제목='{}', 사용자={}", saved.getId(), title, username);
        return saved.getId();
    }

    public void assertMember(Long routeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        List<User> members = routeRepository.findUsersByRouteId(routeId);
        if (members.stream().noneMatch(u -> u.getId().equals(user.getId()))) {
            throw new ForbiddenException(ErrorCode.NOT_ROUTE_MEMBER);
        }
    }

    // findMaxOrderByRouteIdAndDay()+1 의 race condition 방지:
    // 락 없이 동시 요청이 들어오면 같은 maxOrder를 읽어 동일 order 값으로 중복 저장.
    // Redis 분산 락 + 비관적 DB 락으로 order 계산과 저장을 원자적으로 처리.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addPin(AddPlaceToRouteRequest request, String username) {
        assertMember(request.getRouteId(), username);
        NaverPlaceDto place = request.getPlace();
        log.info("장소 추가 요청: 장소='{}', 루트ID={}", place.getTitle(), request.getRouteId());

        String lockToken = distributedLockService.acquire(request.getRouteId());
        try {
            transactionTemplate.executeWithoutResult(status -> {
                Route route = routeRepository.findByIdWithLock(request.getRouteId())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));

                Place newPlace = new Place();
                newPlace.setTitle(place.getTitle());
                newPlace.setAddress(place.getAddress());
                newPlace.setRoadAddress(place.getRoadAddress());
                newPlace.setLink(place.getLink());
                newPlace.setCategory(place.getCategory());
                newPlace.setMapx(place.getMapx());
                newPlace.setMapy(place.getMapy());
                newPlace.setRoute(route);
                placeRepository.save(newPlace);

                int maxOrder = routeItemRepository.findMaxOrderByRouteIdAndDay(request.getRouteId(), request.getDay());
                PlaceItem placeItem = new PlaceItem();
                placeItem.setPlace(newPlace);
                placeItem.setRoute(route);
                placeItem.setDay(request.getDay());
                placeItem.setOrder(maxOrder + 1);
                routeItemRepository.save(placeItem);
                log.info("장소 추가 완료: 장소ID={}, 장소='{}', 루트ID={}", newPlace.getId(), place.getTitle(), request.getRouteId());
            });
        } finally {
            distributedLockService.release(request.getRouteId(), lockToken);
        }
    }

    // 클라이언트 제공 order를 신뢰하지 않고 서버에서 계산.
    // addPin/addMemo와 동일한 분산 락 + 비관적 락으로 order 중복을 방지.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addPlaceByName(Long routeId, String name, String hours, String tip, int day) {
        log.info("장소 추가 요청(이름만): 루트ID={}, 장소='{}', day={}", routeId, name, day);
        String lockToken = distributedLockService.acquire(routeId);
        try {
            transactionTemplate.executeWithoutResult(status -> {
                Route route = routeRepository.findByIdWithLock(routeId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
                Place place = new Place();
                place.setTitle(name);
                if (hours != null && !hours.isBlank()) place.setAddress(hours);
                if (tip != null && !tip.isBlank()) place.setRoadAddress(tip);
                place.setRoute(route);
                placeRepository.save(place);

                int maxOrder = routeItemRepository.findMaxOrderByRouteIdAndDay(routeId, day);
                PlaceItem placeItem = new PlaceItem();
                placeItem.setPlace(place);
                placeItem.setRoute(route);
                placeItem.setDay(day);
                placeItem.setOrder(maxOrder + 1);
                routeItemRepository.save(placeItem);
                log.debug("장소 추가 완료(이름만): 장소='{}', 루트ID={}", name, routeId);
            });
        } finally {
            distributedLockService.release(routeId, lockToken);
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void addMemo(MemoRequest memoRequest) {
        log.info("메모 추가 요청: 루트ID={}, day={}", memoRequest.getRouteId(), memoRequest.getDay());
        Long routeId = memoRequest.getRouteId();

        String lockToken = distributedLockService.acquire(routeId);
        try {
            transactionTemplate.executeWithoutResult(status -> {
                Route route = routeRepository.findByIdWithLock(routeId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
                Memo memo = new Memo();
                memo.setContent(memoRequest.getContent());
                memo.setRoute(route);
                memoRepository.save(memo);

                int maxOrder = routeItemRepository.findMaxOrderByRouteIdAndDay(routeId, memoRequest.getDay());
                MemoItem memoItem = new MemoItem();
                memoItem.setMemo(memo);
                memoItem.setRoute(route);
                memoItem.setDay(memoRequest.getDay());
                memoItem.setOrder(maxOrder + 1);
                routeItemRepository.save(memoItem);
                log.debug("메모 추가 완료: 메모ID={}", memo.getId());
            });
        } finally {
            distributedLockService.release(routeId, lockToken);
        }
    }

    public void editMemo(Long id, String content, String username) {
        log.info("메모 수정 요청: ID={}", id);
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMO_NOT_FOUND));
        assertMember(memo.getRoute().getId(), username);
        memo.setContent(content);
        memoRepository.save(memo);
        log.debug("메모 수정 완료: ID={}", id);
    }

    public String deleteMemo(Long memoId, String username) {
        log.info("메모 삭제 요청: ID={}", memoId);
        Memo memo = memoRepository.findById(memoId).orElse(null);
        MemoItem memoItem = memoItemRepository.findByMemoId(memoId).orElse(null);
        if (memo == null || memoItem == null) {
            log.warn("메모 삭제 실패 - 존재하지 않음: ID={}", memoId);
            throw new NotFoundException(ErrorCode.MEMO_NOT_FOUND);
        }
        assertMember(memoItem.getRoute().getId(), username);
        doDeleteMemo(memoId, memoItem);
        return "삭제되었습니다";
    }

    private void doDeleteMemo(Long memoId, MemoItem memoItem) {
        routeItemRepository.deleteById(memoItem.getId());
        memoRepository.deleteById(memoId);
        log.info("메모 삭제 완료: ID={}", memoId);
    }

    public String deletePlace(Long placeId, String username) {
        log.info("장소 삭제 요청: ID={}", placeId);
        Place place = placeRepository.findById(placeId).orElse(null);
        PlaceItem placeItem = placeItemRepository.findByPlaceId(placeId).orElse(null);
        if (placeItem == null || place == null) {
            log.warn("장소 삭제 실패 - 존재하지 않음: ID={}", placeId);
            throw new NotFoundException(ErrorCode.PLACE_NOT_FOUND);
        }
        assertMember(placeItem.getRoute().getId(), username);
        doDeletePlace(placeId, placeItem);
        return "삭제되었습니다";
    }

    private void doDeletePlace(Long placeId, PlaceItem placeItem) {
        routeItemRepository.deleteById(placeItem.getId());
        placeRepository.deleteById(placeId);
        log.info("장소 삭제 완료: ID={}", placeId);
    }

    @Transactional
    public void deleteRoute(Long routeId, String username) {
        log.info("루트 삭제 요청: ID={}, 요청자={}", routeId, username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        Long ownerId = routeRepository.findOwnerIdById(routeId);
        if (!ownerId.equals(user.getId())) {
            log.warn("루트 삭제 권한 없음: ID={}, 요청자={}", routeId, username);
            throw new ForbiddenException(ErrorCode.NOT_ROUTE_OWNER);
        }
        routeRepository.deleteAllRouteUsers(routeId);
        placeRepository.findIdsByRouteId(routeId).forEach(placeId -> {
            PlaceItem placeItem = placeItemRepository.findByPlaceId(placeId).orElse(null);
            if (placeItem != null) doDeletePlace(placeId, placeItem);
        });
        memoRepository.findIdsByRouteId(routeId).forEach(memoId -> {
            MemoItem memoItem = memoItemRepository.findByMemoId(memoId).orElse(null);
            if (memoItem != null) doDeleteMemo(memoId, memoItem);
        });
        routeRepository.delete(route);
        log.info("루트 삭제 완료: ID={}", routeId);
    }

    // 멤버 수 체크 → 추가 사이의 race condition 방지:
    // Redis 분산 락(routeId)으로 동시 참가 요청을 직렬화,
    // 트랜잭션 내부에서 Route 행에 비관적 락을 추가로 걸어 DB 레벨 이중 보호.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void joinRoute(String token, String username) {
        log.info("루트 참가 요청: 사용자={}, 토큰={}", username, token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_INVITE_TOKEN));
        Long routeId = invite.getRouteId();

        String lockToken = distributedLockService.acquire(routeId);
        try {
            transactionTemplate.executeWithoutResult(status -> {
                routeRepository.findByIdWithLock(routeId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
                List<User> users = routeRepository.findUsersByRouteId(routeId);
                if (users.stream().anyMatch(u -> u.getId().equals(user.getId()))) {
                    log.info("루트 참가 생략 - 이미 멤버: 사용자={}, 루트ID={}", username, routeId);
                    return;
                }
                if (users.size() >= 30) {
                    log.warn("루트 참가 거부 - 멤버 상한 초과: 루트ID={}, 현재인원={}", routeId, users.size());
                    throw new BusinessException(ErrorCode.ROUTE_MEMBER_LIMIT_EXCEEDED);
                }
                routeRepository.addRouteUser(routeId, user.getId());
                log.info("루트 참가 완료: 사용자={}, 루트ID={}", username, routeId);
            });
        } finally {
            distributedLockService.release(routeId, lockToken);
        }
    }

    // Route 행에 대한 비관적 락으로 db 레벨 이중 보호.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Collection<BlockDto> moveBlock(Long routeId, Long blockId, Long targetBlockId, boolean insertAfter, String username) {
        assertMember(routeId, username);
        log.debug("블록 이동 요청: 블록ID={}, 루트ID={}, targetBlockId={}, insertAfter={}", blockId, routeId, targetBlockId, insertAfter);

        String lockToken = distributedLockService.acquire(routeId);
        try {
            transactionTemplate.executeWithoutResult(status -> {
                routeRepository.findByIdWithLock(routeId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
                executeReorder(routeId, blockId, targetBlockId, insertAfter);
            });

            Collection<BlockDto> result = transactionTemplate.execute(status -> {
                List<RouteItem> items = routeItemRepository.findByRouteId(routeId);
                List<BlockDto> list = items.stream().map(BlockDto::new).collect(Collectors.toList());
                list.sort(Comparator.comparingInt(BlockDto::getDay).thenComparingInt(BlockDto::getOrder));
                return list;
            });

            log.debug("블록 이동 완료: 블록ID={}, 루트ID={}", blockId, routeId);
            return result;
        } finally {
            distributedLockService.release(routeId, lockToken);
        }
    }

    private void executeReorder(Long routeId, Long blockId, Long targetBlockId, boolean insertAfter) {
        RouteItem dragged = routeItemRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BLOCK_NOT_FOUND));
        RouteItem target = routeItemRepository.findById(targetBlockId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BLOCK_NOT_FOUND));

        int oldDay = dragged.getDay();
        int oldOrder = dragged.getOrder();
        int newDay = target.getDay();
        int targetOrder = target.getOrder();

        // 이동 방향에 따라 최종 order 계산 (트랜잭션 내부에서 수행하여 데이터 일관성 보장)
        int newOrder;
        if (oldDay == newDay) {
            boolean movingDown = oldOrder < targetOrder;
            if (insertAfter) {
                newOrder = movingDown ? targetOrder : targetOrder + 1;
            } else {
                newOrder = movingDown ? targetOrder - 1 : targetOrder;
            }
        } else {
            newOrder = insertAfter ? targetOrder + 1 : targetOrder;
        }

        if (oldDay == newDay) {
            if (oldOrder < newOrder) {
                routeItemRepository.shiftDownBetween(routeId, oldDay, oldOrder, newOrder, blockId);
            } else if (oldOrder > newOrder) {
                routeItemRepository.shiftUpBetween(routeId, oldDay, newOrder, oldOrder, blockId);
            }
        } else {
            routeItemRepository.shiftDownFrom(routeId, oldDay, oldOrder, blockId);
            routeItemRepository.shiftUpFrom(routeId, newDay, newOrder, blockId);
        }

        routeItemRepository.updateOrderAndDay(blockId, newOrder, newDay);
    }

    public void updateTitle(Long routeId, String title, String username) {
        log.info("루트 제목 수정 요청: ID={}, 제목='{}', 요청자={}", routeId, title, username);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        Long ownerId = routeRepository.findOwnerIdById(routeId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        if (!ownerId.equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.NOT_ROUTE_OWNER);
        }
        route.setTitle(title.trim());
        routeRepository.save(route);
        log.info("루트 제목 수정 완료: ID={}, 제목='{}'", routeId, title);
    }

    @Transactional(readOnly = true)
    public List<RouteListDto> getMyRoutes(String username) {
        log.debug("내 루트 목록 조회: 사용자={}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        return routeRepository.findByMemberUsername(username).stream()
                .map(r -> new RouteListDto(r.getId(), r.getTitle(), r.getOwner().getId().equals(user.getId())))
                .collect(Collectors.toList());
    }

    public Collection<BlockDto> getAllBlocks(Long routeId, String username) {
        log.debug("루트 전체 블록 조회: 루트ID={}", routeId);
        routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        assertMember(routeId, username);
        List<RouteItem> items = routeItemRepository.findByRouteId(routeId);
        List<BlockDto> list = items.stream().map(BlockDto::new).collect(Collectors.toList());
        list.sort(Comparator.comparingInt(BlockDto::getDay).thenComparingInt(BlockDto::getOrder));
        return list;
    }

    public Long addCopiedRoute(Long routeId, String username){
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Route newRoute = Route.builder().title("[복사] "+route.getTitle())
            .items(route.getItems()).owner(user).build();
        routeRepository.save(newRoute);
        return newRoute.getId();
    }
}
