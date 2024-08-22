import React, { useState } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

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
  text-decoration: underline;
`;

const FoodLink = styled.a`
  color: var(--White, #FFF);
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
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

function Food({ foods }) {
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

  if (foods.length === 0) {
    return <div>로딩 중...</div>;
  }

  return (
    <FoodWrapper>
      <Title>🍴이런 음식들이 유명해요</Title>
      <FoodContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }}
        />

        {foods.slice(currentPage * 3, currentPage * 3 + 3).map((food, index) => (
          <FoodItem key={index}>
            <FoodTitle>{food.title}</FoodTitle>
            <FoodList>
              {food.links.map((link, linkIndex) => (
                <FoodListItem key={linkIndex}>
                  <FoodLink href={link.url} target="_blank" rel="noopener noreferrer">
                    {link.name}
                  </FoodLink>
                </FoodListItem>
              ))}
            </FoodList>
          </FoodItem>
        ))}

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
