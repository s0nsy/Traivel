import React from 'react';
import styled from 'styled-components';
import InfoBox from './components/InfoBox';
import MapSection from './components/MapSection';
import Recommendations from './components/Recommendations';
import Accommodation from './components/Accommodation';
import Food from './components/Food';
import Attractions from './components/Attractions';
import Footer from './components/Footer';

// Styled-components로 스타일링된 텍스트 컨테이너 정의
const HeaderText = styled.div`
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  gap: 1rem;
  font-size: 3rem;
  font-weight: bold;
  text-align: center;
  width: 73.75rem; /* ContentDiv와 동일한 width로 변경 */
  color: #ffffff; /* 기본 텍스트 색상 */
`;

const GradientText = styled.span`
  background: var(--gradation_1, linear-gradient(105deg, #00E3F1 10.28%, #A3FFD4 93.01%));
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`;

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 1.25rem; /* 20px -> 1.25rem */
  background: linear-gradient(to top, rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.5)); /* 그라데이션 효과 */
`;

const ContentDiv = styled.div`
  display: flex;
  width: 73.75rem;
  padding-top: 15.75rem;
  flex-direction: column;
  align-items: flex-start;
  gap: 4rem; /* 각 컴포넌트 사이의 간격을 4rem으로 설정 */
`;

const MainPage = () => {
  const destination = '서귀포'; // 백엔드에서 받아오는 예시 데이터
  const mapImage = 'path_to_map_image';
  const recommendations = ['렌터카 1', '렌터카 2', '렌터카 3'];
  const accommodations = ['호텔 1', '호텔 2', '호텔 3'];
  const foods = ['흑돼지', '갈치조림', '전복구이', '고기국수'];
  const attractions = ['천지연 폭포', '고기국수 맛집', '서귀포 예술의 전당'];

  return (
    <>
      <PageContainer>
        <ContentDiv>
          <HeaderText>
            요청해주신 <GradientText>{destination}</GradientText>의 여행 정보를 알아왔어요!
          </HeaderText>
          <InfoBox destination={destination} />
          <MapSection mapImage={mapImage} description="서귀포시는 여기에 위치해 있어요" />
          <Recommendations recommendations={recommendations} />
          <Accommodation accommodations={accommodations} />
          <Food foods={foods} />
          <Attractions attractions={attractions} />
          <Footer />
        </ContentDiv>
      </PageContainer>
    </>
  );
};

export default MainPage;
