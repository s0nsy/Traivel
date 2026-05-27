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

    public void addPin(AddPlaceToRouteRequest request) {
        NaverPlaceDto place = request.getPlace();
        log.info("장소 추가 요청: 장소='{}', 루트ID={}", place.getTitle(), request.getRouteId());

        Place newPlace = new Place();
        newPlace.setTitle(place.getTitle());
        newPlace.setAddress(place.getAddress());
        newPlace.setRoadAddress(place.getRoadAddress());
        newPlace.setLink(place.getLink());
        newPlace.setCategory(place.getCategory());
        newPlace.setMapx(place.getMapx());
        newPlace.setMapy(place.getMapy());

        Route route = routeRepository.findById(request.getRouteId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
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
    }

    public void addMemo(MemoRequest memoRequest) {
        log.info("메모 추가 요청: 루트ID={}, day={}", memoRequest.getRouteId(), memoRequest.getDay());
        Route route = routeRepository.findById(memoRequest.getRouteId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        Memo memo = new Memo();
        memo.setContent(memoRequest.getContent());
        memo.setRoute(route);
        memoRepository.save(memo);

        int maxOrder = routeItemRepository.findMaxOrderByRouteIdAndDay(memoRequest.getRouteId(), memoRequest.getDay());
        MemoItem memoItem = new MemoItem();
        memoItem.setMemo(memo);
        memoItem.setRoute(route);
        memoItem.setDay(memoRequest.getDay());
        memoItem.setOrder(maxOrder + 1);
        routeItemRepository.save(memoItem);
        log.debug("메모 추가 완료: 메모ID={}", memo.getId());
    }

    public void editMemo(Long id, String content) {
        log.info("메모 수정 요청: ID={}", id);
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMO_NOT_FOUND));
        memo.setContent(content);
        memoRepository.save(memo);
        log.debug("메모 수정 완료: ID={}", id);
    }

    public String deleteMemo(Long memoId) {
        log.info("메모 삭제 요청: ID={}", memoId);
        Memo memo = memoRepository.findById(memoId).orElse(null);
        MemoItem memoItem = memoItemRepository.findByMemoId(memoId).orElse(null);
        if (memo == null || memoItem == null) {
            log.warn("메모 삭제 실패 - 존재하지 않음: ID={}", memoId);
            throw new NotFoundException(ErrorCode.MEMO_NOT_FOUND);
        }
        routeItemRepository.deleteById(memoItem.getId());
        memoRepository.deleteById(memoId);
        log.info("메모 삭제 완료: ID={}", memoId);
        return "삭제되었습니다";
    }

    public String deletePlace(Long placeId) {
        log.info("장소 삭제 요청: ID={}", placeId);
        Place place = placeRepository.findById(placeId).orElse(null);
        PlaceItem placeItem = placeItemRepository.findByPlaceId(placeId).orElse(null);
        if (placeItem == null || place == null) {
            log.warn("장소 삭제 실패 - 존재하지 않음: ID={}", placeId);
            throw new NotFoundException(ErrorCode.PLACE_NOT_FOUND);
        }
        routeItemRepository.deleteById(placeItem.getId());
        placeRepository.deleteById(placeId);
        log.info("장소 삭제 완료: ID={}", placeId);
        return "삭제되었습니다";
    }

    @Transactional
    public void deleteRoute(Long routeId, User user) {
        log.info("루트 삭제 요청: ID={}, 요청자={}", routeId, user.getUsername());
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        Long ownerId = routeRepository.findOwnerIdById(routeId);
        if (!ownerId.equals(user.getId())) {
            log.warn("루트 삭제 권한 없음: ID={}, 요청자={}", routeId, user.getUsername());
            throw new ForbiddenException(ErrorCode.NOT_ROUTE_OWNER);
        }
        routeRepository.deleteAllRouteUsers(routeId);
        placeRepository.findIdsByRouteId(routeId).forEach(this::deletePlace);
        memoRepository.findIdsByRouteId(routeId).forEach(this::deleteMemo);
        routeRepository.delete(route);
        log.info("루트 삭제 완료: ID={}", routeId);
    }

    public void joinRoute(String token, User user) {
        log.info("루트 참가 요청: 사용자={}, 토큰={}", user.getUsername(), token);
        Invite invite = inviteRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_INVITE_TOKEN));
        Route route = routeRepository.findById(invite.getRouteId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        List<User> users = routeRepository.findUsersByRouteId(route.getId());
        if (users.contains(user)) {
            log.info("루트 참가 생략 - 이미 멤버: 사용자={}, 루트ID={}", user.getUsername(), route.getId());
            return;
        }
        if (users.size() >= 30) {
            log.warn("루트 참가 거부 - 멤버 상한 초과: 루트ID={}, 현재인원={}", route.getId(), users.size());
            throw new BusinessException(ErrorCode.ROUTE_MEMBER_LIMIT_EXCEEDED);
        }
        routeRepository.addRouteUser(route.getId(), user.getId());
        log.info("루트 참가 완료: 사용자={}, 루트ID={}", user.getUsername(), route.getId());
    }

    // Route 행에 대한 비관적 락으로 db 레벨 이중 보호.
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Collection<BlockDto> moveBlock(Long routeId, Long blockId, int newOrder, int newDay) {
        log.debug("블록 이동 요청: 블록ID={}, 루트ID={}, newDay={}, newOrder={}", blockId, routeId, newDay, newOrder);

        String lockToken = distributedLockService.acquire(routeId);
        try {
            transactionTemplate.executeWithoutResult(status -> {
                routeRepository.findByIdWithLock(routeId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
                executeReorder(routeId, blockId, newOrder, newDay);
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

    private void executeReorder(Long routeId, Long blockId, int newOrder, int newDay) {
        RouteItem target = routeItemRepository.findById(blockId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BLOCK_NOT_FOUND));

        int oldDay = target.getDay();
        int oldOrder = target.getOrder();

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

    public Collection<BlockDto> getAllBlocks(Long routeId) {
        log.debug("루트 전체 블록 조회: 루트ID={}", routeId);
        routeRepository.findById(routeId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROUTE_NOT_FOUND));
        List<RouteItem> items = routeItemRepository.findByRouteId(routeId);
        List<BlockDto> list = items.stream().map(BlockDto::new).collect(Collectors.toList());
        list.sort(Comparator.comparingInt(BlockDto::getDay).thenComparingInt(BlockDto::getOrder));
        return list;
    }
}
