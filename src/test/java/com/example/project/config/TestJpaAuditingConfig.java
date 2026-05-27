package com.example.project.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @DataJpaTest 슬라이스는 ProjectApplication을 로드하지 않으므로
 * @EnableJpaAuditing이 비활성화된다. BaseEntity의 createdAt/updatedAt
 * (nullable=false)을 채우기 위해 각 레포지토리 테스트에 @Import 한다.
 */
@TestConfiguration
@EnableJpaAuditing
public class TestJpaAuditingConfig {}
