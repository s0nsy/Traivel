import { useEffect } from "react";
import {
   connectWebSocket,
   disconnectWebSocket,
} from "../services/websocketService";

const Websocket = ({ routeId, onBlocksUpdate, setConnected}) => {
   // 웹소켓 연결
   useEffect(() => {
      if (routeId) {
         connectWebSocket(
            (updatedBlocks) => {
            // 부모(RouteManagePage)에게 blocks 전달
            onBlocksUpdate(updatedBlocks);
         },
            ()=>{
            setConnected(true);
            });
      }
      return () => {
         disconnectWebSocket();
         setConnected(false);
      };
   }, [routeId]);

   return null; // 렌더링 없음, 단지 웹소켓 역할만 함
};

export default Websocket;
