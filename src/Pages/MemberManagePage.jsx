import React, { useState, useEffect } from "react";
import { useParams,useNavigate } from "react-router-dom";
import MemberPage from "../services/memberService.jsx";

export default function MemberManagePage() {
   const navigate = useNavigate();
   // 상태 관리
   const [inviteLink, setInviteLink] = useState("");
   const [inviteUsername, setInviteUsername] = useState("");
   const [joinToken, setJoinToken] = useState("");
   const [evictUsername, setEvictUsername] = useState("");
   const [notifications, setNotifications] = useState([]);
   const [routeId, setRouteId] = useState("");
   const [members, setMembers] = useState([]);

   // 1. 초대 링크 생성
   const createLink = async () => {
      try {
         const res = await MemberPage.post("/link", null, { params: { routeId } });
         setInviteLink(res.data);
      } catch (err) {
         console.error(err);
         alert("초대 링크 생성 실패");
      }
   };

   // 2. 초대 알림 전송
   const sendInvite = async () => {
      if (!inviteUsername || !inviteLink) {
         alert("사용자 이름과 링크를 입력하세요.");
         return;
      }
      try {
         await MemberPage.post("/invite/notification", null, {
            params: { username: inviteUsername, link: inviteLink }
         });
         alert("초대 알림 전송 완료");
         setInviteUsername("");
      } catch (err) {
         console.error(err);
         alert("초대 알림 전송 실패");
      }
   };

   // 3. 초대 코드로 입장
   const joinRoute = async () => {
      if (!joinToken) return alert("토큰을 입력하세요.");
      try {
         await MemberPage.get(`/invite/${joinToken}`);
         alert("정상적으로 초대되었습니다.");
         setJoinToken("");
      } catch (err) {
         console.error(err);
         alert("입장 실패");
      }
   };

   // // 4. 멤버 퇴장
   // const evictMember = async () => {
   //    if (!evictUsername) return alert("사용자 이름을 입력하세요.");
   //    try {
   //       await MemberPage.delete("/eviction/member", { params: { routeId, username: evictUsername } });
   //       alert(`${evictUsername}을 퇴장시켰습니다.`);
   //       setEvictUsername("");
   //    } catch (err) {
   //       console.error(err);
   //       alert("퇴장 실패");
   //    }
   // };

   // 5. 알림 조회
   const fetchNotifications = async () => {
      try {
         const res = await MemberPage.get("/notification");
         setNotifications(res.data);
      } catch (err) {
         console.error(err);
      }
   };

   // 6. 멤버 조회
   const fetchMember = async()=>{
      try{
         const res = await MemberPage.get(`/list`,  {params: { routeId }});
         setMembers(res.data);
      }catch(err){
         console.error(err);
      }
   }

   const copyLink = () => {
      if (!inviteLink) return;
      navigator.clipboard.writeText(inviteLink)
         .then(() => alert("링크가 복사되었습니다!"))
         .catch(err => alert("복사 실패: " + err));
   };
   const goToChat = () => {
      navigate("/chat");
   };
   const goToRoute = () => {
      navigate("/route");
   };

   useEffect(() => {
      fetchNotifications();
      // 알림 실시간 업데이트는 필요 시 interval 추가 가능
   }, []);

   return (
      <div style={{ padding: "100px" , color:"white"}}>
         <h2>여행 루트 관리</h2>
         {/* 1. 초대 링크 생성 */}
         <div>
            <input
               placeholder="루트 아이디"
               value={routeId}
               onChange={e => setRouteId(e.target.value)}
            />
            <button onClick={fetchMember}>확인</button>

         </div><br/>
         <div>
            <h3>초대 링크 생성</h3>
            <button onClick={createLink}>초대 링크 생성</button>
            {inviteLink && (
               <div>
                  <p>생성된 링크: {inviteLink}</p>
                  <button onClick ={copyLink}>복사</button>
               </div>
            )}
         </div>
         <hr/>

         {/* 2. 초대 알림 전송 */}
         <div>
            <h3>초대 알림 전송</h3>
            <input
               placeholder="초대할 사용자 이름"
               value={inviteUsername}
               onChange={e => setInviteUsername(e.target.value)}
            />
            <input
               placeholder="링크"
               value={inviteLink}
               readOnly
            />
            <button onClick={sendInvite}>알림 보내기</button>
         </div>
         <hr/>

         {/* 3. 초대 코드로 입장 */}
         <div>
            <h3>초대 코드로 입장</h3>
            <input
               placeholder="초대 코드"
               value={joinToken}
               onChange={e => setJoinToken(e.target.value)}
            />
            <button onClick={joinRoute}>입장</button>
         </div>
         <hr/>

         {/* 4. 멤버 퇴장 */}
         <div>
            <h3>멤버 퇴장</h3>
            <ul>
            {members.map((n, i) => (
               <div key={i}>
                  [{n}{"  "}
                  <button
                     onClick={async ()=>{
                        try{
                           await MemberPage.post("/eviction/member", { routeId, username: n });
                           setMembers(prev=>prev.filter((_,index)=>index!==i));

                        } catch (err) {
                           console.error("퇴장 실패:", err);
                        }
                     }}
                  >
                     방출
                  </button>
               </div>
            ))}
            </ul>

         </div>

         <hr/>

         {/* 5. 알림 조회 */}
         <div>
            <h3>알림 목록</h3>
            <button onClick={fetchNotifications}>새로고침</button>
            <ul>
               {notifications.map((n, i) => (
                  <li key={i}>
                     [{n.type}] {n.message} - {n.content}{"  "}
                     <button
                        onClick={() => {
                           navigator.clipboard.writeText(n.content)
                              .then(() => alert("알림 내용이 복사되었습니다!"))
                              .catch(err => alert("복사 실패: " + err));
                        }}
                     >
                        복사
                     </button>
                  </li>
               ))}
            </ul>
         </div>
         <br/><br/>
         <div>
            <button onClick={goToChat}>
               여행 루트 추천 받기
            </button><br/>
            <button onClick={goToRoute}>
               루트 관리하기
            </button>
         </div>
      </div>
   );
}
