import React from "react";
import Icon from "../assets/Icon.svg";
import Vector from "../assets/Vector.svg";
import styled from "styled-components";
import { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import chattingbackground from "../assets/chattingbackground.png";

const Body = styled.div`
  margin-top: 190px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 40vh;
  flex-direction: column;
  position: relative;
`;

const IconImg = styled.img`
  width: 85px;
  height: 80px;
  margin-top: 25px;
  filter: drop-shadow(0px 4px 4px rgba(1, 236, 254, 0.2));
  z-index: 1;
`;

const VectorImg = styled.img`
  position: absolute;
  z-index: -1;
  width: 700px;
  height: 500px;
  margin-right: 45px;
  margin-top: 50px;
  display: block;
`;

const Recommend = styled.p`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 25px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
`;

const Suggest = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 25px;
`;

const Button = styled.button`
  padding: 20px;
  border-radius: 20px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(0, 0, 0, 0.1);
  display: flex;
  width: 125px;
  height: 100px;
  padding: 0px 22px;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
  color: var(--White, #fff);
  text-align: center;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  cursor: pointer;
  &:hover {
    background: rgba(0, 0, 0, 0.5);
  }
`;

const Guide = styled.p`
  color: var(--White, #fff);
  text-align: center;
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
`;

const Background = styled.div`
  width: 100%;
  height: 100%;
  z-index:+2;
`;

function Main() {
  const [sentense] = useState([
    "여행 테마",
    "선호하는 지역",
    "이동 및 경비",
    "여행 취향",
  ]);
  const navigate = useNavigate();
  const handleNavigate = (i) => {
    switch (i) {
      case 0:
        navigate("/theme");
        break;
      case 1:
        navigate("location");
        break;
      case 2:
        navigate("cost");
        break;
      case 3:
        navigate("preference");
        break;
      default:
        navigate("/");
        break;
    }
  };
  return (
    <Background>
      <Body>
        <IconImg src={Icon} alt="icon" />
        <VectorImg src={Vector} alt="vector" />
        <Recommend>대화로 추천받는 내 여행지</Recommend>
        <Suggest>
          {sentense.map((a, i) => {
            return (
              <Button onClick={() => handleNavigate(i)}>{sentense[i]}</Button>
            );
          })}
        </Suggest>
        <Guide>멋진 일정을 계획하기 위해 루트포터 가이드를 이용해보세요.</Guide>
      </Body>
    </Background>
  );
}

export default Main;
