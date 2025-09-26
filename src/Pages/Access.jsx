import { useEffect, useState } from "react";
import { getAccessList, createAccess, editAccess, deleteAccess } from "../services/accessService.jsx";

export default function Access() {
   const [list, setList] = useState([]);
   const [result, setResult] = useState("");
   const [title, setTitle] = useState("");
   const [content, setContent] = useState("");

   const loadList = async () => {
      try {
         const data = await getAccessList();
         setList(data);
      } catch (err) {
         console.error(err);
      }
   };

   useEffect(() => {
      loadList();
   }, []);


   useEffect(() => {
      console.log("리스트 확인:", list);
   }, [list]);
   useEffect(() => {
      const loadList = async () => {
         try {
            const data = await getAccessList();
            console.log("API response:", data); // 여기서 데이터 확인
            setList(data);
         } catch (err) {
            console.error("API 호출 실패:", err);
         }
      };

      loadList();
   }, []);


   // POST 생성
   const handleCreate = async () => {
      if (!title || !content) {
         alert("제목과 내용을 입력해주세요!");
         return;
      }

      const formData = new FormData();
      formData.append("title", title);
      formData.append("content", content);

      const res = await createAccess(formData);
      setResult(res);
      setTitle("");    // 입력 초기화
      setContent("");
      loadList();      // 리스트 갱신
   };

   // PATCH 수정
   const handleEdit = async (id) => {
      const formData = new FormData();
      formData.append("title", "수정 제목");
      formData.append("content", "수정 내용");
      const res = await editAccess(id, formData);
      setResult(res);
      getAccessList().then(setList); // 수정 후 리스트 갱신
   };

   // DELETE 삭제
   const handleDelete = async (id) => {
      const res = await deleteAccess(id);
      setResult(res);
      getAccessList().then(setList); // 삭제 후 리스트 갱신
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
         <h1>Access 테스트</h1>

         {/* 제목/내용 입력 */}
         <div style={{ marginBottom: 10 }}>
            <input
               type="text"
               placeholder="제목"
               value={title}
               onChange={(e) => setTitle(e.target.value)}
               style={{ marginRight: 10, padding: 5 }}
            />
            <input
               type="text"
               placeholder="내용"
               value={content}
               onChange={(e) => setContent(e.target.value)}
               style={{ marginRight: 10, padding: 5 }}
            />
            <button onClick={handleCreate} style={{ padding: "5px 10px", backgroundColor: "gray", color: "white" }}>
               POST 생성
            </button>
         </div>

         <h2>리스트</h2>
         {list.map((item) => (
            <div key={item.id} style={{ borderBottom: "1px solid #ccc", padding: 10 }}>
               <div>
                  {item.title} - {item.writer}
               </div>
               <button onClick={() => handleEdit(item.id)}>PATCH 수정</button>
               <button onClick={() => handleDelete(item.id)}>DELETE</button>
            </div>
         ))}

         <h2>결과</h2>
         <pre>{result}</pre>
      </div>
   );
}