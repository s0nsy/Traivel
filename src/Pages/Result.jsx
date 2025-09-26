import {useDispatch, useSelector} from 'react-redux';
import { useLocation } from "react-router-dom";
import React, { useState, useEffect } from 'react';
import { recommendRoute,adjustRoute } from '../services/chatService.jsx';
import { useNavigate } from "react-router-dom";



const RecomRoutePage = () => {
   const location = useLocation();
   const { selectedItem } = location.state;
   const [routeResult, setRouteResult] = useState("");
   const [loading, setLoading] = useState(false);
   const [customRouteId, setCustomRouteId] = useState("");
   const navigate = useNavigate();

   const travelRequestData = useSelector((state) => state.travel.travelRequestData)
      || JSON.parse(localStorage.getItem('travelRequestData'));
   console.log("travelRequestData는 ", travelRequestData)
   useEffect(() => {
      const fetchRoute = async () => {
         if (!selectedItem.destination) return;
         setLoading(true);
         const routeRequest = {
            travelRequest: travelRequestData,
            destination: selectedItem.destination,
            city: selectedItem.city,
            features: selectedItem.features,
         };

         console.log("API 호출 데이터:", routeRequest);
         try {
            const result = await recommendRoute(routeRequest);
            setRouteResult(result);
         } catch (err) {
            console.error("추천 루트 호출 실패:", err);
            setRouteResult("추천 루트 호출 실패");
         } finally {
            setLoading(false); // 완료 시 로딩 끄기
         }
      };

      fetchRoute();
   }, [travelRequestData, selectedItem]);

   const handleSave = async () => {
      try {
         // routeId는 백엔드에서 추천 루트 생성 후 반환한 ID로 사용해야 함
         // 예시로 selectedItem.routeId 또는 travelRequestData.routeId에서 가져오기
         const routeIdToSend = customRouteId ? customRouteId : null;

         const result = await adjustRoute(routeIdToSend, routeResult);
         alert(result); // "루트가 적용되었습니다." 나옴
      } catch (err) {
         console.error("루트 저장 실패:", err);
         alert("루트 저장 실패");
      }
   };
   if (!selectedItem.destination) return <div>선택된 여행지가 없습니다.</div>;

   const goToMember = () => {
      navigate("/member");
   };

   const goToRoute = () => {
      navigate("/route");
   };
   return (
      <div style={{
         paddingTop: "120px",    // 배너 아래로 조정
         paddingLeft: "20px",
         paddingRight: "20px",
         color: "white"
      }}>

         <h1>추천 여행 루트</h1>
         <h2>{selectedItem.destination}</h2>
         {loading ? <div>로딩 중...</div> : <pre>{routeResult}</pre>}
         <br/>

         <input
            type="text"
            placeholder="Route ID를 입력하세요."
            value={customRouteId}
            onChange={(e) => setCustomRouteId(e.target.value)}
            style={{ marginBottom: "10px", padding: "5px" }}
         />
         <button
            onClick={handleSave}
            style={{ marginTop: "10px", padding: "10px 20px" }}
         >
            저장하기
         </button>
         <h5>루트를 생성하지 않았다면 빈칸으로 둔 후, 저장하기 버튼을 눌러주세요.</h5>
         <br/><br/>
         <button onClick={goToMember}>
            멤버 관리하기
         </button><br/>
         <button onClick={goToRoute}>
            루트 관리하기
         </button>
      </div>

   );
};

export default RecomRoutePage;
