import React from "react";
import Header from "./Header";
import Footer2 from "./Footer2";
import Icon from "../assets/Icon.svg";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

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
  top: 30px;
`;

const Guide2 = styled.p`
  color: var(--White, #fff);
  text-align: center;
  position:relative;
  padding: 0;
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 300;
  p
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
  background: ${({isLocation})=>(isLocation?'rgba(0,0,0,0.5)':'rgba(0,0,0,0.1)')};
  display: flex;
  width: 280px;
  height: 55px;
  margin: 6px 0;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
  color: var(--White, #fff);
  text-align: left;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  cursor: pointer;
  text-indent: -120px;
  &:hover{
    background: rgba(0, 0, 0, 0.5);
  
`;

const Cover = styled.div`
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  box-shadow: 0px 4px 16px 4px rgba(1, 236, 254, 0.2);
`;

const Lump = styled.div`
  width: 620px;
  height: 290px;
  display: flex;
  position: relative;
  margin-left: 400px;
`;
const Lump2 = styled.div`
  display: flex;
  position: relative;
  top: 100px;
  margin-left: 400px;
`;

const P = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 15px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  width: 310px;
  margin-left: 20px;
  padding-right: 20px;
`;

const Background = styled.div``;

const Question = styled.div`
  display: inline-flex;
  padding: 14px 24px;
  position: relative;
  top: 40px;
  margin-left: 900px;
  justify-content: center;
  align-items: center;
  gap: 10px;
  border-radius: 24px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 16px 4px rgba(1, 236, 254, 0.2);
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 15px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
`;
const Answer = styled.div`
  width: 345px;
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
  font-size: 15px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
`;
const Guide3 = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 190px;
`;

function Location() {
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
        navigate("/location");
        break;
      case 2:
        navigate("/cost");
        break;
      case 3:
        navigate("/preference");
        break;
      default:
        navigate("/");
    }
  };

  return (
      <Background>
        <Header />
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
              {sentense.map((a, i) => {
                return (
                  <Button onClick={() => handleNavigate(i)}
                   isLocation={i===1}
                  >
                    {sentense[i]}
                  </Button>
                );
              })}
            </Suggest>
          </Cover>
        </Lump>
        <Question>
          여행 경비에 따라서 추천하는 휴양지가 많이 달라지나요?
        </Question>
        <Lump2>
          <IconImg src={Icon} alt="icon" />
          <Answer>
            여행 경비는 여행 기간과 큰 영향을 받습니다. 이것을 잘 생각해서
            루트포터한테 물어보시면 됩니다.
          </Answer>
        </Lump2>
        <Guide3>
          필요한 상세조건을 입력하여 더 구체적인 여행 계획을 세울 수 있습니다.
        </Guide3>
        <Footer2 />
      </Background>
  );
}

export default Location;
