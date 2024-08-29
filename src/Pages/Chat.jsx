import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import styled,{keyframes} from 'styled-components';
import { useDispatch, useSelector } from 'react-redux';
import flag from '../assets/flag.png';
import sendIcon from '../assets/send-icon.png';
import send from '../assets/send.png';
import { useNavigate } from 'react-router';
import { 
  setPurpose, setBudget, setkeyElement, setAccommodation, 
  settransport, setCompanion, setfavorite, setfavoriteReason, 
  setspecialNeeds, setRecommendationType, setfreeTime, setimportantFactors 
} from '../store/surveySlice';
import { setRecommendations } from '../store/surveySlice';

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
  font-size: 36px;
  font-weight: 500;
`;

const Detail = styled.p`
  font-family: Pretendard, sans-serif;
  color: #FFFFFF;
  font-size: 29px;
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
  width: 40px;
  height: 40px;
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
  padding: 12px 16px;
  word-wrap: break-word; /* 긴 텍스트를 줄바꿈 */
  font-size:20px;
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
  padding: 12px 16px; /* 내부 여백을 조정 */
  word-wrap: break-word; /* 긴 텍스트를 줄바꿈 */
  font-size:20px;
`;

const ChatInputContainer = styled.div`
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 1160px;
  height: 72px;
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
  font-size: 20px;
  outline: none;
`;

const SendIcon = styled.img`
  width: 40px;
  height: 40px;
  cursor: pointer;
`;
const Recommend = styled.div`
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 1160px;
  height: 72px;
  padding: 16px 27px;
  border-radius: 36px;
  border-top: 1px solid #01ECFF;
  background: #FFFFFF4D;
  box-shadow: 4px 4px 12px 2px #01ECFF66;
  cursor: pointer; 
  display: flex;
  align-items: center;
  justify-content: space-between; /* 공간을 양쪽 끝으로 분배 */
  color: #FFFFFF; /* 텍스트 색상을 흰색으로 설정 */
  font-size: 20px; /* 글씨 크기 설정 */
  box-sizing: border-box;
`;

const RecommendText = styled.div`
  text-align: center;
  flex-grow: 1;
`;

const SendIconStyled = styled.img`
  width: 40px;
  height: 40px;
  cursor: pointer;
  margin-left: auto; /* 아이콘을 오른쪽 끝으로 밀어내기 */
`;
const bounce = keyframes`
  0%, 80%, 100% { 
    transform: scale(0);
  } 
  40% { 
    transform: scale(1.0);
  }
`;

const DotLoader = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: auto; /* 아이콘 자리 맞추기 */

  div {
    width: 10px;
    height: 10px;
    background-color: #ffffff;
    border-radius: 50%;
    display: inline-block;
    animation: ${bounce} 1.4s infinite ease-in-out both;
    
    &:nth-child(1) {
      animation-delay: -0.32s;
    }
    &:nth-child(2) {
      animation-delay: -0.16s;
    }
  }
`;

const Chat = () => {
  const [currentInput, setCurrentInput] = useState('');
  const [step, setStep] = useState(0);
  const [loading, setLoading] = useState(false);
  const [showChatCon, setShowChatCon] = useState(true); // 첫 질문은 바로 표시되도록 true로 설정
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const userResponses = useSelector((state) => state.survey);
  const adults = useSelector((state) => state.survey.adults);
  const children = useSelector((state) => state.survey.children);
  const infants = useSelector((state) => state.survey.infants);
  const [finish, setFinish] = useState(false);
  const chatContainerRef = useRef(null);
  const [questionResponses, setQuestionResponses] = useState([]);
  const PROXY = window.location.hostname === 'localhost' ? '' : '/proxy';

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

  const actionDispatchers = [
    setPurpose, setBudget, setkeyElement, setAccommodation,
    settransport, setCompanion, setfavorite, setfavoriteReason,
    setspecialNeeds, setRecommendationType, setfreeTime, setimportantFactors
  ];

  const schedule = useSelector((state) => state.survey.schedule);

  const handleSend = async () => {
    if (currentInput.trim() !== '') {
      dispatch(actionDispatchers[step](currentInput));
      setQuestionResponses(prev => [...prev, { question: questions[step], response: currentInput }]);

      setCurrentInput('');

      if (step < questions.length - 1) {
        setShowChatCon(false); // ChatCon 숨기기
        setTimeout(() => {
          setStep(prev => prev + 1);
          setShowChatCon(true); // ChatCon을 0.5초 후에 표시
        }, 600);
      } else {
        setLoading(true);
        setFinish(true);
        try {
          await handleSubmit();
        } catch (error) {
          console.error('Error submitting:', error);
        } finally {
          setLoading(false);
        }
      }
    }
  };

  const handleSubmit = async () => {
    try {
      const submissionData = {
        schedule: schedule,
        groupComposition: { adults: adults, children: children, infants: infants },
        purpose: userResponses.purpose,
        budget: userResponses.budget,
        keyElement: userResponses.keyElement,
        accommodation: userResponses.accommodation,
        transport: userResponses.transport,
        companion: userResponses.companion,
        favorite: userResponses.favorite,
        favoriteReason: userResponses.favoriteReason,
        specialNeeds: userResponses.specialNeeds,
        recommendationType: userResponses.recommendationType,
        freeTime: userResponses.freeTime,
        importantFactors: userResponses.importantFactors,
      };

      const response = await axios.post(`${PROXY}/api/chat`, submissionData);

      dispatch(setRecommendations(response.data));
      localStorage.setItem('recommendations', JSON.stringify(response.data));
      console.log(response.data);

    } catch (error) {
      console.error('서버 요청 중 오류 발생:', error);
    }
  };

  const handleInputChange = (e) => {
    setCurrentInput(e.target.value);
  };

  const handleList = () => {
    navigate('/lists');
  };

  useEffect(() => {
    if (chatContainerRef.current) {
      setTimeout(() => {
        chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
      }, 100);
    }
  }, [step, finish, showChatCon]); // showChatCon 상태가 변경될 때마다 스크롤 업데이트

  return (
    <>
      <TitleCon>
        <Title>Route Porter</Title>
        <Detail>필요한 상세조건을 입력하여 더 구체적인 여행 계획을 세울 수 있습니다.</Detail>
      </TitleCon>

      <ChatContainer ref={chatContainerRef}>
        {questionResponses.map((qr, index) => (
          <React.Fragment key={index}>
            <ChatCon>
              <ChatImg src={flag} alt="flag" />
              <ChatBox>{qr.question}</ChatBox>
            </ChatCon>

            <UserChatCon>
              <UserChatBox>{qr.response}</UserChatBox>
            </UserChatCon>
          </React.Fragment>
        ))}

        {!finish && step < questions.length && showChatCon && (
          <ChatCon>
            <ChatImg src={flag} alt="flag" />
            <ChatBox>{questions[step]}</ChatBox>
          </ChatCon>
        )}
      </ChatContainer>

      {loading ? (
        <Recommend>
          <RecommendText>로딩 중입니다... 잠시만 기다려주세요.</RecommendText>
          <DotLoader>
            <div></div>
            <div></div>
            <div></div>
          </DotLoader>
        </Recommend>
      ) : finish ? (
        <Recommend onClick={handleList}>
          <RecommendText>이제 루트포터로 여행지 리스트를 추천해드릴게요.</RecommendText>
          <SendIconStyled src={send} alt="send" onClick={handleList} />
        </Recommend>
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
