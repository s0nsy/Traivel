import React, { useState,useEffect } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';
import Hotel from '../../assets/Accommodation1.png';

// Styled components
const AccommodationWrapper = styled.div`
  width: 73.5rem;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem;
  color: #ffffff;
`;

const AccommodationContainer = styled.div`
 width: 73.5rem;
  height: 23rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem;
  color: #ffffff;
  position: relative;
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

const AccommodationLinkBox = styled.div`
  display:flex;
  justify-content: space-between;
  align-items: center;
  position: absolute;
  top: 15.81rem;
  left: 2rem;
  padding: 1.75rem 15rem;
  border-radius: 0.75rem;
  border: 1px solid var(--Main_2, #01ECFF);
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
  text-align: left;
  width: calc(100% - 4.06rem);
`;

const AccommodationLink = styled.div`
  display: block;
  margin-bottom: 0.5rem;
  color: #01ECFF;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
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
  const [tip, setTip] = useState("");
  const [links, setLinks] = useState([]);

  useEffect(() => {
    console.log('Received accommo:', accommodations);

    if (accommodations.length > 0) {
      // 현재 페이지의 accommodation 데이터 추출
      const currentAccommodation = accommodations[currentPage].description;
      
      // 숙박 정보와 추천 사이트 설정
      setTip(currentAccommodation.reason);
      setLinks(currentAccommodation.sites);
    }
  }, [accommodations, currentPage]);

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

  return (
    <AccommodationWrapper>
      <Title>🏡 요즘 많이 찾는 숙박시설을 모아봤어요</Title>
      <AccommodationContainer>
        <SmallText>{accommodations[currentPage].description.name}</SmallText>
        <HotelImage src={Hotel}/>
        <SmallText2>추천 사이트</SmallText2>
        <TextContainer>{tip}</TextContainer>

        <AccommodationLinkBox>
          {links.map((link, index) => (
            <AccommodationLink
              key={index}
              href={link.url}
              target="_blank"
              rel="noopener noreferrer"
            >
              {link.name}
            </AccommodationLink>
          ))}
        </AccommodationLinkBox>

        <ArrowImageLeft
          src={ArrowLeft}
          alt="이전 페이지로 이동"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? "hidden" : "visible" }}
        />
        <ArrowImageRight
          src={ArrowRight}
          alt="다음 페이지로 이동"
          onClick={handleNextPage}
          style={{
            visibility: currentPage === accommodations.length - 1 ? "hidden" : "visible",
          }}
        />
      </AccommodationContainer>
    </AccommodationWrapper>
  );
};

export default Accommodation;