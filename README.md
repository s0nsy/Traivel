# 🛫 Traivel

## https://traivel.p-e.kr/

**Traivel**은 OpenAI API를 활용해 개인 맞춤형 여행 루트를 추천하고,  
WebSocket 기반 실시간 다인원 협업으로 여행 루트를 함께 수정하며 계획할 수 있는 여행 계획 플랫폼입니다.
<br>

---

## 📌 서비스 소개

- OpenAI GPT API로 사용자의 여행 취향과 조건에 맞는 여행 루트 자동 추천
- 여러 사용자가 동시에 여행 일정을 실시간으로 추가, 편집 가능한 협업 기능 (WebSocket 활용)
- 지도에 여행지 핀을 찍어 시각적으로 루트 확인 가능
- Naver 검색 및 지도 API를 활용해 여행지 정보와 위치 제공
- JWT 인증 기반 권한 관리 (HttpOnly 쿠키 방식, Access Token + Refresh Token)
- Redis 분산 락을 통한 동시성 제어
- Kafka 기반 비동기 알림 시스템 (초대 알림, 인기 여행지 이벤트)
- Elasticsearch + nori 형태소 분석기를 활용한 후기 전문 검색
- AWS S3 및 로컬 스토리지 이미지 업로드 지원
<br><br>

---

## 📌 기술 스택

- **Backend**: Spring Boot, Spring Data JPA, WebSocket, REST API
- **Database**: PostgreSQL, Flyway (마이그레이션)
- **Cache / Lock**: Redis
- **Message Queue**: Apache Kafka
- **Search**: Elasticsearch (nori 형태소 분석기)
- **인프라 및 배포**: AWS EC2, AWS S3, Docker, GitHub Actions (CI/CD), Nginx
- **인증**: JWT (HttpOnly 쿠키, Access Token + Refresh Token)
- **API 문서**: Swagger (Springdoc)
- **외부 API**: OpenAI GPT API, Naver Search API, Naver Map API
<br><br>

---

## 🗂 폴더 구조

```
src/main/java/com/example/project/
├─ controller/
├─ service/
│  └─ storage/
├─ repository/
├─ entity/
│  ├─ base/
│  ├─ document/            (Elasticsearch 도큐먼트)
│  └─ dto/
├─ kafka/                  (Producer / Consumer / Event)
├─ exception/
└─ config/
   └─ security/
      ├─ jwt/
      └─ dto/
src/main/resources/
├─ application.yml
├─ application-local.yml   (gitignore, 로컬 전용)
├─ db/migration/           (Flyway SQL)
├─ elasticsearch/          (index 매핑 및 설정 JSON)
└─ logback-spring.xml
```

---

## ⚙️ 개발 환경 및 실행 방법

### 사전 준비

- Java 17
- PostgreSQL
- Redis
- Apache Kafka
- Elasticsearch (nori 플러그인 설치 필요)
- OpenAI API Key
- Naver Map / Search API Key

### 1. 환경 변수 설정

| 변수명 | 설명 |
|---|---|
| `DB_URL` | PostgreSQL JDBC URL |
| `DB_USERNAME` | DB 사용자명 |
| `DB_PASSWORD` | DB 비밀번호 |
| `REDIS_HOST` | Redis 호스트 |
| `JWT_SECRET` | JWT 서명 키 |
| `OPENAI_API_KEY` | OpenAI API Key |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka 브로커 주소 (기본값: `localhost:9092`) |
| `NAVER_SEARCH_ID` | Naver Search API ID |
| `NAVER_SEARCH_SECRET` | Naver Search API Secret |
| `NAVER_MAP_ID` | Naver Map API ID |
| `NAVER_MAP_SECRET` | Naver Map API Secret |
| `AMAZON_ACCESS_KEY` | AWS Access Key |
| `AMAZON_SECRET_ACCESS_KEY` | AWS Secret Key |
| `AWS_S3_BUCKET` | S3 버킷 이름 |

로컬 실행 시에는 `src/main/resources/application-local.yml`에 datasource와 Redis 호스트를 설정합니다.

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/traivel
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
```

### 2. Docker로 실행 (권장)

```bash
docker-compose up -d
```

### 3. 직접 빌드 및 실행

```bash
./gradlew clean build -x test
java -jar build/libs/project-0.0.1-SNAPSHOT.jar
```

### 4. API 문서 확인

서버 실행 후 Swagger UI: `http://localhost:8080/swagger-ui.html`
