import React, { useState } from 'react';
import styled from 'styled-components';
import background from '../assets/background.png';
import flag from '../assets/flag.png';
import selectBox from '../assets/선택전.png';
import startBtn from '../assets/Start.png';
import CalendarSite from './Calendar';
import selected from './assets/selected.png';

const Background = styled.div`
    display: flex;
    width: 100%;
    height: 100vh;
    background-image: url(${background});
    background-size: cover; /* 이미지를 뷰포트에 맞게 채우기 */
    background-position: center center; /* 가운데 정렬 */
    position: relative; /* 절대 위치 요소를 포함하기 위해 */
`;

const FirstCon = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center; /* 가로 방향 중앙 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    width: 100%;
    height: 100%; /* 부모 요소의 전체 높이 차지 */
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

const SelectBox = styled.img`
    cursor: pointer; /* 클릭 가능한 커서 */
`;

const StartBtn = styled.img`
    cursor: pointer; /* 클릭 가능한 커서 */
`;

const CalendarWrapper = styled.div`
    position: absolute;
    top: 58%;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 10; /* 다른 요소들 위에 표시되도록 */
`;

const SelectCon =styled.div`
    display:flex;
    justify-content: center;
    `

const Main = () => {
    const [showCalendar, setShowCalendar] = useState(false);
    const [startDate, setStartDate] =useState("MM.DD");
    const [EndDate, setEndDate] =useState("MM.DD");
    const [People , setPeople] =useState("추가");

    return (
        <Background>
            <FirstCon>
                <FlagImg src={flag} />
                <Title>Route Porter</Title>
                <Description>일정을 추가한 뒤 루트포터와 대화를 시작해보세요.</Description>
                <SelectBox src={selectBox} onClick={() => setShowCalendar(!showCalendar)} />
                <SelectCon>

                </SelectCon>
                {showCalendar && (
                    <CalendarWrapper>
                        <CalendarSite />
                    </CalendarWrapper>
                )}
                <StartBtn src={startBtn} />
            </FirstCon>
        </Background>
    );
}

export default Main;
