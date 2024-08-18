import React, { useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom';
import background from '../assets/background.png';
import flag from '../assets/flag.png';
import selectBox from '../assets/선택전.png';
import startBtn from '../assets/Start.png';
import CalendarSite from './Calendar';
import selectedImg from '../assets/selected.png';
import Rectangle from '../assets/sidebar.png';
import moment from 'moment';

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
  left: 50%;
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
  left: 40%;
  transform: translate(-50%, -50%);
  width: 230px;
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
  const [showCalendar, setShowCalendar] = useState(false);
  const [selectedText, setSelectedText] = useState(null);
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [people, setPeople] = useState(1); // Default value for people
  const navigate = useNavigate();

  const handleInlineTextClick = (text) => {
    setSelectedText(text);
  };

  const handleDateChange = (date) => {
    if (selectedText === 'startDate') {
      setStartDate(date);
    } else if (selectedText === 'endDate') {
      setEndDate(date);
    }
  };

  const handleStartClick = () => {
    navigate('/main');
  };

  return (
    <Background>
      <FirstCon>
        <FlagImg src={flag} />
        <Title>Route Porter</Title>
        <Description>일정을 추가한 뒤 루트포터와 대화를 시작해보세요.</Description>
        <SelectCon>
          <SelectBox src={selectBox} onClick={() => handleInlineTextClick(null)} />
          <SelectDate>
            <InlineTextWrapper onClick={() => handleInlineTextClick('startDate')}>
              {selectedText === 'startDate' && <SelectedImg src={selectedImg} />}
              <InlineText>출발일 {startDate ? moment(startDate).format('MM.DD') : "MM.DD"}</InlineText>
            </InlineTextWrapper>
            <ArrowIcon src={Rectangle} />
            <InlineTextWrapper onClick={() => handleInlineTextClick('endDate')}>
              {selectedText === 'endDate' && <SelectedImg src={selectedImg} />}
              <InlineText>도착일 {endDate ? moment(endDate).format('MM.DD') : "MM.DD"}</InlineText>
            </InlineTextWrapper>
            <ArrowIcon src={Rectangle} />
            <InlineTextWrapper onClick={() => handleInlineTextClick('people')}>
              {selectedText === 'people' && <SelectedImg src={selectedImg} />}
              <InlineText>인원 {people}</InlineText>
            </InlineTextWrapper>
          </SelectDate>
        </SelectCon>
        {selectedText === 'startDate' && (
          <CalendarWrapper>
            <CalendarSite onChange={handleDateChange} dateRange={[startDate, endDate]} />
          </CalendarWrapper>
        )}
        <StartBtn src={startBtn} onClick={handleStartClick} />
      </FirstCon>
    </Background>
  );
};

export default Home;
