import React from "react";
import Icon from "../assets/Icon.svg";
import Vector from "../assets/Vector.svg";
import Footer from "../components/Onboard/Footer";
import styled from "styled-components";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { clickButton } from '../store/buttonSlice'; 

const Background = styled.div`
  height: 100vh;
  position: relative;
  overflow: hidden;
`;

const Body = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: relative;
  top: 200px;
  z-index: 1;
`;

const IconImg = styled.img`
  width: 85px;
  height: 80px;
  margin-top: 25px;
  filter: drop-shadow(0px 4px 4px rgba(1, 236, 254, 0.2));
  top:268px;
`;

const VectorImg = styled.img`
  position: absolute;
  top:-65px;
  left: 48%; 
  transform: translateX(-50%); 
  z-index: 0;
  width: 813px;
  height: 555px;
`;

const Recommend = styled.p`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 36px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
  z-index: 1;
  margin-top:10px;
`;

const Suggest = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 25px;
  z-index: 1;
`;

const Button = styled.button`
  margin: 20px 0;
  padding: 20px;
  border-radius: 20px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(0, 0, 0, 0.1);
  display: flex;
  width: 160px;
  height: auto; 
  min-height: 128px; 
  padding: 16px; 
  justify-content: center;
  align-items: center;
  text-align: center; 
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: 1.2; 
  margin-top:40px;
  cursor: pointer;
  white-space: normal;
  word-wrap: break-word; 
  &:hover {
    background: rgba(0, 0, 0, 0.5);
  }
  z-index: 1;
`;

const Guide = styled.p`
  color: var(--White, #fff);
  margin-top: 29px;
  text-align: center;
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  z-index: 1;
`;

function Onboard() {
  const [sentense] = useState([
    "시작하기",
    "좋은 답변",
    "여행지 추천 범위",
    "제안 <br />및 피드백", 
  ]);

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const handleNavigate = (i) => {
    const paths = ["/theme", "/location", "/cost", "/preference"];
    const buttonName = sentense[i].toLowerCase().replace(/\s+/g, "");

    dispatch(clickButton(buttonName)); 
    navigate(paths[i]);
  };

  return (
    <Background>
      <Body>
        <IconImg src={Icon} alt="icon" />
        <VectorImg src={Vector} alt="vector" />
        <Recommend>대화로 추천받는 내 여행지</Recommend>
        <Suggest>
          {sentense.map((a, i) => (
            <Button key={i} onClick={() => handleNavigate(i)}>
              <span dangerouslySetInnerHTML={{ __html: a }} /> {/* HTML로 줄바꿈 처리 */}
            </Button>
          ))}
        </Suggest>
        <Guide>멋진 일정을 계획하기 위해 루트포터 가이드를 이용해보세요.</Guide>
      </Body>
      <Footer />
    </Background>
  );
}

export default Onboard;
