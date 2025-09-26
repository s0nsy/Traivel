import React, {useEffect, useState} from "react";
import { useLocation,useNavigate, useParams } from "react-router-dom";
import NaverServicePage from "../services/naverServicePage.jsx";

const NaverSearchAndPin = () => {
   const [keyword, setKeyword] = useState("");
   const [searchResults, setSearchResults] = useState([]);
   const [message, setMessage] = useState("");
   const [day, setDay] = useState("");
   const [order, setOrder] = useState(null);

   const {routeId} = useParams();

   const handleSearch = async () => {
      if (!keyword.trim()) return;
      try {
         const results = await NaverServicePage.searchPlace(keyword);
         if (results.length === 0) {
            setSearchResults([]);
            setMessage("검색 결과가 없습니다.");
         } else {
            setSearchResults(results);
            setMessage("");
         }
      } catch (error) {
         console.error(error);
         setMessage("검색 중 오류가 발생했습니다.");
      }
   };

   const handleAddPin = async (place) => {
      try {
         const stripTags = (str) => str.replace(/<[^>]*>/g, '');
         const cleanPlace = {
            ...place,
            title: stripTags(place.title)
         };

         await NaverServicePage.addPin(cleanPlace, routeId, day);
         setMessage(`${stripTags(place.title)} 장소를 추가했습니다.`);
      } catch (error) {
         console.error(error);
         setMessage("장소 추가 중 오류가 발생했습니다.");
      }
   };
   useEffect(() => {
      if (window.naver && window.naver.maps) {
         const mapOptions = {
            center: new window.naver.maps.LatLng(37.5665, 126.9780),
            zoom: 10,
         };
         new window.naver.maps.Map("map", mapOptions);
      }
   }, []);

   function stripTags(str) {
      return str.replace(/<[^>]*>?/g, '');
   }
   const navigate = useNavigate();

   const goToRoute = () => {
      navigate(`/route`,{state:{routeId}});
   };

   return (
      <div style={{padding: "100px", maxWidth: "600px", margin: "0 auto", color: "white"}}>
         <button onClick={goToRoute}>루트 관리하기</button>
         <h2>지도</h2>
         <div id="map" style={{width: "100%", height: "400px"}}></div>
         <h2>네이버 장소 검색 & 추가</h2>
         <div style={{display: "flex", marginBottom: "10px"}}>
            <input
               type="text"
               placeholder="검색어 입력"
               value={keyword}
               onChange={(e) => setKeyword(e.target.value)}
               style={{flex: 1, padding: "8px"}}
            />
            <button onClick={handleSearch} style={{marginLeft: "5px", padding: "8px 12px"}}>
               검색
            </button>
         </div>

         {
            message && <p>{message}</p>
         }

         <ul style={{listStyle: "none", padding: 0}}>
            <input type="number" placeholder="일차" value={day} onChange={(e) => setDay(Number(e.target.value))} />
            {searchResults.map((place, idx) => (
               <li
                  key={idx}
                  style={{
                     border: "1px solid #ccc",
                     padding: "10px",
                     marginBottom: "5px",
                     borderRadius: "5px",
                     display: "flex",
                     justifyContent: "space-between",
                     alignItems: "center"
                  }}
               >
                  <div>
                     <strong>{stripTags(place.title)}</strong>
                     <p style={{margin: 0}}>{place.address}</p>
                  </div>
                  <button onClick={() => handleAddPin(place)}>장소 추가</button>
               </li>
            ))}
         </ul>

      </div>
   );
};

export default NaverSearchAndPin;
