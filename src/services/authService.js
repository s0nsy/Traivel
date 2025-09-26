const BASE_URL = "https://traivel.p-e.kr/api/auth";
// const BASE_URL = "http://localhost:8080/auth";

export const register = async (username, password, confirmedPassword, email) => {
   const response = await fetch(`${BASE_URL}/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password, confirmedPassword, email })
   });

   if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
   return await response.json();
};

export const login = async (username, password) => {
   const res = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
   });
   // text로 받아서 처리
   const text = await res.text();
   console.log("raw:", text);

// 만약 text가 "Token = ey..." 형태라면 토큰 부분만 파싱
   const token = text.replace("Token = ", "").trim();

   localStorage.setItem("jwtToken", token);
   return token;
};

export const getNotifications = async () => {
   const token = localStorage.getItem("jwtToken");
   const response = await fetch(`${BASE_URL}/notifications`, {
      method: "GET",
      headers: { "Authorization": `Bearer ${token}` }
   });

   if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
   return await response.json();
};
