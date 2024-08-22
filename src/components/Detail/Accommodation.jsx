import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Hotel from '../../assets/Accommodation1.png';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

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

const AccommodationLinkBox = styled.div`
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
  const [pageContent, setPageContent] = useState([]); // API로부터 받아올 페이지 콘텐츠

  useEffect(() => {
    // API로부터 데이터를 받아오는 함수
    const fetchPageContent = async () => {
      try {
        const response = await fetch('/api/detail', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          },
        });

        const data = await response.json();

        // data.optComment가 존재하는지 확인하고 데이터 생성
        const fetchedContent = [
          {
            smallText: '숙박시설',
            description: data.optComment?.hotel1 || '숙박시설에 대한 설명이 없습니다.',
            links: ['#사이트1', '#사이트2', '#사이트3', '#사이트4', '#사이트5'], // 실제 링크로 대체
          },
          {
            smallText: '숙박시설',
            description: data.optComment?.hotel2 || '숙박시설에 대한 설명이 없습니다.',
            links: ['#사이트1', '#사이트2', '#사이트3', '#사이트4', '#사이트5'], // 실제 링크로 대체
          },
          {
            smallText: '숙박시설',
            description: data.optComment?.hotel3 || '숙박시설에 대한 설명이 없습니다.',
            links: ['#사이트1', '#사이트2', '#사이트3', '#사이트4', '#사이트5'], // 실제 링크로 대체
          },
        ];

        setPageContent(fetchedContent);
      } catch (error) {
        console.error('Failed to fetch page content:', error);
      }
    };

    fetchPageContent();
  }, []);

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

  if (pageContent.length === 0) {
    return <div>로딩 중...</div>;
  }

  return (
    <AccommodationWrapper>
      <Title>🏡요즘 많이 찾는 숙박시설을 모아봤어요</Title>
      <AccommodationContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }} // 첫 페이지에서 숨김
        />
        
        <>
          <SmallText>{pageContent[currentPage].smallText}</SmallText>
          <HotelImage src={Hotel} alt="숙박 이미지" />
          <SmallText2>추천 사이트</SmallText2>
          <TextContainer>
            {pageContent[currentPage].description}
            <br />
            텍스트 최대 3줄
          </TextContainer>
          <AccommodationLinkBox>
            {pageContent[currentPage].links.map((link, index) => (
              <AccommodationLink key={index} href="#">
                {link}
              </AccommodationLink>
            ))}
          </AccommodationLinkBox>
        </>
        
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
