import React from "react";
import { useState } from "react";
import styled from "styled-components";
import Icon from "../assets/Icon.svg";
import Shortline1 from "../assets/Rectangle 42987.png";
import Shortline2 from "../assets/short-line.png";
import Back from "../assets/back.svg";
import Share from "../assets/share.svg";
import DayLists from "./DayLists";
import { useNavigate } from "react-router-dom";
const RouteContainer = styled.div``;
const Frame1 = styled.div`
  margin-left: 350px;
  margin-top: 20px;
  width: 930px;
  height: 290px;
  flex-shrink: 0;
  border-radius: 12px;
  position: relative; /
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  display: flex; /* 플렉스 컨테이너로 설정 */
  align-items: center; /* 수직 정렬을 가운데로 설정 */
`;
const Img1 = styled.img`
  width: 30px;
  height: 30px;
  flex-shrink: 0;
`;
const Img2 = styled.div`
  display: flex;
  justify-content: center; /* 수평 가운데 정렬 */
  align-items: center; /* 수직 가운데 정렬 */
  position: relative;
  left: 15px;
  width: 370px;
  height: 270px;
  flex-shrink: 0;
  border-radius: 12px;
  background: var(--Gray_2, #d6d4d4);
  color: var(--Gray_4, #4a4a4a);
  font-family: Pretendard;
  font-size: 13px;
  font-style: normal;
  font-weight: 500;
  text-align: center;
  line-height: 28px; /* 140% */
`;

const Img3 = styled.img`
  width: 2px;
  height: 16px;
  margin: 0 10px;
  border-radius: 8px;
  background: var(--Gray_3, #acacac);
`;
const Img4 = styled.img`
  width: 25px;
  height: 25px;
  margin: 2px 4px;
`;
const Img5 = styled.img`
  width: 30px;
  height: 30px;
  margin-top: 2px;
  margin-right: 7px;
`;
const Img6 = styled.img`
  width: 2px;
  height: 26px;
  margin: 0 200px;
  background: var(--White_O50, rgba(255, 255, 255, 0.5));
`;
const Img7 = styled.img`
  width: 30px;
  height: 30px;
  margin-top: 2px;
  margin-right: 7px;
`;

const Header = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 23px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
`;

const P1 = styled.div`
  align-self: stretch;
  color: #fff;
  margin-top: 40px;
  font-family: Pretendard;
  font-size: 17px;
  margin-bottom: 150px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
`;
const P2 = styled.div`
  align-self: stretch;
  color: #fff;
  font-family: Pretendard;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
`;
const P3 = styled.div`
  align-self: stretch;
  color: #fff;
  font-family: Pretendard;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
`;
const P4 = styled.div`
  color: #fff;
  margin-top: 20px;
  position: relative;
  bottom: 80px;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
`;

const Guide1 = styled.div`
  position: absolute;
  width: auto;
  height: auto;
  bottom: 200px;
  padding: 4px 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 20px;
  background: var(
    --gradation_2,
    linear-gradient(
      90deg,
      rgba(255, 255, 255, 0.2) 0%,
      rgba(153, 153, 153, 0.2) 100%
    )
  );
`;
const Word = styled.div`
  display: inline-block;
  font-size: 10px;
  margin-left: 30px;
`;
const Guide2 = styled.div`
  position: relative;
  bottom: 70px;
  display: flex;
  flex-wrap: wrap; /* 자식 요소들이 넘칠 때 줄 바꿈 */
  justify-content: flex-start; /* 왼쪽 정렬 */
  align-items: center;
`;

const A = styled.div`
  margin-top: 90px;
  display: flex;
  justify-content: left;
  margin-left: 350px;
  align-items: center;
`;

const B = styled.div`
  display: flex;
  justify-content: left;
  margin-left: 350px;
  margin-top: 20px;
`;
const Guide3 = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: 28px; /* 116.667% */
`;
const Guide4 = styled.div`
color: #FFF;
font-family: Pretendard;
font-size: 18px;
font-style: normal;
font-weight: 600;
line-height: 28px; /* 1
`;
const Guide5 = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: 28px;
`;

const C = styled.div`
  display: inline-flex;
  margin-left: 350px;
  margin-top: 20px;
  padding: 16px 150px;
  justify-content: center;
  align-items: center;
  border-radius: 36px;
  border: 1px solid var(--Main_2, #01ecff);
  background: var(--Calendar, #014c4e);
  /* 프롬프트 효과 */
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
`;

const ChatOrShare1 = styled.div`
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 36px; /* 112.5% */
`;
const ChatOrShare2 = styled.div`
  color: #fff;

  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 36px; /* 112.5% */
`;
const Frame2 = styled.div``;

const D = styled.div`
  display: flex;
  width: 1176px;
  flex-direction: column;
  align-items: flex-start;
  gap: 80px;
`;

const KeywordItem = styled.div`
  margin: 5px;
  padding: 8px 25px;
  background-color: rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  color: var(--White, #fff);
  font-family: Pretendard;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 140% */
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 36px;
  background: var(
    --gradation_2,
    linear-gradient(
      90deg,
      rgba(255, 255, 255, 0.2) 0%,
      rgba(153, 153, 153, 0.2) 100%
    )
  );
  box-shadow: 0px 4px 8px 4px rgba(0, 0, 0, 0.1);
`;

function Output() {
  const [keyword] = useState([
    "서귀포",
    "하루 2끼 식사",
    "베이커리 카페",
    "액티비티",
    "힐링 컨셉",
    "날 것 못먹음",
  ]);

  const navigate = useNavigate();

  const Chattting = () => {
    navigate("/chat");
  };

  const Sharing = () => {
    navigate("/share");
  };

  return (
    <RouteContainer>
      <A>
        <Img1 src={Icon} alt="Icon"></Img1>
        <Header>제주 ‘서귀포’ 지역의 2박 3일 일정을 추천드립니다!</Header>
      </A>
      <Frame1>
        <Img2>관광정보 사이트 이미지</Img2>
        <Word>
          <P1>여행 일정 및 인원</P1>
          <Guide1>
            <P2>날짜 08.22 - 08.24</P2>
            <Img3 src={Shortline1} alt="shortline"></Img3>
            <P3>인원 1</P3>
          </Guide1>
          <P4>여행 키워드</P4>
          <Guide2>
            {keyword.map((a, i) => {
              return <KeywordItem>{keyword[i]}</KeywordItem>;
            })}
          </Guide2>
        </Word>
      </Frame1>
      <B>
        <Guide3>추천 정보가 마음에 드신다면</Guide3>
        <Img4 src={Share} alt="share"></Img4>
        <Guide4>공유하기</Guide4>
        <Guide5>를 눌러 루트를 공유해보세요</Guide5>
      </B>
      <C>
        <Img5 src={Back}></Img5>
        <ChatOrShare1 onClick={Chattting}>채팅으로</ChatOrShare1>
        <Img6 src={Shortline2}></Img6>
        <Img7 src={Share}></Img7>
        <ChatOrShare2 onClick={Sharing}>공유하기</ChatOrShare2>
      </C>
      <D>
        <Frame2>
          <DayLists />
        </Frame2>
      </D>
    </RouteContainer>
  );
}

export default Output;
