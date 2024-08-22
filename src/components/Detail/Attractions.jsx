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

const ImageContainer = styled.div`
  flex: 1;
  width: 100%;
  background-image: url(${props => props.imageUrl});
  background-size: cover;
  background-position: center;
  margin-bottom: 0.5rem; 
  border-radius: 0.5rem; 
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
  const [pageContent, setPageContent] = useState([[], [], []]); // API로부터 받아올 페이지 콘텐츠

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

        // 받아온 데이터를 페이지별로 나누어서 설정합니다.
        const fetchedContent = [
          [
            { text: data.tourData.item[0].title, imageUrl: data.tourData.item[0].firstimage },
            { text: data.tourData.item[1].title, imageUrl: data.tourData.item[1].firstimage },
            { text: data.tourData.item[2].title, imageUrl: data.tourData.item[2].firstimage },
          ],
          [
            { text: data.tourData.item[3].title, imageUrl: data.tourData.item[3].firstimage },
            { text: data.tourData.item[4].title, imageUrl: data.tourData.item[4].firstimage },
            { text: data.tourData.item[5].title, imageUrl: data.tourData.item[5].firstimage },
          ],
          [
            { text: data.tourData.item[6].title, imageUrl: data.tourData.item[6].firstimage },
            { text: data.tourData.item[7].title, imageUrl: data.tourData.item[7].firstimage },
            { text: data.tourData.item[8].title, imageUrl: data.tourData.item[8].firstimage },
          ]
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

  if (pageContent[0].length === 0) {
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

        {pageContent[currentPage].map((item, index) => (
          <AttractionSection key={index}>
            <ImageContainer imageUrl={item.imageUrl} />
            <TextContainer>{item.text}</TextContainer>
          </AttractionSection>
        ))}

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
