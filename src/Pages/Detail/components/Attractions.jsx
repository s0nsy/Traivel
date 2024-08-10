import React, { useState } from 'react';
import styled from 'styled-components';
import Background from '../../../assets/Detail_background.png';
import ArrowLeft from '../../../assets/Arrow.png';
import ArrowRight from '../../../assets/ArrowRight.png';

const AttractionsWrapper = styled.div`
  width: 100%;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; /* 10px -> 0.625rem */
  color: #ffffff;
`;

const AttractionsContainer = styled.div`
  display: flex;
  width: 100%;
  height: 23rem;
  padding: 1.25rem 1rem 1.1875rem 1rem; /* 20px 16px 19px 16px -> 1.25rem 1rem 1.1875rem 1rem */
  justify-content: space-around;
  align-items: stretch;
  gap: 0.875rem; /* 14px -> 0.875rem */
  border-radius: 0.75rem; /* 12px -> 0.75rem */
  background: rgba(255, 255, 255, 0.10);
  position: relative; /* Positioning for arrows */
`;

const AttractionSection = styled.div`
  flex: 1;
  border-radius: 0.5rem; /* 8px -> 0.5rem */
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0.1rem;
`;

const ImageContainer = styled.div`
  flex: 1;
  width: 100%;
  background-image: url(${Background});
  background-size: cover;
  background-position: center;
  margin-bottom: 0.5rem; /* 8px -> 0.5rem */
  border-radius: 0.5rem; /* 8px -> 0.5rem */
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

function Attractions() {
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
    <AttractionsWrapper>
      <Title><h2>주요 관광지, 쇼핑 정보를 모아봤어요</h2></Title>
      <AttractionsContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }} // 첫 페이지에서 숨김
        />

        {currentPage === 0 && (
          <>
            <AttractionSection>
              <ImageContainer />
              <TextContainer>첫 번째 텍스트</TextContainer>
            </AttractionSection>
            <AttractionSection>
              <ImageContainer />
              <TextContainer>두 번째 텍스트</TextContainer>
            </AttractionSection>
            <AttractionSection>
              <ImageContainer />
              <TextContainer>세 번째 텍스트</TextContainer>
            </AttractionSection>
          </>
        )}

        {currentPage === 1 && (
          <TextContainer style={{ margin: 'auto', fontSize: '2rem' }}>
            두번째 페이지입니다.
          </TextContainer>
        )}

        {currentPage === 2 && (
          <TextContainer style={{ margin: 'auto', fontSize: '2rem' }}>
            세번째 페이지입니다.
          </TextContainer>
        )}

        <ArrowImageRight
          src={ArrowRight}
          alt="Right Arrow"
          onClick={handleNextPage}
          style={{ visibility: currentPage === 2 ? 'hidden' : 'visible' }} // 마지막 페이지에서 숨김
        />
      </AttractionsContainer>
    </AttractionsWrapper>
  );
}

export default Attractions;
