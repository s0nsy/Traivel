import React from "react";
import {useNavigate} from "react-router-dom";


const Mainhome = () => {
   const navigate = useNavigate();

   const goToAuth = () => {
      navigate("/auth");
   }
   return(
      <div style={{padding: '100px', color: "white"}}>
         <button onClick={goToAuth}>회원가입/로그인</button>
         <h1 id="traivel">Traivel</h1>

         <p><strong>🛫 Traivel</strong>은 OpenAI API를 활용해 개인 맞춤형 여행 루트를 추천하고,<br/>WebSocket 기반 실시간 다인원 협업으로 여행 루트를 함께 수정하며
            계획할 수 있는 여행 계획 플랫폼입니다.</p>

         <h2 id="서비스소개">- 📌 서비스 소개</h2>
         <ul>
            <li>OpenAI GPT API로 사용자의 여행 취향과 조건에 맞는 여행 루트 자동 추천</li>
            <li>여러 사용자가 동시에 여행 일정을 실시간으로 추가, 편집 가능한 협업 기능 (WebSocket 활용)</li>
            <li>지도에 여행지 핀을 찍어 시각적으로 루트 확인 가능</li>
            <li>Naver 검색 및 지도 API를 활용해 여행지 정보와 위치 제공</li>
            <li>JWT 인증 기반 권한 관리로 안전한 사용자 관리
               <br/><br/></li>
         </ul>
         <hr/>
         <h2 id="기술스택">-📌 기술 스택</h2>
         <ul>
            <li><strong>Backend</strong>: Spring Boot, MyBatis, WebSocket, REST API</li>
            <li><strong>Frontend</strong>: React, Vite</li>
            <li><strong>Database</strong>: PostgreSQL</li>
            <li><strong>인프라 및 배포</strong>: AWS EC2/S3, GitHub Actions (CI/CD), Nginx</li>
            <li><strong>인증</strong>: JWT</li>
            <li><strong>외부 API</strong>: OpenAI GPT API, Naver Search API, Naver Map API, Gateway API
               <br/><br/></li>
         </ul>
         <hr/>
         <h2 id="폴더구조">- 🗂 폴더 구조</h2>
         <pre><code>
            {`Traivel/
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
│   ├─ application<span className="hljs-selector-class">.yml</span>
│   ├─ mapper/
│   └─ templates/
├─ .github/
│ └─ workflows/
│ └─ deploy<span className="hljs-selector-class">.yml</span>
└─ README.md`}
</code></pre>
         <hr/>
         <h2 id="개발실행">- ⚙️ 개발 환경 및 실행 방법</h2>
         <h3 id="사전준비">- 사전 준비</h3>
         <ul>
            <li>Java 17 설치</li>
            <li>PostgreSQL 설치 및 실행</li>
            <li>OpenAI API Key 준비</li>
            <li>Naver Map API Key 준비</li>
            <li>Naver Search API Key 준비</li>
            <li>JWT secrets 준비</li>
         </ul>
         <h3 id="1-">1. 데이터베이스 설정</h3>
         <ul>
            <li>PostgreSQL에 데이터베이스 생성</li>
            <li><code>src/main/resources/application.yml</code> 또는 환경 변수에 DB 접속 정보 설정</li>
         </ul>
         <h3 id="2-">2. 백엔드 실행</h3>
         <p>
            cd Traivel <br/>
            ./gradlew clean build -x test <br/>
            java -jar build/libs/Traivel-0.0.1-SNAPSHOT.jar</p>
         <br/>
         <hr/>
         <h2>- 📌 참고 사항</h2>
         <p>
            지속적으로 확장 중인 프로젝트입니다.
            팀 프로젝트를 진행한 후 개인적으로 기능을 추가했습니다.
         </p>
         <br/>
         <iframe width="560" height="315"
                 src="https://www.youtube.com/embed/35SrA411vrg"
                 title="Swagger 시연 영상"
                 allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                 allowfullscreen>
         </iframe>

      </div>

   );
}

export default Mainhome;