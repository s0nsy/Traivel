import React, { useState } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../../assets/Arrow.png';
import ArrowRight from '../../../assets/ArrowRight.png';

const FoodWrapper = styled.div`
  width: 73.5rem;
`;

const FoodContainer = styled.div`
  margin-bottom: 1.25rem; 
  width: 73.5rem;
  height: 24rem;
  padding: 1.25rem; 
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem; 
  color: #ffffff;
  display: flex;
  justify-content: space-between;
  position: relative;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; 
  color: #ffffff;
`;

const FoodItem = styled.div`
  background: rgba(255, 255, 255, 0.10);
  border-radius: 0.625rem; 
  padding: 1.25rem; 
  flex: 1;
  margin: 0 0.625rem; 
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  box-sizing: border-box;
`;

const FoodTitle = styled.div`
  display: flex;
  padding: 1rem 1.5rem;
  justify-content: center;
  align-items: center;
  gap: 0.625rem;
  border-radius: 1.5rem;
  border: 1px solid var(--White, #FFF);
  margin-bottom: 0.9375rem; 
  text-align: left;
  width: fit-content;
`;

const FoodList = styled.ul`
  display: flex;
  width: 100%;
  flex-direction: column;
  align-items: flex-start;
  gap: 1.5625rem;
  list-style: disc;
  padding-left: 1.25rem; 
`;

const FoodListItem = styled.li`
  margin-bottom: 0.3125rem; 
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

function Food() {
  const [currentPage, setCurrentPage] = useState(0);

  const handleNextPage = () => {
    if (currentPage < 2) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePrevPage = () => {
    if (currentPage > 0) {
      setCurrentPage(currentPage - 1);
    }
  };

  return (
    <FoodWrapper>
      <Title><h2>이런 음식들이 유명해요</h2></Title>
      <FoodContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }}
        />

        {currentPage === 0 && (
          <>
            <FoodItem>
              <FoodTitle>흑돼지</FoodTitle>
              <FoodList>
                <FoodListItem>칠돈가</FoodListItem>
                <FoodListItem>돈사돈</FoodListItem>
                <FoodListItem>백년가게</FoodListItem>
                <FoodListItem>흑돼지명가</FoodListItem>
              </FoodList>
            </FoodItem>
            <FoodItem>
              <FoodTitle>갈치조림</FoodTitle>
              <FoodList>
                <FoodListItem>칠돈가</FoodListItem>
                <FoodListItem>돈사돈</FoodListItem>
                <FoodListItem>백년가게</FoodListItem>
                <FoodListItem>흑돼지명가</FoodListItem>
              </FoodList>
            </FoodItem>
            <FoodItem>
              <FoodTitle>전복구이</FoodTitle>
              <FoodList>
                <FoodListItem>칠돈가</FoodListItem>
                <FoodListItem>돈사돈</FoodListItem>
                <FoodListItem>백년가게</FoodListItem>
                <FoodListItem>흑돼지명가</FoodListItem>
              </FoodList>
            </FoodItem>
            <FoodItem>
              <FoodTitle>고기국수</FoodTitle>
              <FoodList>
                <FoodListItem>칠돈가</FoodListItem>
                <FoodListItem>돈사돈</FoodListItem>
                <FoodListItem>백년가게</FoodListItem>
                <FoodListItem>흑돼지명가</FoodListItem>
              </FoodList>
            </FoodItem>
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
          style={{ visibility: currentPage === 2 ? 'hidden' : 'visible' }}
        />
      </FoodContainer>
    </FoodWrapper>
  );
}

export default Food;
