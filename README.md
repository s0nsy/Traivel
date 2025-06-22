# 🛫 Traivel

**Traivel**은 OpenAI API를 활용해 개인 맞춤형 여행 루트를 추천하고,  
WebSocket 기반 실시간 다인원 협업으로 여행 루트를 함께 수정하며 계획할 수 있는 여행 계획 플랫폼입니다.
<br>
---

## 📌 서비스 소개

- OpenAI GPT API로 사용자의 여행 취향과 조건에 맞는 여행 루트 자동 추천
- 여러 사용자가 동시에 여행 일정을 실시간으로 추가, 편집 가능한 협업 기능 (WebSocket 활용)
- 지도에 여행지 핀을 찍어 시각적으로 루트 확인 가능
- Naver 검색 및 지도 API를 활용해 여행지 정보와 위치 제공
- JWT 인증 기반 권한 관리로 안전한 사용자 관리
<br><br>
---

## 📌 기술 스택

- **Backend**: Spring Boot, MyBatis, WebSocket, REST API
- **Database**: PostgreSQL
- **인프라 및 배포**: AWS EC2, GitHub Actions (CI/CD), Nginx
- **인증**: JWT
- **외부 API**: OpenAI GPT API, Naver Search API, Naver Map API, Gateway API
<br><br>
---

## 🗂 폴더 구조
```
Traivel/
├─ backend/
│ ├─ src/main/java/com/yourorg/traivel/
│ │ ├─ controller/
│ │ ├─ service/
│ │ ├─ mapper/
│ │ ├─ entity/
│ │ │ └─ dto/
│ │ └─ config/
│ │   └─ security/
│ │     ├─ jwt/
│ │     └─ dto/
│ └─ resources/
│   ├─ application.yml
│   ├─ mapper/
│   └─ templates/
├─ .github/
│ └─ workflows/
│ └─ deploy.yml
└─ README.md
```

---

## ⚙️ 개발 환경 및 실행 방법

### 사전 준비

- Java 17 설치
- PostgreSQL 설치 및 실행
- OpenAI API Key 준비
- Naver Map API Key 준비
- Naver Search API Key 준비
- JWT secrets 준비

### 1. 데이터베이스 설정

- PostgreSQL에 데이터베이스 생성
- `src/main/resources/application.yml` 또는 환경 변수에 DB 접속 정보 설정

### 2. 백엔드 실행

```bash
cd Traivel
./gradlew clean build -x test
java -jar build/libs/Traivel-0.0.1-SNAPSHOT.jar
