import {useState, useEffect} from 'react';
import RouteService from '../services/routeService.jsx';
import Websocket from "./websocket.js"
import { sendMoveBlock } from "../services/websocketService";
import { useNavigate,useLocation } from "react-router-dom";


const RouteManagePage = () => {
   const [title, setTitle] = useState('');
   const location = useLocation();
   const stateRouteId = location.state?.routeId;
   const [routeId, setRouteId] = useState(stateRouteId || null);
   const [blocks, setBlocks] = useState([]);
   const [memoContent, setMemoContent] = useState('');
   const [editMemoId, setEditMemoId] = useState(null);
   const [editContent, setEditContent] = useState('');
   const [order, setOrder] = useState();
   const [day, setDay] = useState()
   const [blockId, setBlockId] = useState(1)
   const [newDay, setNewDay] = useState(1)
   const [newOrder, setNewOrder] = useState(1)
   const [connected, setConnected] = useState(false);

   useEffect(() => {
      if (stateRouteId) {
         fetchRoute(stateRouteId);
      }
   }, [stateRouteId]);

   const fetchRoute = async (id) => {
      try {
         const res = await RouteService.getRoute(id);
         setBlocks(res);
      } catch (err) {
         console.error(err);
         alert('루트 조회 실패');
      }
   };

   // 루트 생성
   const handleAddRoute = async () => {
      try {
         const id = await RouteService.addRoute(title);
         setRouteId(id);
         alert(`루트가 생성되었습니다. ID: ${id}`);
      } catch (err) {
         console.error(err);
         alert('루트 생성 실패');
      }
   };

   // 루트 조회
   const handleGetRoute = async () => {
      try {
         const res = await RouteService.getRoute(routeId);
         setBlocks(res);
         alert(`루트가 조회되었습니다. ${JSON.stringify(res, null, 2)} 루트 제목: ${res}`);
      } catch (err) {
         console.error(err);
         alert('루트 조회 실패');
      }
   };

   // 메모 추가
   const handleAddMemo = async () => {
      try {
         await RouteService.addMemo({
            routeId: Number(routeId),
            content: memoContent,
            order: order,
            day: day,
            dtype: "MEMO"
         });
         setMemoContent('');
         handleGetRoute();
      } catch (err) {
         console.error(err);
         alert('메모 추가 실패: 루트를 조회해주세요.');
      }
   };

   // 메모 수정
   const handleEditMemo = async () => {
      try {
         await RouteService.editMemo(editMemoId, editContent);
         setEditMemoId(null);
         setEditContent('');
         handleGetRoute();
      } catch (err) {
         console.error(err);
         alert('메모 수정 실패');
      }
   };

   // 메모 삭제
   const handleDeleteMemo = async (memoId) => {
      try {
         await RouteService.deleteMemo(memoId);
         handleGetRoute();
      } catch (err) {
         console.error(err);
         alert('메모 삭제 실패');
      }
   };

   // 장소 삭제
   const handleDeletePlace = async (placeId) => {
      try {
         await RouteService.deletePlace(placeId);
         handleGetRoute();
      } catch (err) {
         console.error(err);
         alert('장소 삭제 실패');
      }
   };

   // 루트 삭제
   const handleDeleteRoute = async () => {
      try {
         await RouteService.deleteRoute(routeId);
         setRouteId(null);
         setBlocks([]);
         alert('루트 삭제 완료');
      } catch (err) {
         console.error(err);
         alert('루트 삭제 실패');
      }
   };
      const blocksByDay = blocks.reduce((acc, block) => {
         const day = block.day;
         if (!acc[day]) acc[day] = [];
         acc[day].push(block);
         return acc;
      }, {});

   const handleMoveBlock = (blockId, newDay, newOrder) => {
      sendMoveBlock({routeId, blockId, newDay, newOrder });
   };

   const navigate = useNavigate();

   const searchPlace = () => {
      navigate(`/naver/${routeId}`);
   };
      return (
         <div style={{padding: '100px', color: "white"}}>

            {/* 웹소켓 */}
            {routeId && (
               <Websocket
                  routeId={routeId}
                  onBlocksUpdate={(updatedBlocks) => setBlocks(updatedBlocks)}
                  setConnected={setConnected}
               />
            )}
            <h2>루트 생성</h2>
            <div>
               <input
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="루트 제목"
               />
               <button onClick={handleAddRoute}>루트 생성</button>
               <button onClick={handleDeleteRoute}>루트 삭제</button>
            </div>
            <br/>

            <div>
               <h2>루트 조회</h2>
               <input
                  value={routeId}
                  type="number"
                  onChange={(e) => setRouteId(e.target.value)}
                  placeholder="루트 ID"
               />
               <button onClick={handleGetRoute}>루트 조회</button>
               <button onClick={handleDeleteRoute}>루트 삭제</button>
            </div>

            <br/>
            {routeId ?(
               <div>
                  <h3>메모 추가</h3>
                  <input
                     type="text"
                     placeholder="메모 입력"
                     value={memoContent}
                     onChange={(e) => setMemoContent(e.target.value)}
                  />
                  <input
                     type="number"
                     placeholder="일차"
                     value={day}
                     onChange={(e) => setDay(e.target.value)}
                  />
                  <input
                     type="number"
                     placeholder="순서"
                     value={order}
                     onChange={(e) => setOrder(e.target.value)}
                  />

                  <button onClick={() => handleAddMemo()}>메모 추가</button>
               </div>):<p></p>}
            <br/>
            <div>
               {routeId ? (<div>
                  <h3>장소 추가</h3>
                  <button onClick={searchPlace}>장소 추가</button>
                  </div>
               ) : <p></p>}
            </div>
            <div>
               {Object.keys(blocksByDay)
                  .sort((a, b) => a - b)
                  .map(day => (
                     <div key={day}>
                        <h2>{day}일차</h2>
                        {blocksByDay[day].map(block => (
                           <div key={block.id} style={{border: '1px solid #ccc', margin: '10px', padding: '10px'}}>
                              {block.dtype === 'PLACE' && (
                                 <>
                                    <h6>ID: {block.id}, Order: {block.order}</h6>
                                    <h3>{block.title}</h3>
                                    <span>{block.address}</span><br/>
                                    <button onClick={() => handleDeletePlace(block.placeId)}>삭제</button>
                                    {block.memos?.map(memo => (
                                       <div key={memo.id}>
                                          <span>{memo.content}</span>
                                          <button onClick={() => {
                                             setEditMemoId(memo.id);
                                             setEditContent(memo.content);
                                          }}>수정
                                          </button>
                                          <button onClick={() => handleDeleteMemo(memo.id)}>삭제</button>
                                       </div>
                                    ))}
                                 </>
                              )}
                              {block.dtype === 'MEMO' && (
                                 <>
                                    <h6>ID: {block.id}, Order: {block.order}</h6>
                                    <span>{block.title}</span>
                                    <button onClick={() => {
                                       setEditMemoId(block.memoId);
                                       setEditContent(block.title);
                                    }}>수정
                                    </button>
                                    <button onClick={() => handleDeleteMemo(block.memoId)}>삭제</button>
                                 </>
                              )}

                           </div>
                        ))}
                     </div>
                  ))
               }

            </div>

            {
               editMemoId && (
                  <div>
                     <h3>메모 수정</h3>
                     <input value={editContent} onChange={(e) => setEditContent(e.target.value)}/>
                     <button onClick={handleEditMemo}>완료</button>
                     <button
                        onClick={() => {
                           setEditMemoId(null);
                           setEditContent('');
                        }}
                     >
                        취소
                     </button>
                  </div>
               )
            }
            {routeId ? (
            <div style={{ marginTop: '1rem' }}>
               <h5>Block ID</h5>
               <input
                  type="text"
                  placeholder="Block ID"
                  value={blockId}
                  onChange={(e) => setBlockId(e.target.value)}
                  style={{ marginRight: '0.5rem' }}
               />
               <h5>Day</h5>
               <input
                  type="number"
                  placeholder="일차"
                  value={newDay}
                  onChange={(e) => setNewDay(e.target.value)}
                  style={{ marginRight: '0.5rem' }}
               />
               <h5>Order</h5>
               <input
                  type="number"
                  placeholder="순서"
                  value={newOrder}
                  onChange={(e) => setNewOrder(e.target.value)}
                  style={{ marginRight: '0.5rem' }}
               />
               <button disabled={!connected} onClick={() => handleMoveBlock(blockId, newDay, newOrder)}>블록 이동</button>
            </div>
               ):<br/>}

         </div>
      )
         ;
   }
;

export default RouteManagePage;
