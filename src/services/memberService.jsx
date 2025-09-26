import axios from "axios";

const MemberPage = axios.create({
   baseURL: "https://traivel.p-e.kr/api/member",
   // baseURL: "https://localhost:8080/member",
   withCredentials: true,
});

MemberPage.interceptors.request.use(config => {
   const token = localStorage.getItem("jwtToken");
   if(token) config.headers.Authorization = `Bearer ${token}`;
   return config;
});

export default MemberPage;
