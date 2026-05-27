package com.example.project.repository;

import com.example.project.entity.Route;
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
class RouteRepositoryTest {

    @Autowired RouteRepository routeRepository;
    @Autowired UserRepository userRepository;
    @Autowired TestEntityManager em;

    private User owner;
    private Route route;

    @BeforeEach
    void setUp() {
        User u = new User();
        u.setUsername("routeowner");
        u.setPassword("encoded");
        u.setEmail("owner@example.com");
        u.setRole("ROLE_USER");
        owner = userRepository.save(u);

        Route r = new Route();
        r.setTitle("테스트 루트");
        r.setOwner(owner);
        route = routeRepository.save(r);

        em.flush();
    }

    // ── findOwnerIdById ──────────────────────────────────────────────────────

    @Test
    void findOwnerIdById_returnsOwnerId() {
        Long ownerId = routeRepository.findOwnerIdById(route.getId());

        assertThat(ownerId).isEqualTo(owner.getId());
    }

    // ── findUsersByRouteId ───────────────────────────────────────────────────

    @Test
    void findUsersByRouteId_noMembers_returnsEmpty() {
        List<User> users = routeRepository.findUsersByRouteId(route.getId());

        assertThat(users).isEmpty();
    }

    @Test
    void findUsersByRouteId_afterAddRouteUser_containsMember() {
        User member = saveUser("member1", "m1@example.com");
        routeRepository.addRouteUser(route.getId(), member.getId());
        em.flush();
        em.clear();

        List<User> users = routeRepository.findUsersByRouteId(route.getId());

        assertThat(users).extracting(User::getUsername).containsExactly("member1");
    }

    // ── addRouteUser / deleteRouteUser ───────────────────────────────────────

    @Test
    void deleteRouteUser_removesOnlyTargetMember() {
        User m1 = saveUser("del1", "del1@example.com");
        User m2 = saveUser("del2", "del2@example.com");
        routeRepository.addRouteUser(route.getId(), m1.getId());
        routeRepository.addRouteUser(route.getId(), m2.getId());
        em.flush();
        em.clear();

        routeRepository.deleteRouteUser(route.getId(), m1.getId());
        em.flush();
        em.clear();

        List<User> users = routeRepository.findUsersByRouteId(route.getId());
        assertThat(users).extracting(User::getUsername)
                .doesNotContain("del1")
                .contains("del2");
    }

    // ── deleteAllRouteUsers ──────────────────────────────────────────────────

    @Test
    void deleteAllRouteUsers_removesAllMembers() {
        User m1 = saveUser("all1", "all1@example.com");
        User m2 = saveUser("all2", "all2@example.com");
        routeRepository.addRouteUser(route.getId(), m1.getId());
        routeRepository.addRouteUser(route.getId(), m2.getId());
        em.flush();
        em.clear();

        routeRepository.deleteAllRouteUsers(route.getId());
        em.flush();
        em.clear();

        List<User> users = routeRepository.findUsersByRouteId(route.getId());
        assertThat(users).isEmpty();
    }

    private User saveUser(String username, String email) {
        User u = new User();
        u.setUsername(username);
        u.setPassword("encoded");
        u.setEmail(email);
        u.setRole("ROLE_USER");
        return userRepository.save(u);
    }
}
