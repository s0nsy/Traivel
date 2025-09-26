import SockJS from "sockjs-client";
import { over } from "stompjs";



let stompClient = null;
const token = localStorage.getItem("jwtToken");

export const connectWebSocket = (onBlocksUpdate, onConnected) => {

   const socket = new SockJS(`https://traivel.p-e.kr/api/ws`);
   stompClient = over(socket);

   stompClient.connect({ Authorization: `Bearer ${token}` }, () => {
      console.log("✅ WebSocket 연결 성공");
      if (onConnected) onConnected();

      // 서버에서 /topic/blocks를 구독 -> 서버에서 convertAndSend()로 push한 데이터 수신
      stompClient.subscribe("/topic/blocks", (message) => {

         if (message.body) {
            const blocks = JSON.parse(message.body);
            console.log("📥 blocks 업데이트 받음:", blocks);
            if (onBlocksUpdate) onBlocksUpdate(blocks);
         }
      });
   });
};

export const sendMoveBlock = ({routeId, blockId, newDay, newOrder}) => {
   if (stompClient && stompClient.connected) {
      stompClient.send("/app/move-block",
         {},
         JSON.stringify({
            routeId: Number(routeId),
            blockId: Number(blockId),
            newDay: Number(newDay),
            newOrder: Number(newOrder)
         }));
   } else {
      console.error("❌ WebSocket 연결 안 됨");
   }
};

export const disconnectWebSocket = () => {
   if (stompClient) {
      stompClient.disconnect(() => {
         console.log("🔌 WebSocket 연결 해제");
      });
   }
};