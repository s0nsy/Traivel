import React, { useState } from "react";
import Header from "../components/Header";
import Footer2 from "../components/Onboard/Footer2";
import Footer3 from "../components/Onboard/Footer3";
import Icon from "../assets/Icon.svg";
import styled, { keyframes } from "styled-components";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { clickButton } from "../store/buttonSlice";

const PreferenceContainer = styled.div`
  position: relative;
  top: 125px;
`;

const Guide1 = styled.h3`
  color: var(--White, #fff);
  text-align: center;
  font-family: Pretendard;
  font-size: 36px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
  position: relative;
  padding: 0;
`;

const Guide2 = styled.p`
  margin: 10px 0 40px 0;
  color: var(--White, #fff);
  text-align: center;
  position: relative;
  padding: 0;
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 300;
  line-height: normal;
`;

const Suggest = styled.div`
  display: flex;
  width: 320px;
  align-items: center;
  flex-direction: column;
`;

const IconImg = styled.img`
  width: 36px;
  height: 36px;
  margin-top: 2px;
  flex-shrink: 0;
  filter: drop-shadow(0px 4px 4px rgba(1, 236, 254, 0.2));
`;

const Button = styled.button`
  border-radius: 20px;
  border: 1px solid var(--Main_2, #01ecff);
  background: ${({ isClicked }) =>
    isClicked ? "rgba(0,0,0,0.5)" : "rgba(0,0,0,0.1)"};
  display: flex;
  width: 300px;
  height: 75px;
  margin: 6px 0;
  justify-content: flex-start; /* 버튼 내 텍스트를 왼쪽으로 정렬 */
  align-items: center;
  padding-left: 20px; /* 텍스트와 버튼의 왼쪽 가장자리 간의 간격 조정 */
  color: var(--White, #fff);
  text-align: left; /* 텍스트를 왼쪽 정렬 */
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  cursor: pointer;
  margin-right: 24px;

  &:hover {
    background: rgba(0, 0, 0, 0.5);
  }
`;


const Cover = styled.div`
  width: 790px;
  height: 368px;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0px 4px 16px 4px rgba(1, 236, 254, 0.2);
`;

const Lump = styled.div`
  width: 790px;
  height: 368px;
  display: flex;
  position: relative;
  margin-left: 50px;
  margin-right: 24px;
`;

const Lump2 = styled.div`
  display: flex;
  position: relative;
  top: 106px;
  margin-left: 50px;
`;

const P = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  width: 412px;
  margin-left: 20px;
  padding-right: 20px;
`;

const Background = styled.div`
  height: 100vh;
`;

const fadeIn = keyframes`
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
`;

const Question = styled.div`
  display: inline-flex;
  padding: 14px 24px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 16px 4px rgba(1, 236, 254, 0.2);
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  opacity: 0;
  animation: ${fadeIn} 1s ease-in forwards;
  animation-delay: 0.3s;
  position: absolute;
  top: 515px; /* Lump의 bottom에서 여유를 둠 */
  right: calc(50% - 580px); 
`;


const Answer = styled.div`
  width: 900px;
  white-space: normal;
  display: block;
  padding: 16px 20px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0px 4px 16px 4px rgba(1, 236, 254, 0.2);
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  opacity: 0;
  animation: ${fadeIn} 1s ease-in forwards;
  animation-delay: 1s;
`;

const Answer1 = styled.div`
  margin-top: 10px;
  white-space: nowrap;
`;

const Mail = styled.div`
  text-decoration: underline;
`;

const Guide3 = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 773px;
  left: 50%;
  transform: translateX(-50%);
`;


function Preference() {
  const [sentense] = useState([
    "시작하기",
    "좋은 답변",
    "여행지 추천 범위",
    "제안 및 피드백",
  ]);

  const navigate = useNavigate();
  const dispatch = useDispatch();
  const clickedButtons = useSelector((state) => state.button.clickedButtons);

  const handleNavigate = (i) => {
    const routes = ["/theme", "/location", "/cost", "/preference"];
    const buttonName = sentense[i].toLowerCase().replace(/\s+/g, "");

    navigate(routes[i] || "/");
    dispatch(clickButton(buttonName));
  };

  const allButtonsClicked = sentense.every(
    (text) => clickedButtons[text.toLowerCase().replace(/\s+/g, "")]
  );

  return (
    <Background>
      
      <PreferenceContainer>
        <Guide1>루트포터 가이드</Guide1>
        <Guide2>궁금한 점이 있다면 루트포터에게 물어보세요.</Guide2>
        <Lump>
          <IconImg src={Icon} alt="icon" />
          <Cover>
            <P>
              여행 경비는 여행 기간과 큰 영향을 받습니다. 이것을 잘 생각해서
              루트포터한테 물어보시면 됩니다.
            </P>
            <Suggest>
              {sentense.map((text, i) => (
                <Button
                  key={i}
                  onClick={() => handleNavigate(i)}
                  isClicked={
                    clickedButtons[text.toLowerCase().replace(/\s+/g, "")]
                  }
                >
                  {text}
                </Button>
              ))}
            </Suggest>
          </Cover>
        </Lump>
        <Question>루트포터에게 제안 또는 피드백을 하고 싶습니다!</Question>
        <Lump2>
          <IconImg src={Icon} alt="icon" />
          <Answer>
            루트포터는 여러분의 다양한 제안과 피드백을 기다리고 있습니다.
            <Answer1>
              제안 또는 피드백은 아래 이메일 주소로 마음껏 보내주시면
              감사하겠습니다!
            </Answer1>
            <Mail>daval.master.offical@gmail.com</Mail>
          </Answer>
        </Lump2>
        <Guide3>
          필요한 상세조건을 입력하여 더 구체적인 여행 계획을 세울 수 있습니다.
        </Guide3>
      </PreferenceContainer>
      <Footer3
        onClick={() => navigate("/chat")}
      />
    </Background>
  );
}

export default Preference;
