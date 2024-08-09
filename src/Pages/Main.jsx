import React, { useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom'; 
import background from '../assets/background.png';
import flag from '../assets/flag.png';
import selectBox from '../assets/선택전.png';
import startBtn from '../assets/Start.png';
import CalendarSite from './Calendar'; // 기존 Calendar 사용
import People from './People';  // People 컴포넌트 import
import selectedImg from '../assets/selected.png'; 
import Rectangle from '../assets/sidebar.png'; 
import moment from "moment";

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
    color: #01ECFF;
    font-size: 60px;
    font-weight: 600;
`;

const Description = styled.p`
    display: flex;
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
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
    color: #FFFFFF;
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
    const [selectedText, setSelectedText] = useState(null); // 클릭된 텍스트의 상태
    const [startDate, setStartDate] = useState(null); // 출발일 상태
    const [endDate, setEndDate] = useState(null); // 도착일 상태
    const [people, setPeople] = useState("추가");
    const navigate = useNavigate(); 

    const handleStartClick = () => {
        navigate('/main'); 
    };

    const handleInlineTextClick = (text) => {
        setSelectedText(selectedText === text ? null : text);
    };

    const handleDateChange = (date) => {
        if (selectedText === 'startDate') {
            setStartDate(date);
        } else if (selectedText === 'endDate') {
            setEndDate(date);
        }
    };

    const formatDateRange = () => {
        if (startDate && endDate) {
            const formattedStartDate = moment(startDate).format('MM.DD');
            const formattedEndDate = moment(endDate).format('MM.DD');
            const days = moment(endDate).diff(moment(startDate), 'days') + 1;
            return `${formattedStartDate} - ${formattedEndDate} ${days}박 ${days + 1}일`;
        } else {
            return "기간 선택";
        }
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
                        <CalendarSite onChange={handleDateChange} />
                    </CalendarWrapper>
                )}
                {selectedText === 'endDate' && (
                    <CalendarWrapper>
                        <CalendarSite onChange={handleDateChange} />
                    </CalendarWrapper>
                )}
                {selectedText === 'people' && (
                    <CalendarWrapper>
                        <People />
                    </CalendarWrapper>
                )}
                <StartBtn src={startBtn} onClick={handleStartClick} />
            </FirstCon>
        </Background>
    );
}

export default Home;
