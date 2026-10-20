package com.example.project.repository;

import com.example.project.entity.Memo;
import com.example.project.entity.MemoItem;
import com.example.project.entity.Route;
import com.example.project.entity.RouteItem;
import com.example.project.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class RouteItemRepositoryTest {

    @Autowired RouteItemRepository routeItemRepository;
    @Autowired MemoItemRepository memoItemRepository;
    @Autowired RouteRepository routeRepository;
    @Autowired UserRepository userRepository;
    @Autowired TestEntityManager em;

    private Route route;

    @BeforeEach
    void setUp() {
        User owner = new User();
        owner.setUsername("itemowner");
        owner.setPassword("encoded");
        owner.setEmail("itemowner@example.com");
        owner.setRole("ROLE_USER");
        owner = userRepository.save(owner);

        Route r = new Route();
        r.setTitle("아이템 테스트 루트");
        r.setOwner(owner);
        route = routeRepository.save(r);

        em.flush();
    }

    // ── findMaxOrderByRouteIdAndDay ──────────────────────────────────────────

    @Test
    void findMaxOrderByRouteIdAndDay_noItems_returnsZero() {
        int max = routeItemRepository.findMaxOrderByRouteIdAndDay(route.getId(), 1);

        assertThat(max).isZero();
    }

    @Test
    void findMaxOrderByRouteIdAndDay_multipleItems_returnsMax() {
        saveMemoItem(1, 1);
        saveMemoItem(2, 1);
        saveMemoItem(3, 1);
        em.flush();
        em.clear();

        int max = routeItemRepository.findMaxOrderByRouteIdAndDay(route.getId(), 1);

        assertThat(max).isEqualTo(3);
    }

    @Test
    void findMaxOrderByRouteIdAndDay_differentDay_returnsZero() {
        saveMemoItem(1, 2);
        em.flush();
        em.clear();

        int max = routeItemRepository.findMaxOrderByRouteIdAndDay(route.getId(), 1);

        assertThat(max).isZero();
    }

    // ── findByRouteIdAndDay ──────────────────────────────────────────────────

    @Test
    void findByRouteIdAndDay_returnsItemsOrderedByOrder() {
        saveMemoItem(2, 1);
        saveMemoItem(1, 1);
        saveMemoItem(3, 1);
        saveMemoItem(1, 2);
        em.flush();
        em.clear();

        List<RouteItem> items = routeItemRepository.findByRouteIdAndDay(route.getId(), 1);

        assertThat(items).hasSize(3);
        assertThat(items).extracting(RouteItem::getOrder).containsExactly(1, 2, 3);
    }

    @Test
    void findByRouteIdAndDay_noMatch_returnsEmpty() {
        List<RouteItem> items = routeItemRepository.findByRouteIdAndDay(route.getId(), 99);

        assertThat(items).isEmpty();
    }

    // ── findByRouteId ────────────────────────────────────────────────────────

    @Test
    void findByRouteId_returnsAllItemsOrderedByDayThenOrder() {
        saveMemoItem(1, 2);
        saveMemoItem(1, 1);
        saveMemoItem(2, 1);
        em.flush();
        em.clear();

        List<RouteItem> items = routeItemRepository.findByRouteId(route.getId());

        assertThat(items).hasSize(3);
        assertThat(items.get(0).getDay()).isEqualTo(1);
        assertThat(items.get(1).getDay()).isEqualTo(1);
        assertThat(items.get(2).getDay()).isEqualTo(2);
    }

    // ── updateOrderAndDay ────────────────────────────────────────────────────

    @Test
    void updateOrderAndDay_changesValues() {
        MemoItem item = saveMemoItem(1, 1);
        em.flush();
        em.clear();

        routeItemRepository.updateOrderAndDay(item.getId(), 5, 3);
        em.flush();
        em.clear();

        RouteItem updated = routeItemRepository.findById(item.getId()).orElseThrow();
        assertThat(updated.getOrder()).isEqualTo(5);
        assertThat(updated.getDay()).isEqualTo(3);
    }

    private MemoItem saveMemoItem(int order, int day) {
        Memo memo = new Memo();
        memo.setContent("메모 내용");
        memo.setRoute(route);

        MemoItem item = new MemoItem();
        item.setOrder(order);
        item.setDay(day);
        item.setRoute(route);
        item.setMemo(memo);
        return memoItemRepository.save(item);
    }
}
