import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

const AttractionsWrapper = styled.div`
  width: 100%;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; 
  color: #ffffff;
`;

const AttractionsContainer = styled.div`
  display: flex;
  width: 100%;
  height: 23rem;
  padding: 1.25rem 1rem 1.1875rem 1rem; 
  justify-content: space-around;
  align-items: stretch;
  gap: 0.875rem; 
  border-radius: 0.75rem; 
  background: rgba(255, 255, 255, 0.10);
  position: relative; 
`;

const AttractionSection = styled.div`
  flex: 1;
  border-radius: 0.5rem; 
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0.1rem;
`;

const ImageContainer = styled.img`
   width:356px;
   height:321px;
  margin-bottom: 1rem;
  border-radius: 0.625rem; 
`;

const TextContainer = styled.div`
  width: 100%;
  text-align: center;
  color: var(--White, #FFF);
  font-family: Pretendard, sans-serif;
  font-size: 1.2rem;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
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

function Attractions({ attractions }) {
  const [currentPage, setCurrentPage] = useState(0); // 페이지 상태
  const itemsPerPage = 3; // 페이지 당 표시할 항목 수
  const pageContent = [];

  // 페이지별로 attractions를 슬라이스하여 저장
  for (let i = 0; i < attractions.length; i += itemsPerPage) {
    pageContent.push(attractions.slice(i, i + itemsPerPage));
  }

  const handleNextPage = () => {
    if (currentPage < pageContent.length - 1) {
      setCurrentPage(currentPage + 1); // 다음 페이지로 이동
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1); // 이전 페이지로 이동
    }
  };

  if (attractions.length === 0) {
    return <div>로딩 중...</div>;
  }

  return (
    <AttractionsWrapper>
      <Title>🌟주요 관광지, 쇼핑 정보를 모아봤어요</Title>
      <AttractionsContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }} // 첫 페이지에서 숨김
        />

        {/* 현재 페이지의 콘텐츠를 렌더링 */}
        {pageContent[currentPage].map((attraction, index) => (
          <AttractionSection key={index}>
            <ImageContainer src={attraction.firstimage}/>
            <TextContainer>{attraction.title}</TextContainer>
          </AttractionSection>
        ))}

        <ArrowImageRight
          src={ArrowRight}
          alt="Right Arrow"
          onClick={handleNextPage}
          style={{ visibility: currentPage === pageContent.length - 1 ? 'hidden' : 'visible' }} // 마지막 페이지에서 숨김
        />
      </AttractionsContainer>
    </AttractionsWrapper>
  );
}

export default Attractions;
