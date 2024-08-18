import React, { useState, useEffect, useRef } from 'react';
import styled from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';
import flag from '../assets/flag.png';
import sendIcon from '../assets/send-icon.png';
import { addUserResponse } from '../store/chatSlice';
import recommend from '../assets/리스트 추천.png';
import { useNavigate } from 'react-router';

const TitleCon = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-top: 120px;
`;

const Title = styled.p`
  font-family: Pretendard, sans-serif;
  color: #FFFFFF;
  font-size: 30px;
  font-weight: 500;
`;

const Detail = styled.p`
  font-family: Pretendard, sans-serif;
  color: #FFFFFF;
  font-size: 20px;
  font-weight: 300;
`;

const ChatContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-top: 20px;
  padding-left: 300px;
  padding-right: 300px;
  height: 70vh; /* 최대 높이 설정 */
  overflow-y: auto; /* 스크롤바 추가 */
  /* Custom scrollbar */
  ::-webkit-scrollbar {
    width: 8px;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgba(1, 236, 255, 0.8);
    border-radius: 10px;
  }

  ::-webkit-scrollbar-track {
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
  }

  /* Firefox */
  scrollbar-width: thin;
  scrollbar-color: rgba(1, 236, 255, 0.8) rgba(255, 255, 255, 0.1);
`;

const ChatCon = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: flex-start;
  margin-bottom: 20px;
  margin-top: 20px;
`;

const ChatImg = styled.img`
  width: 36px;
  height: 36px;
`;

const ChatBox = styled.div`
  max-width: 600px; /* 최대 너비 제한 */
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ECFF);
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0px 4px 16px 4px #01ECFE33;
  color: #FFFFFF;
  display: flex;
  align-items: center;
  padding: 12px 16px; /* 내부 여백을 조정 */
  word-wrap: break-word; /* 긴 텍스트를 줄바꿈 */
`;

const UserChatCon = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin-bottom: 20px;
  margin-top: 20px;
  width: 100%;
`;

const UserChatBox = styled.div`
  max-width: 600px; /* 최대 너비 제한 */
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ECFF);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 16px 4px #01ECFE33;
  color: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px 30px; /* 내부 여백을 조정 */
  word-wrap: break-word; /* 긴 텍스트를 줄바꿈 */
`;

const ChatInputContainer = styled.div`
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 1160px;
  height: 60px;
  padding: 16px 27px;
  border-radius: 36px;
  border-top: 1px solid #01ECFF;
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: border-box;
`;

const ChatInputField = styled.input`
  flex: 1;
  background: transparent;
  border: none;
  color: #FFFFFF;
  font-size: 16px;
  outline: none;
`;

const SendIcon = styled.img`
  width: 36px;
  height: 36px;
  cursor: pointer;
`;
const Recommend = styled.img`
  position: fixed; /* 화면에 고정 */
  bottom: 20px; /* 화면의 하단에서 20px 위에 위치 */
  left: 50%; /* 수평 가운데 */
  transform: translateX(-50%); /* 정확히 가운데로 이동 */
  width: 1160px;
  height: 80px;
  cursor: pointer; /* 클릭 가능하도록 설정 */
`;

const Chat = () => {
  const [currentInput, setCurrentInput] = useState('');
  const [step, setStep] = useState(0);
  const dispatch = useDispatch();
  const navigate =useNavigate();
  const userResponses = useSelector((state) => state.chat.userResponses);
  const [finish, setFinish] =useState(false);
  const questions = [
    '이번 여행의 주된 목적은 무엇인가요? (예: 휴식, 탐험, 문화 체험, 미식 여행 등)',
    '여행 예산은 어느 정도인가요? (예: 100만원, 80~120만원)',
    '여행 중 가장 중요하게 생각하는 요소는 무엇인가요? (예: 음식, 역사, 자연 경관, 쇼핑, 액티비티)',
    '어떤 숙박 시설을 선호하나요? (예: 호텔, 게스트하우스, 에어비앤비, 리조트)',
    '여행 중에 어떤 이동 수단을 선호하나요? (예: 자동차, 대중교통, 도보 등)',
    '누구와 함께 여행하나요? (예: 혼자, 가족, 친구, 연인 등)',
    '이미 다녀온 여행지 중에 특히 마음에 들었던 곳이 있나요? (예: 일본, 홍콩 등)',
    '해당 여행지가 더 마음에 들었던 이유는 무엇인가요? (예: 친절한 사람들, 좋은 치안 등)',
    '여행 중에 특별히 필요하거나 피하고 싶은 요소가 있나요? (예: 채식주의, 장애인 접근성, 반려동물 동반 등)',
    'AI가 여행지를 추천할 때 어떤 방식으로 추천 받기를 원하나요? (예: 순위, 테마별 추천, 무작위 추천 등)',
    '여행 중 어느 정도의 자유 시간을 원하시나요? (예: 모든 일정이 꽉 찬 것, 적당한 자유 시간, 대부분의 시간을 자유롭게 보내기 등)',
    '위 질문에 대한 답변 중 가장 중요시 생각하는 것들을 단어 형태로 입력해주세요.'
  ];

  const [questionResponses, setQuestionResponses] = useState([]);
  const chatContainerRef = useRef(null);

  useEffect(() => {
    // 메시지가 추가될 때마다 가장 아래로 스크롤
    if (chatContainerRef.current) {
      chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
    }
  }, [questionResponses]);

  const handleSend = () => {
    if (currentInput.trim() !== '') {
      setQuestionResponses(prev => [
        ...prev,
        { question: questions[step], response: currentInput }
      ]);
      dispatch(addUserResponse(currentInput));
      setCurrentInput(''); 
      if (step < questions.length - 1) {
        setStep(prev => prev + 1);
        console.log(step)
      } else {
        setFinish(true);
        handleSubmit();
      }
    }
  };

  const handleInputChange = (e) => {
    setCurrentInput(e.target.value);
  };

  const handleSubmit = () => {
    console.log('User responses:', userResponses);
    // 서버로 데이터를 전송하는 로직 추가
  };
  const handleList=()=>{
    navigate('/lists')
  }
  return (
    <>
      <TitleCon>
        <Title>Route Porter</Title>
        <Detail>필요한 상세조건을 입력하여 더 구체적인 여행 계획을 세울 수 있습니다.</Detail>
      </TitleCon>

      <ChatContainer ref={chatContainerRef}>
        {questionResponses.map((qr, index) => (
          <React.Fragment key={index}>
            {/* 질문은 왼쪽 */}
            <ChatCon>
              <ChatImg src={flag} alt="flag" />
              <ChatBox>{qr.question}</ChatBox>
            </ChatCon>
            {/* 응답은 오른쪽 */}
            <UserChatCon>
              <UserChatBox>{qr.response}</UserChatBox>
            </UserChatCon>
          </React.Fragment>
        ))}

        {!finish &&step < questions.length && (
          <ChatCon>
            <ChatImg src={flag} alt="flag" />
            <ChatBox>{questions[step]}</ChatBox>
          </ChatCon>
        )}
      </ChatContainer>

      {finish ? (
        <Recommend src={recommend} onClick={handleList} />
      ) : (
        <ChatInputContainer>
          <ChatInputField
            placeholder="메시지를 입력하세요..."
            value={currentInput}
            onChange={handleInputChange}
            onKeyPress={(e) => e.key === 'Enter' && handleSend()}
          />
          <SendIcon src={sendIcon} alt="send" onClick={handleSend} />
        </ChatInputContainer>
      )}
      
    </>
  );
};

export default Chat;

