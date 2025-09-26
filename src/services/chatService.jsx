const BASE_URL = "https://traivel.p-e.kr/api/chat";
// const BASE_URL = "http://3.38.9.29:8080/chat";

export default BASE_URL;
const token = localStorage.getItem("jwtToken");
export const recommendDestinations = async (travelRequest) => {
   const res = await fetch(`${BASE_URL}/recommend`, {
      method: "POST",
      headers: { "Content-Type": "application/json",
         Authorization: `Bearer ${token}`},
      body: JSON.stringify(travelRequest),
   });
   return await res.json();
};

export const recommendRoute = async (routeRequest) => {
   const res = await fetch(`${BASE_URL}/route`, {
      method: "POST",
      headers: { "Content-Type": "application/json",
         "Authorization": `Bearer ${token}`},
      body: JSON.stringify(routeRequest),
   });
   return await res.text(); // 문자열 반환
};

export const adjustRoute = async (routeId, text) => {
   const res = await fetch(`${BASE_URL}/route/adjust?routeId=${routeId}`, {
      method: "POST",
      headers: {
         "Content-Type": "application/json",
         "Authorization": `Bearer ${token}` // 로그인 시 저장한 JWT 사용
      },
      body: JSON.stringify(text),
   });
   return await res.text();
};
