package com.example.project.repository;

import com.example.project.entity.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class RefreshTokenRepositoryTest {

    @Autowired RefreshTokenRepository refreshTokenRepository;
    @Autowired TestEntityManager em;

    // ── findByToken ──────────────────────────────────────────────────────────

    @Test
    void findByToken_existingToken_returnsToken() {
        save("token-abc", "alice", future(1));

        Optional<RefreshToken> result = refreshTokenRepository.findByToken("token-abc");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void findByToken_nonExistent_returnsEmpty() {
        Optional<RefreshToken> result = refreshTokenRepository.findByToken("no-such-token");

        assertThat(result).isEmpty();
    }

    // ── deleteByUsername ─────────────────────────────────────────────────────

    @Test
    void deleteByUsername_removesAllTokensForUser_leavesOthers() {
        save("t1", "bob", future(1));
        save("t2", "bob", future(2));
        save("t3", "carol", future(1));
        em.flush();

        refreshTokenRepository.deleteByUsername("bob");
        em.flush();
        em.clear();

        assertThat(refreshTokenRepository.findByToken("t1")).isEmpty();
        assertThat(refreshTokenRepository.findByToken("t2")).isEmpty();
        assertThat(refreshTokenRepository.findByToken("t3")).isPresent();
    }

    @Test
    void deleteByUsername_noTokensForUser_doesNothing() {
        save("t1", "dave", future(1));
        em.flush();

        refreshTokenRepository.deleteByUsername("nobody");
        em.flush();
        em.clear();

        assertThat(refreshTokenRepository.findByToken("t1")).isPresent();
    }

    // ── deleteAllExpired ─────────────────────────────────────────────────────

    @Test
    void deleteAllExpired_removesExpiredTokens_keepsValid() {
        save("expired1", "eve", past(2));
        save("expired2", "eve", past(1));
        save("valid1", "eve", future(1));
        em.flush();

        refreshTokenRepository.deleteAllExpired(LocalDateTime.now());
        em.flush();
        em.clear();

        assertThat(refreshTokenRepository.findByToken("expired1")).isEmpty();
        assertThat(refreshTokenRepository.findByToken("expired2")).isEmpty();
        assertThat(refreshTokenRepository.findByToken("valid1")).isPresent();
    }

    @Test
    void deleteAllExpired_nothingExpired_keepsAll() {
        save("v1", "frank", future(1));
        save("v2", "frank", future(2));
        em.flush();

        refreshTokenRepository.deleteAllExpired(LocalDateTime.now());
        em.flush();
        em.clear();

        assertThat(refreshTokenRepository.findByToken("v1")).isPresent();
        assertThat(refreshTokenRepository.findByToken("v2")).isPresent();
    }

    // ── isExpired (엔티티 메서드) ─────────────────────────────────────────────

    @Test
    void isExpired_futureToken_returnsFalse() {
        RefreshToken token = new RefreshToken("t", "user", future(1));

        assertThat(token.isExpired()).isFalse();
    }

    @Test
    void isExpired_pastToken_returnsTrue() {
        RefreshToken token = new RefreshToken("t", "user", past(1));

        assertThat(token.isExpired()).isTrue();
    }

    private void save(String token, String username, LocalDateTime expiresAt) {
        refreshTokenRepository.save(new RefreshToken(token, username, expiresAt));
    }

    private LocalDateTime future(int hours) {
        return LocalDateTime.now().plusHours(hours);
    }

    private LocalDateTime past(int hours) {
        return LocalDateTime.now().minusHours(hours);
    }
}
