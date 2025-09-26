const BASE_URL = "https://traivel.p-e.kr/api/access";

const getToken = () => localStorage.getItem("jwtToken");

// GET 리스트
export const getAccessList = async () => {
   const token = localStorage.getItem("jwtToken");
   console.log("JWT Token:", token);

   const res = await fetch(BASE_URL, {
      headers: { "Authorization": `Bearer ${token}` }
   });

   if (!res.ok) {
      const text = await res.text();
      throw new Error(`HTTP ${res.status}: ${text}`);
   }

   return res.json(); // 여기서 끝
};


// POST 생성
export const createAccess = async (formData) => {
   const res = await fetch(`${BASE_URL}/write`, {
      method: "POST",
      headers: { "Authorization": `Bearer ${getToken()}` },
      body: formData
   });
   return res.text();
};

// PATCH 수정
export const editAccess = async (id, formData) => {
   const res = await fetch(`${BASE_URL}/${id}`, {
      method: "PATCH",
      headers: { "Authorization": `Bearer ${getToken()}` },
      body: formData
   });
   return res.text();
};

// DELETE 삭제
export const deleteAccess = async (id) => {
   const res = await fetch(`${BASE_URL}/${id}`, {
      method: "DELETE",
      headers: { "Authorization": `Bearer ${getToken()}` }
   });
   return res.text();
};
