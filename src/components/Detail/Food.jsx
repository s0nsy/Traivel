import React, { useState } from 'react';
import styled from 'styled-components';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

// Styled components
const FoodWrapper = styled.div`
  width: 73.5rem;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem;
  color: #ffffff;
`;

const FoodContainer = styled.div`
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem;
  color: #ffffff;
  padding: 1rem;
  position: relative;
`;

const FoodItem = styled.div`
  margin-bottom: 2rem;
`;

const FoodTitle = styled.h3`
  color: #ffffff;
  margin-bottom: 0.5rem;
`;

const FoodList = styled.ul`
  list-style-type: none;
  padding: 0;
`;

const FoodListItem = styled.li`
  margin-bottom: 0.5rem;
`;

const FoodLink = styled.a`
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

function Food({ foods }) {
  console.log(foods);
  const [currentPage, setCurrentPage] = useState(0); // 페이지 상태

  const handleNextPage = () => {
    if (currentPage < Math.ceil(foods.length / 3) - 1) {
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

  // 페이지당 3개의 음식을 표시하기 위해 slice로 데이터를 잘라냄
  const currentFoods = foods.slice(currentPage * 3, currentPage * 3 + 3);

  return (
    <FoodWrapper>
      <Title>🍴 이런 음식들이 유명해요</Title>
      <FoodContainer>
        <ArrowImageLeft
          src={ArrowLeft}
          alt="Left Arrow"
          onClick={handlePrevPage}
          style={{ visibility: currentPage === 0 ? 'hidden' : 'visible' }}
        />

        {currentFoods.map((food, index) => (
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
          style={{
            visibility: currentPage === Math.ceil(foods.length / 3) - 1 ? 'hidden' : 'visible',
          }}
        />
      </FoodContainer>
    </FoodWrapper>
  );
}

export default Food;
