import React, { useState } from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import background from "../assets/background.png";
import flag from "../assets/flag.png";
import selectBox from "../assets/선택전.png";
import startBtn from "../assets/Start.png";
import CalendarSite from "../components/Main/Calendar";
import People from "../components/Main/People";
import selectedImg from "../assets/selected.png";
import Rectangle from "../assets/sidebar.png";
import moment from "moment";
import Output from "./Output";
import { useDispatch, useSelector } from 'react-redux';
import { setStartDate, setEndDate, setAdults, setChildren, setInfants } from '../store/surveySlice';


const Background = styled.div`
  display: flex;
  width: 100%;
  height: 100vh;
  background-image: url(${background});
  background-size: cover;
  background-position: center center;
  position: relative;
`;

const FirstCon = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  gap: 10px;
`;

const FlagImg = styled.img`
  width: 100px;
  height: 100px;
`;

const Title = styled.p`
  display: flex;
  font-family: Pretendard, sans-serif;
  color: #01ecff;
  font-size: 60px;
  font-weight: 600;
`;

const Description = styled.p`
  display: flex;
  font-family: Pretendard, sans-serif;
  color: #ffffff;
  font-size: 24px;
  font-weight: 300;
`;

const SelectCon = styled.div`
  display: flex;
  position: relative;
  align-items: center;
`;

const SelectBox = styled.img`
  cursor: pointer;
`;

const SelectDate = styled.div`
  position: absolute;
  display: flex;
  align-items: center;
  font-family: Pretendard, sans-serif;
  color: #ffffff;
  font-size: 18px;
  font-weight: 400;
  top: 50%;
  left: 48%;
  transform: translate(-50%, -50%);
`;

const InlineTextWrapper = styled.div`
  position: relative;
  display: inline-flex;
  align-items: center;
`;

const InlineText = styled.span`
  white-space: nowrap;
  cursor: pointer;
  z-index: 2;
`;

const ArrowIcon = styled.img`
  width: 2px;
  height: 24px;
  margin: 0 40px;
`;

const SelectedImg = styled.img`
  position: absolute;
  top: 50%;
  left: ${({ selected }) =>
    selected === "startDate" ? "50%" : selected === "endDate" ? "60%" : "160%"};
  transform: translate(-60%, -50%);
  width: ${({ selected }) =>
    selected === "startDate"
      ? "250px"
      : selected === "endDate"
      ? "230px"
      : "230px"};
  height: 60px;
  z-index: 0;
  pointer-events: none;
`;

const StartBtn = styled.img`
  cursor: pointer;
`;

const CalendarWrapper = styled.div`
  position: absolute;
  top: 58%;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10;
`;


const Home = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  // Redux에서 필요한 상태 불러오기
  const startDate = useSelector((state) => state.survey.startDate);
  const endDate = useSelector((state) => state.survey.endDate);
  const adults = useSelector((state) => state.survey.adults);
  const children = useSelector((state) => state.survey.children);
  const infants = useSelector((state) => state.survey.infants);

  const [selectedText, setSelectedText] = useState(null);

  const handleStartClick = () => {
    
    navigate("/onboard");
    
  };

  const handleInlineTextClick = (text) => {
    setSelectedText(selectedText === text ? null : text);
  };

  const handleDateChange = (date) => {
    if (selectedText === "startDate") {
      dispatch(setStartDate(date));
      setSelectedText("endDate");
    } else if (selectedText === "endDate") {
      dispatch(setEndDate(date));
    }
  };

  const handlePeopleChange = (totalPeople) => {
    const { adultsCount, childrenCount, infantsCount } = totalPeople;
    dispatch(setAdults(adultsCount));
    dispatch(setChildren(childrenCount));
    dispatch(setInfants(infantsCount));
  };

  const dateRange =
    startDate && endDate
      ? `${moment(startDate).format("MM.DD")} - ${moment(endDate).format("MM.DD")} ${moment(endDate).diff(moment(startDate), "days")}박 ${moment(endDate).diff(moment(startDate), "days") + 1}일`
      : startDate
      ? `${moment(startDate).format("MM.DD")} -`
      : "날짜를 선택하세요";

  const peopleText = adults + children + infants > 0 ? `${adults + children + infants}명` : "추가";

  return (
    <Background>
      <FirstCon>

        <FlagImg src={flag} />
        <Title>Route Porter</Title>
        <Description>
          일정을 추가한 뒤 루트포터와 대화를 시작해보세요.
        </Description>
        <SelectCon>
          <SelectBox src={selectBox} onClick={() => handleInlineTextClick(null)} />
          <SelectDate>
            <InlineTextWrapper onClick={() => handleInlineTextClick("startDate")}>
              {selectedText === "startDate" && <SelectedImg src={selectedImg} selected="startDate" />}
              <InlineText>출발일 {startDate ? moment(startDate).format("MM.DD") : "MM.DD"}</InlineText>
            </InlineTextWrapper>
            <ArrowIcon src={Rectangle} />
            <InlineTextWrapper onClick={() => handleInlineTextClick("endDate")}>
              {selectedText === "endDate" && <SelectedImg src={selectedImg} selected="endDate" />}
              <InlineText>도착일 {endDate ? moment(endDate).format("MM.DD") : "MM.DD"}</InlineText>
            </InlineTextWrapper>
            <ArrowIcon src={Rectangle} />
            <InlineTextWrapper onClick={() => handleInlineTextClick("people")}>
              {selectedText === "people" && <SelectedImg src={selectedImg} selected="people" />}
              <InlineText>인원 {peopleText}</InlineText>
            </InlineTextWrapper>
          </SelectDate>
        </SelectCon>

        {/* 선택된 날짜와 인원에 따라 컴포넌트 렌더링 */}
        {selectedText === "startDate" && (
          <CalendarWrapper>
            <CalendarSite onChange={handleDateChange} dateRange={dateRange} />
          </CalendarWrapper>
        )}
        {selectedText === "endDate" && (
          <CalendarWrapper>
            <CalendarSite onChange={handleDateChange} dateRange={dateRange} />
          </CalendarWrapper>
        )}
        {selectedText === "people" && (
          <CalendarWrapper>
            <People onPeopleChange={handlePeopleChange} />
          </CalendarWrapper>
        )}
        
        <StartBtn src={startBtn} onClick={handleStartClick} />

      </FirstCon>
    </Background>
  );
};

export default Home;