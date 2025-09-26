import axios from "axios";

// axios 인스턴스 생성
const api = axios.create({
   baseURL: "https://traivel.p-e.kr/api",
   withCredentials: true,
});

// 모든 요청에 기본 Authorization 헤더 추가
api.interceptors.request.use((config) => {
   const token = localStorage.getItem("jwtToken");
   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }
   return config;
});

const NaverServicePage = {
   searchPlace: async (keyword) => {
      const res = await api.get(`/naver/search?keyword=${encodeURIComponent(keyword)}`);
      if (res.status === 204) return [];
      return res.data;
   },

   addPin: async (place, routeId, day) => {
      const requestBody = {
         routeId,
         naverSearchRequest: {
            title: place.title,
            address: place.address,
            mapx: String(place.mapx),
            mapy: String(place.mapy),
            link: place.link,
            roadAddress: place.roadAddress,
            category: place.category
         },
         order: place.order,
         day: day,
         dtype: "PLACE"
      };
      console.log(requestBody);
      await api.post("/naver/pin", requestBody);
   },
};

export default NaverServicePage;
