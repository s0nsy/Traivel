import React, { useEffect, useState } from "react";
import styled from "styled-components";
import Circle from "../../assets/circle.png";
import ShortLine from "../../assets/Frame 2085666344.png";
import Icon from "../../assets/travel-icon.png";
import axios from "axios";

const TotalContainer = styled.div``;
const DayContainer = styled.div``;

const DayDate = styled.div`
  margin: 20px 0 10px 0;
  margin-left: 350px;
  color: #fff;
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 36px; /* 112.5% */
`;

const DayFrame = styled.div`
  margin-left: 350px;
  width: 1000px;
  flex-shrink: 0;
  border-radius: 12px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  padding: 20px;
`;

const PlanContainer = styled.div`
  color: #fff;
  display: flex;
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
`;

const PlanImg1 = styled.img`
  margin: 20px 10px;
  width: 12px;
  height: 12px;
`;

const PlanTitle = styled.div`
  color: #fff;
  margin: 10px 0;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const PlanImg2 = styled.img`
  width: 3px;
  height: 28px;
  flex-shrink: 0;
  margin: 10px 6px;
`;

const PlanDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  margin: 10px 0;
  font-weight: 200;
  line-height: 28px; /* 116.667% */
`;

const TipContainer = styled.div`
  margin: 20px 0 0 350px;
  color: #fff;
`;

const TipFrame = styled.div`
  margin-top: 10px;
  width: 1000px;
  flex-shrink: 0;
  border-radius: 12px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  padding: 20px;
`;

const TipSubHeader = styled.div`
  margin: 15px 0;
`;
const TipHeader = styled.div`
  display: flex;
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
`;

const TipTitle = styled.div`
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 116.667% */
`;

const TipImg = styled.img`
  width: 20px;
  height: 20px;
  margin: 6px 4px;
`;

const TipSubTitle = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
`;
const TipDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 16px;
  font-style: normal;
  font-weight: 200;
  line-height: normal;
`;

function DayLists({ region, city, points }) {
  const [itinerary, setItinerary] = useState([]);
  const [error, setError] = useState(null); // 상태 추가

  useEffect(() => {
    const fetchItinerary = async () => {
      console.log("API 호출 시작");
      try {
        const response = await axios.post(
          "/api/routes",
          {
            destinations: [
              {
                region: region,
                district: city,
                points: points,
              },
            ],
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        console.log("서버 응답:", response.data);
        if (response.data.success) {
          setItinerary(response.data.data);
          setError(null); // 성공 시 에러 상태 초기화
        } else {
          setError("API 호출 실패: " + response.data.message); // 에러 상태 설정
        }
      } catch (error) {
        console.error(
          "에러 발생:",
          error.response ? error.response.data : error.message
        );
        setError("서버 에러: " + (error.response ? error.response.data.message : error.message)); // 에러 상태 설정
      }
    };

    if (region && city && points && points.length > 0) {
      fetchItinerary();
    } else {
      console.warn("필수 데이터가 누락되었습니다.");
    }
  }, [region, city, points]);

  return (
    <TotalContainer>
      {itinerary.map((day, i) => (
        <DayContainer key={i}>
          <DayDate> {day.day}일차</DayDate>
          <DayFrame>
            {day.places.map((place, index) => (
              <PlanContainer key={index}>
                <PlanImg1 src={Circle} alt="원" />
                <PlanTitle>{place.name}</PlanTitle>
                <PlanImg2 src={ShortLine} alt="짧은 선" />
                <PlanDetail>{place.hours}</PlanDetail>
              </PlanContainer>
            ))}
          </DayFrame>
          <TipContainer>
            <TipHeader>
              <TipTitle>여행 Tip</TipTitle>
              <TipImg src={Icon} alt="아이콘" />
            </TipHeader>
            <TipFrame>
              <TipSubHeader>
                <TipSubTitle>추천 음식</TipSubTitle>
                <TipDetail>{day.places[0]?.popularMenu || '정보 없음'}</TipDetail> {/* null 체크 */}
              </TipSubHeader>
              <TipSubHeader>
                <TipSubTitle>추천 사항</TipSubTitle>
                <TipDetail>{day.places[0]?.recommendations || '정보 없음'}</TipDetail> {/* null 체크 */}
              </TipSubHeader>
            </TipFrame>
          </TipContainer>
        </DayContainer>
      ))}
    </TotalContainer>
  );
}

export default DayLists;
