import React, { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import styled from 'styled-components';
import moment from "moment";
import bar from '../assets/bar.png';
const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    background: #014C4E;
    border-radius: 24px;
    overflow: hidden;
    padding-bottom:20px;
    width: 360px;
    height: 320px;
`;
const ExtraContent = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px; /* 달력 아래 여백 */
    text-align: center;
`;

const Image = styled.img`
    margin-bottom: 10px; /* 이미지와 글자 사이 여백 */
`;

const Text = styled.p`
    color: white;
    font-size: 16px;
    font-family: Pretendard;
`;
const StyledCalendar = styled(Calendar)`
    border-radius: 24px; /* 달력의 모서리를 둥글게 */
    overflow: hidden; /* 둥근 모서리가 잘 적용되도록 */
    border: none; /* 테두리 제거 */
    box-shadow: none; /* 그림자 제거 */
    background: #014C4E;
    padding: 25px; /* 내부 여백 추가 */
    padding-bottom:50px;
   

    .react-calendar__tile {
        background: #014C4E; /* 배경색 변경 */
        color: white; /* 날짜 글씨 색상 변경 */
    }
    .react-calendar__navigation button {
        color: white; /* 내비게이션 버튼 글씨 색상 변경 */
        border: none; /* 내비게이션 버튼 테두리 제거 */
        background: none; /* 내비게이션 버튼 배경 제거 */
    }
    .react-calendar__tile--active {
        color:#FFFFFF;
        background-color:#2A2A2A;
        border-radius: 200px;
    }
    .react-calendar__tile:enabled:hover,
    .react-calendar__tile:enabled:focus {
       color:#FFFFFF;
        background-color:#2A2A2A;
        border-radius: 200px;
  }

   .react-calendar__tile--rangeStart,
  .react-calendar__tile--rangeEnd {
    color:#FFFFFF;
        background-color:#2A2A2A;
        border-radius: 200px;
  }
        .react-calendar__month-view__weekdays {
        color:white;
    }
    
   

`;


const CalendarSite = () => {
    const [value, onChange] = useState(new Date());
    return (
        <Container>
            <StyledCalendar onChange={onChange} value={value} formatDay={(locale, date)=> moment(date).format("DD")} />
                <ExtraContent>
                    <Image src={bar}/>
                    <Text>08.22(목) - 08.24(토) 2박 3일</Text>
                </ExtraContent>
        </Container>
        
    );
}

export default CalendarSite;
