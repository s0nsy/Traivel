import React, { useState } from "react";
import { register, login, getNotifications } from "../services/authService";
import { useNavigate } from "react-router-dom";


function Auth() {
   const [username, setUsername] = useState("");
   const [password, setPassword] = useState("");
   const [confirmedPassword, setConfirmedPassword] = useState("");
   const [email, setEmail] = useState("");
   const [notifications, setNotifications] = useState([]);
   const navigate = useNavigate();

   const handleRegister = async () => {
      if (password !== confirmedPassword) {
         alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
         return;
      }

      try {
         const res = await register(username, password, confirmedPassword, email);
         console.log("회원가입 성공:", res);
         alert("회원가입 성공!");
      } catch (err) {
         console.error(err);
         alert("회원가입 실패");
      }
   };

   const handleLogin = async () => {
      try {
         const res = await login(username, password);
         console.log("로그인 성공:", res);
         alert("로그인 성공!");
      } catch (err) {
         console.error(err);
         alert("로그인 실패");
      }

   };

   const handleGetNotifications = async () => {
      try {
         const res = await getNotifications();
         console.log("알림 조회:", res);
         setNotifications(res);
      } catch (err) {
         console.error(err);
         alert("알림 조회 실패");
      }
   };
   const goToChat = () => {
      navigate("/chat");
   };
   const goToMember = () => {
      navigate("/member");
   };
   const goToRoute = () => {
      navigate("/route");
   };
   return (
      <div
         style={{
            paddingTop: "120px",    // 배너 아래로 조정
            paddingLeft: "20px",
            paddingRight: "20px",
            color: "white"
         }}
      >
         <h2>회원가입 / 로그인</h2>

         <div style={{ marginBottom: "10px" }}>
            <input
               placeholder="username"
               value={username}
               onChange={e => setUsername(e.target.value)}
               style={{ marginRight: "10px" }}
            />
            <input
               type="password"
               placeholder="password"
               value={password}
               onChange={e => setPassword(e.target.value)}
               style={{ marginRight: "10px" }}
            />
            <input
               type="password"
               placeholder="confirmed password"
               value={confirmedPassword}
               onChange={e => setConfirmedPassword(e.target.value)}
               style={{ marginRight: "10px" }}
            />
            <input
               placeholder="email"
               value={email}
               onChange={e => setEmail(e.target.value)}
            />
         </div>

         <div style={{ marginBottom: "20px" }}>
            <button onClick={handleRegister} style={{ marginRight: "10px" }}>
               회원가입
            </button>
            <button onClick={handleLogin} style={{ marginRight: "10px" }}>
               로그인
            </button>
            <button onClick={handleGetNotifications}>알림 조회</button>
         </div>

         <ul>
            {notifications.map((n, idx) => (
               <li key={idx}>
                  {n.message} ({n.type})
               </li>
            ))}
         </ul>
         <br/><br/>
         <div>
            <button onClick={goToChat}>
               여행 루트 추천 받기
            </button>
         </div>
         <div>
            <button onClick={goToMember}>
               멤버 관리하기
            </button>
         </div>
         <div>
            <button onClick={goToRoute}>
               루트 관리하기
            </button>
         </div>
      </div>
   );
}

export default Auth;
