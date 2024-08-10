import React, { useState } from 'react';
import styled from 'styled-components';
import Car from '../../../assets/recommendation1.png';
import ArrowLeft from '../../../assets/Arrow.png';
import ArrowRight from '../../../assets/ArrowRight.png';

const RecommendationsWrapper = styled.div`
  width: 73.5rem;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; /* 10px -> 0.625rem */
  color: #ffffff;
`;

const RecommendationsContainer = styled.div`
  width: 73.5rem;
  height: 23rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem; /* 10px -> 0.625rem */
  color: #ffffff;
  position: relative;
`;

const TextContainer = styled.div`
  position: absolute;
  top: 5.5rem;
  left: 13rem;
  padding-right: 2.06rem;
  max-width: calc(100% - 13.75rem);
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1.5rem;
  font-style: normal;
  font-weight: 400;
  line-height: 2rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
`;

const CarImage = styled.img`
  position: absolute;
  top: 2.75rem;
  left: 1.62rem;
  width: auto;
  height: auto;
  max-width: calc(62.94rem - 1.62rem);
  max-height: calc(21.75rem - 2.75rem - 8.25rem);
  object-fit: contain;
`;

const SmallText = styled.div`
  position: absolute;
  top: 2.5rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
`;

const SmallText2 = styled.div`
  position: absolute;
  top: 13rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1.2rem;
  font-weight: 400;
`;

const RecommendationLinkBox = styled.div`
  position: absolute;
  top: 15.81rem;
  left: 2rem;
  padding: 1.75rem 2.06rem;
  border-radius: 0.75rem;
  border: 1px solid var(--Main_2, #01ECFF);
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
  text-align: left;
  display: block;
  width: calc(100% - 4.06rem);
  line-height: 1.5rem;
`;

const RecommendationLink = styled.a`
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1.5rem;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
  text-decoration-line: underline;
  display: inline-block;
  margin-right: 1rem;
`;

const ArrowImageLeft = styled.img`
  position: absolute;
  top: 50%;
  left: -6rem;
  transform: translateY(-50%);
  width: 5rem;
  height: 5rem;
  cursor: pointer;
`;

const ArrowImageRight = styled.img`
  position: absolute;
  top: 50%;
  right: -6rem;
  transform: translateY(-50%);
  width: 5rem;
  height: 5rem;
  cursor: pointer;
`;

function Recommendations() {
  const [currentPage, setCurrentPage] = useState(0); // 페이지 상태

  const handleNextPage = () => {
    if (currentPage < 2) {
      setCurrentPage(currentPage + 1); // 다음 페이지로 이동
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1); // 이전 페이지로 이동
    }
  };

  return (
    <RecommendationsWrapper>
      <Title><h2>이런 교통수단을 이용하면 더욱 편리해요</h2></Title>
      <RecommendationsContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }} // 첫 페이지에서 숨김
        />
        
        {currentPage === 0 && (
          <>
            <SmallText>렌트카</SmallText>
            <CarImage src={Car} alt="렌터카 이미지" />
            <SmallText2>추천 사이트</SmallText2>
            <TextContainer>
              제주도는 렌터카를 이용해 여행하는 것을 추천드립니다. 서귀포에서도 다양한 렌터카 업체를 통해 차량을 대여할 수 있습니다. 미리 예약하면 공항에서 바로 차량을 픽업할 수 있어 편리합니다.
              <br />
              텍스트 최대 3줄
            </TextContainer>
            <RecommendationLinkBox>
              <RecommendationLink href="#">#돌하루팡</RecommendationLink>
              <RecommendationLink href="#">#제주 렌트카</RecommendationLink>
              <RecommendationLink href="#">#하이렌트카</RecommendationLink>
              <RecommendationLink href="#">#제주 OK 렌트카</RecommendationLink>
              <RecommendationLink href="#">#좋은 렌트카</RecommendationLink>
              <RecommendationLink href="#">#기가막힌 렌트카</RecommendationLink>
            </RecommendationLinkBox>
          </>
        )}
        
        {currentPage === 1 && (
          <TextContainer style={{ top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }}>
            두번째 페이지입니다.
          </TextContainer>
        )}
        
        {currentPage === 2 && (
          <TextContainer style={{ top: '50%', left: '50%', transform: 'translate(-50%, -50%)' }}>
            세번째 페이지입니다.
          </TextContainer>
        )}
        
        <ArrowImageRight
          src={ArrowRight}
          alt="Right Arrow"
          onClick={handleNextPage}
          style={{ visibility: currentPage === 2 ? 'hidden' : 'visible' }} // 마지막 페이지에서 숨김
        />
      </RecommendationsContainer>
    </RecommendationsWrapper>
  );
}

export default Recommendations;
