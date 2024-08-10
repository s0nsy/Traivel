import React, { useState } from 'react';
import styled from 'styled-components';
import Hotel from '../../../assets/Accommodation1.png';
import ArrowLeft from '../../../assets/Arrow.png';
import ArrowRight from '../../../assets/ArrowRight.png';

const AccommodationWrapper = styled.div`
  width: 100%;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; /* 10px -> 0.625rem */
  color: #ffffff;
`;

const AccommodationContainer = styled.div`
  margin-bottom: 1.25rem; /* 20px -> 1.25rem */
  width: 100%;
  height: 25rem;
  padding: 1.25rem; /* 20px -> 1.25rem */
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

const HotelImage = styled.img`
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
  top: 1.7rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
`;

const SmallText2 = styled.div`
  position: absolute;
  top: 14rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1.2rem;
  font-weight: 400;
`;

const AccommodationLinkBox = styled.div`
  position: absolute;
  top: 16.81rem;
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

const AccommodationLink = styled.a`
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

function Accommodation() {
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
    <AccommodationWrapper>
      <Title><h2>요즘 많이 찾는 숙박시설을 모아봤어요</h2></Title>
      <AccommodationContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }} // 첫 페이지에서 숨김
        />

        {currentPage === 0 && (
          <>
            <SmallText>숙박시설</SmallText>
            <HotelImage src={Hotel} alt="숙박 이미지" />
            <SmallText2>추천 사이트</SmallText2>
            <TextContainer>
              제주도에서 인기 있는 숙박시설을 소개합니다. 다양한 선택지가 있으니 여행 스타일에 맞춰 선택해보세요.
              <br />
              텍스트 최대 3줄
            </TextContainer>
            <AccommodationLinkBox>
              {['호텔 신라', '롯데 호텔', '해비치 호텔', '제주 히든 클리프 호텔', '라마다 호텔'].map(
                (url, index) => (
                  <AccommodationLink key={index} href="#">
                    #{url}
                  </AccommodationLink>
                )
              )}
            </AccommodationLinkBox>
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
      </AccommodationContainer>
    </AccommodationWrapper>
  );
}

export default Accommodation;
