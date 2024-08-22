import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { useSelector } from "react-redux"; 
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
  font-weight: 500;
  line-height: 36px;
`;

const DayFrame = styled.div`
  margin-left: 350px;
  width: 950px;
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
  font-weight: 600;
  line-height: 28px;
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
  font-weight: 600;
  line-height: 28px;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const PlanImg2 = styled.img`
  width: 3px;
  height: 28px;
  margin: 10px 6px;
`;

const PlanDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  margin: 10px 0;
  font-weight: 200;
  line-height: 28px;
`;

const TipContainer = styled.div`
  margin: 20px 0 0 350px;
  color: #fff;
`;

const TipFrame = styled.div`
  margin-top: 10px;
  width: 950px;
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
  font-weight: 600;
  line-height: 28px;
`;

const TipTitle = styled.div`
  font-family: Pretendard;
  font-size: 20px;
  font-weight: 500;
  line-height: 28px;
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
  font-weight: 600;
  line-height: normal;
`;
const TipDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 16px;
  font-weight: 200;
  line-height: normal;
`;

function DayLists() {
  const { region, district, features } = useSelector(
    (state) => state.selectedItem
  );
  console.log({region,district,features});
  
  
  const [itinerary, setItinerary] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchItinerary = async () => {
      try {
        console.log("Sending request with data:", {
          destinations: [
            {
              region: region,
              district: district,
              points: features,
            },
          ],
        });
  
        const response = await axios.post(
          "/api/routes",
          {
            destinations: [
              {
                region: region,
                district: district,
                points: features,
              },
            ],
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
  
        console.log("API 응답:", response.data);
        if (response.data.success) {
          setItinerary(response.data.data);
          setError(null);
        } else {
          setError("API 호출 실패: " + response.data.message);
        }
      } catch (error) {
        setError(
          "서버 에러: " +
            (error.response ? error.response.data.message : error.message)
        );
      }
    };
  
    if (region && district && features && features.length > 0) {
      fetchItinerary();
    }
  }, [region, district, features]);
  return (
    <TotalContainer>
      {error && <div>에러: {error}</div>}
      {itinerary.length === 0 && !error && <div>일정이 없습니다.</div>}
      {itinerary.map((day, i) => (
        <DayContainer key={i}>
          <DayDate>{day.day}일차</DayDate>
          <DayFrame>
            {day.places.map((place, index) => (
              <PlanContainer key={index}>
                <PlanImg1 src={Circle} alt="원" />
                <PlanTitle>{place.name}</PlanTitle>
                <PlanImg2 src={ShortLine} alt="짧은 선" />
                <PlanDetail>{place.recommendations}</PlanDetail>
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
                <TipDetail>
                  {day.places[0]?.popularMenu || "정보 없음"}
                </TipDetail>
              </TipSubHeader>
              <TipSubHeader>
                <TipSubTitle>추천 사항</TipSubTitle>
                <TipDetail>
                  {day.places[0]?.recommendations || "정보 없음"}
                </TipDetail>
              </TipSubHeader>
            </TipFrame>
          </TipContainer>
        </DayContainer>
      ))}
    </TotalContainer>
  );
}

export default DayLists;
