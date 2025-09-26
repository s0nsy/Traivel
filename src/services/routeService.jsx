import axios from 'axios';

const api = axios.create({
   baseURL: "https://traivel.p-e.kr/api",
   withCredentials: true,
   headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use((config) => {
   const token = localStorage.getItem("jwtToken");
   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }
   return config;
});

const RouteService = {
   addRoute: async (title) => {
      const res = await api.post('/route', null, { params: { title } });
      return res.data; // 생성된 routeId 반환
   },

   getRoute: async (routeId) => {
      const res = await api.get(`/route/${routeId}`);
      return res.data; // BlockDto 배열 반환
   },

   addMemo: async (memoRequest) => {
      // memoRequest = { blockId, content }
      const res = await api.post('/route/memo', memoRequest);
      return res.data;
   },

   editMemo: async (memoId, content) => {
      const res = await api.put(`/route/memo/${memoId}`,    { content });

      return res.data;
   },

   deleteMemo: async (memoId) => {
      const res = await api.delete(`/route/memo/${memoId}`);
      return res.data;
   },

   deletePlace: async (placeId) => {
      const res = await api.delete(`/route/place/${placeId}`);
      return res.data;
   },

   deleteRoute: async (routeId) => {
      const res = await api.delete(`/route/${routeId}`);
      return res.data;
   },
};

export default RouteService;
