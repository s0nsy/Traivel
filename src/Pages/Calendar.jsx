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
        .react-calendar__navigation {
        margin-bottom: -20px; /* 년월과 일 사이 간격 조정 */
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
    .react-calendar__month-view__weekdays {
    abbr {
      display:none;
    }
  }
`;

const CalendarSite = () => {
    const [dates, setDates] = useState([new Date(), new Date()]); // 출발일과 도착일을 저장할 상태

    const onChange = (value) => {
        if (Array.isArray(value)) {
            setDates(value); // 날짜 범위를 선택하면 상태를 업데이트
        }
    };

    const formatDateRange = () => {
        const [startDate, endDate] = dates;
        const formattedStartDate = moment(startDate).format('MM.DD(ddd)');
        const formattedEndDate = moment(endDate).format('MM.DD(ddd)');
        const days = moment(endDate).diff(moment(startDate), 'days') + 1; // 선택된 일수 계산
        return `${formattedStartDate} - ${formattedEndDate} ${days}박 ${days + 1}일`;
    };

    return (
        <Container>
            <StyledCalendar 
                selectRange={true} // 날짜 범위 선택을 활성화
                onChange={onChange} 
                value={dates} 
                formatDay={(locale, date)=> moment(date).format("DD")} 
            />
            <ExtraContent>
                <Image src={bar}/>
                <Text>{formatDateRange()}</Text>
            </ExtraContent>
        </Container>
    );
}

export default CalendarSite;
