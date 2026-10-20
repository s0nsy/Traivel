package com.example.project.repository;

import com.example.project.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    private User buildUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("encoded-password");
        user.setEmail(email);
        user.setRole("ROLE_USER");
        return user;
    }

    // ── findByUsername ───────────────────────────────────────────────────────

    @Test
    void findByUsername_existingUser_returnsUser() {
        userRepository.save(buildUser("alice", "alice@example.com"));

        Optional<User> result = userRepository.findByUsername("alice");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void findByUsername_unknown_returnsEmpty() {
        Optional<User> result = userRepository.findByUsername("nobody");

        assertThat(result).isEmpty();
    }

    // ── UUID 자동 생성 ────────────────────────────────────────────────────────

    @Test
    void save_generatesUuid_onPersist() {
        User saved = userRepository.save(buildUser("bob", "bob@example.com"));

        assertThat(saved.getUuid()).isNotNull();
        assertThat(saved.getUuid()).hasSize(36);
    }

    @Test
    void save_twoUsers_haveDistinctUuids() {
        User u1 = userRepository.save(buildUser("carol1", "carol1@example.com"));
        User u2 = userRepository.save(buildUser("carol2", "carol2@example.com"));

        assertThat(u1.getUuid()).isNotEqualTo(u2.getUuid());
    }

    // ── BaseEntity auditing ──────────────────────────────────────────────────

    @Test
    void save_populatesCreatedAt() {
        User saved = userRepository.save(buildUser("dave", "dave@example.com"));

        assertThat(saved.getCreatedAt()).isNotNull();
    }
}
