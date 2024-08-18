import React, { useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom'; 
import background from '../assets/background.png';
import flag from '../assets/flag.png';
import selectBox from '../assets/선택전.png';
import startBtn from '../assets/Start.png';
import CalendarSite from '../components/Main/Calendar';
import People from '../components/Main/People';  
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

const Home = ({ setSelectedDateRange, setSelectedPeople }) => {
    const [selectedText, setSelectedText] = useState(null); 
    const [startDate, setStartDate] = useState(null); 
    const [endDate, setEndDate] = useState(null); 
    const [people, setPeople] = useState("추가");
    const navigate = useNavigate(); 

    const handleStartClick = () => {
        navigate('/onboard'); 
    };

    const handleInlineTextClick = (text) => {
        setSelectedText(selectedText === text ? null : text);
    };

    const handleDateChange = (date) => {
        if (selectedText === 'startDate') {
            setStartDate(date);
            setSelectedText('endDate'); // 출발일 선택 후 도착일 선택으로 자동 전환
        } else if (selectedText === 'endDate') {
            setEndDate(date);
            const dateRange = `${moment(startDate).format('MM.DD')} - ${moment(date).format('MM.DD')} `;
            setSelectedDateRange(dateRange); // 날짜 범위를 상위 컴포넌트로 전달
        }
    };

    const handlePeopleChange = (count) => {
        setPeople(count);
        setSelectedPeople(count);
    };

    const dateRange = startDate && endDate 
        ? `${moment(startDate).format('MM.DD')} - ${moment(endDate).format('MM.DD')} ${moment(endDate).diff(moment(startDate), 'days')}박 ${moment(endDate).diff(moment(startDate), 'days') + 1}일`
        : startDate 
        ? `${moment(startDate).format('MM.DD')} -`
        : "날짜를 선택하세요";

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
                        <CalendarSite onChange={handleDateChange} dateRange={dateRange} />
                    </CalendarWrapper>
                )}
                {selectedText === 'endDate' && (
                    <CalendarWrapper>
                        <CalendarSite onChange={handleDateChange} dateRange={dateRange} />
                    </CalendarWrapper>
                )}
                {selectedText === 'people' && (
                    <CalendarWrapper>
                        <People onPeopleChange={handlePeopleChange} />
                    </CalendarWrapper>
                )}
                <StartBtn src={startBtn} onClick={handleStartClick} />
            </FirstCon>
        </Background>
    );
}

export default Home;
