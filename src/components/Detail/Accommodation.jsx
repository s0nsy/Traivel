import React, { useState } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

// Styled components
const AccommodationWrapper = styled.div`
  width: 73.5rem;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem;
  color: #ffffff;
`;

const AccommodationContainer = styled.div`
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem;
  color: #ffffff;
  padding: 1rem;
  position: relative;
`;

const SmallText = styled.div`
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
  margin-bottom: 1rem;
`;

const HotelImage = styled.img`
  width: 100%;
  height: auto;
  object-fit: contain;
  margin-bottom: 1rem;
`;

const TextContainer = styled.div`
  margin-bottom: 1.5rem;
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1.5rem;
  line-height: 2rem;
`;

const AccommodationLinkBox = styled.div`
  margin-bottom: 1.5rem;
`;

const AccommodationLink = styled.div`
  display: block;
  margin-bottom: 0.5rem;
  color: #01ECFF;
`;

const ArrowImageLeft = styled.img`
  position: absolute;
  top: 50%;
  left: -2rem;
  transform: translateY(-50%);
  cursor: pointer;
`;

const ArrowImageRight = styled.img`
  position: absolute;
  top: 50%;
  right: -2rem;
  transform: translateY(-50%);
  cursor: pointer;
`;

const Accommodation = ({ accommodations }) => {
  const [currentPage, setCurrentPage] = useState(0);

  const handleNextPage = () => {
    if (currentPage < accommodations.length - 1) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  if (accommodations.length === 0) {
    return <div>로딩 중...</div>;
  }

  const currentAccommodation = accommodations[currentPage];

  // "목록" 부분을 분리
  const accommodationDescription = currentAccommodation.split("[목록]")[0].trim();
  const accommodationList = currentAccommodation.split("[목록]")[1]
    ?.split("\n")
    .filter((item) => item.trim() !== "") || [];

  return (
    <AccommodationWrapper>
      <Title>🏡 요즘 많이 찾는 숙박시설을 모아봤어요</Title>
      <AccommodationContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }}
        />

        <>
          {/* 숙박시설 설명 */}
          <TextContainer>{accommodationDescription}</TextContainer>

          {/* 숙박시설 목록 */}
          <AccommodationLinkBox>
            {accommodationList.length > 0 ? (
              accommodationList.map((link, index) => (
                <AccommodationLink key={index}>
                  {link.trim()}
                </AccommodationLink>
              ))
            ) : (
              <div>숙박 목록이 없습니다.</div>
            )}
          </AccommodationLinkBox>
        </>

        <ArrowImageRight
          src={ArrowRight}
          alt="Right Arrow"
          onClick={handleNextPage}
          style={{ visibility: currentPage === accommodations.length - 1 ? 'hidden' : 'visible' }}
        />
      </AccommodationContainer>
    </AccommodationWrapper>
  );
};

export default Accommodation;

