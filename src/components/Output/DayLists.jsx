import React, { useEffect, useState } from "react";
import styled from "styled-components";
import { useSelector } from "react-redux";
import Circle from "../../assets/circle.png";
import ShortLine from "../../assets/Frame 2085666344.png";
import axios from "axios";
import Loader from "../../Pages/infoLoad";
import Error from "../../Pages/Error";
import { useNavigate } from "react-router-dom";
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
  flex-direction: column;
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
  position: relative;
  left: 20px;
  margin: 5px 0;
  font-weight: 200;
  line-height: 28px;
`;

const PlanAttraction = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  margin: 10px 0;
  font-weight: 300;
  line-height: 28px;
  list-style-type: disc;
  margin-left: 20px;
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

const PlanDetail1 = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-weight: 200;
  line-height: normal;
`;

const P = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-weight: 600;
  line-height: normal;
`;

const Strong = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-weight: 600;
  line-height: normal;
  position: relative;
`;

const TipDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 16px;
  font-weight: 200;
  line-height: normal;
`;

const PlanLump = styled.div`
  olor: #fff;
  display: flex;
  align-items: center;
  font-family: Pretendard;
  font-size: 24px;
  font-weight: 600;
  line-height: 28px;
`;

const AttractionsList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;
`;

const AttractionItem = styled.li`
  margin-left: 20px; /* 리스트 아이템에 대한 간격 */
  font-family: Pretendard;
  font-size: 16px;
  position: relative;
  right: 20px;
  color: #fff;
`;
function DayLists() {
  const { region, district, features } = useSelector(
    (state) => state.selectedItem
  );
  const duration = useSelector((state) => state.survey.duration);
  const PROXY = window.location.hostname === "localhost" ? "" : "/proxy";

  const [loading, setLoading] = useState(true);
  const [itinerary, setItinerary] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();  // useNavigate 훅 사용

  useEffect(() => {
    const fetchItinerary = async () => {
      try {
        setLoading(true);
        const response = await axios.post(
          `${PROXY}/api/routes`,
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

        if (response.data.success) {
          setItinerary(response.data.data);
          setError(null);
        } else {
          setError("API 호출 실패: " + response.data.message);
          navigate('/error');  // 에러 페이지로 리디렉션
        }
      } catch (error) {
        setError("서버에 연결할 수 없습니다. 인터넷 연결을 확인하거나 나중에 다시 시도해 주세요.");
        navigate('/error');  // 에러 페이지로 리디렉션
      } finally {
        setLoading(false);
      }
    };

    if (region && district && features && features.length > 0) {
      fetchItinerary();
    }
  }, [region, district, features, navigate]);

  const parseDuration = (duration) => {
    const match = duration.match(/(\d+)박 (\d+)일/);
    if (match) {
      return parseInt(match[2], 10);
    }
    return 0;
  };

  const days = parseDuration(duration);
  const filteredItinerary = itinerary.slice(0, days);

  return (
    <TotalContainer>
      {loading && <Loader />}
      {filteredItinerary.length > 0 ? (
        filteredItinerary.map((day, i) => (
          <DayContainer key={i}>
            <DayDate>{i + 1}일차</DayDate>
            <DayFrame>
              {day.places && day.places.map((place, index) => (
                <PlanContainer key={index}>
                  <PlanLump>
                    <PlanImg1 src={Circle} alt="원" />
                    <PlanTitle>{place.name}</PlanTitle>
                    <PlanImg2 src={ShortLine} alt="짧은 선" />
                    <PlanDetail1>{place.recommendations}</PlanDetail1>
                  </PlanLump>
                  <PlanDetail>
                    <P>운영 시간</P> {place.hours}
                    <P>추천 음식</P> {place.popularMenu}
                    {place.attractions && place.attractions.length > 0 && (
                      <PlanAttraction>
                        <AttractionsList>
                          <Strong>주요 명소</Strong>
                          {place.attractions.map((attraction, attrIndex) => (
                            <AttractionItem key={attrIndex}>
                              {attraction}
                            </AttractionItem>
                          ))}
                        </AttractionsList>
                      </PlanAttraction>
                    )}
                  </PlanDetail>
                </PlanContainer>
              ))}
            </DayFrame>
          </DayContainer>
        ))
      ) : null}
    </TotalContainer>
  );
}

export default DayLists;