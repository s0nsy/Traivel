import React, { useState, useEffect} from 'react';
import styled from 'styled-components';
import ArrowLeftIcon from '../../assets/Arrow.png'; // 화살표 이미지 경로
import ArrowRightIcon from '../../assets/ArrowRight.png'; // 화살표 이미지 경로

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
  flex-direction: row; 
  align-items: center; 
  justify-content: space-between; 
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem;
  color: #ffffff;
  padding: 1rem 10rem; 
  position: relative;
`;

const FoodItem = styled.div`
  flex: 1; 
  margin: 0 1rem; 
  border-radius: 1rem; 
  padding: 1rem; 
  background-color: rgba(255, 255, 255, 0.05);
  max-width: 300px; 
`;
const FoodTitle = styled.h3`
  color: #ffffff;
  margin-bottom: 0.5rem;
  border: 1px solid white;
  border-radius: 1rem;
  text-align: center; /* 가운데 정렬 */
  padding: 0.5rem 1rem; /* 글자와 테두리 사이 여백 */
  display: inline-block; /* 글자 크기에 맞게 테두리가 조정됨 */
`;

const FoodList = styled.ul`
  list-style-type: none; /* 기본 목록 스타일 제거 */
  padding: 0;
  margin: 0; /* 외부 여백 제거 */
`;

const FoodListItem = styled.li`
  margin-bottom: 0.5rem;
  display: flex; /* 동그라미와 텍스트를 가로로 나란히 배치 */
  align-items: center;

  /* FoodLink 앞에 하얀색 동그라미 추가 */
  &::before {
    content: '●'; /* 동그라미 기호 */
    color: #ffffff; /* 동그라미 색상을 하얀색으로 */
    font-size: 0.5rem; /* 동그라미 크기 조정 */
    margin-right: 0.5rem; /* 동그라미와 텍스트 사이 간격 */
  }
`;

const FoodLink = styled.a`
  color: #ffffff; /* 링크 텍스트 색상 */
  text-decoration: none; /* 링크 밑줄 제거 */
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
  const [currentPage, setCurrentPage] = useState(0); // 페이지 상태

  // 디버깅: 전달된 foods 데이터 확인
  useEffect(() => {
    console.log('Received foods:', foods);
  }, [foods]);

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

  // 현재 페이지에 표시할 데이터 계산
  const itemsPerPage = 3; // 한 페이지에 보여줄 음식 수
  const startIndex = currentPage * itemsPerPage;
  const currentFoods = foods.slice(startIndex, startIndex + itemsPerPage);

  return (
    <FoodWrapper>
      <Title>🍴 이런 음식들이 유명해요</Title>
      <FoodContainer>
        
        
        {currentFoods.map((food, index) => (
          <FoodItem key={index}>
            <FoodTitle>{food.foodName}</FoodTitle>
            <FoodList>
              {food.shops.map((shop, shopIndex) => (
                <FoodListItem key={shopIndex}>
                  <FoodLink>{shop}</FoodLink>
                  </FoodListItem>
              ))}
              </FoodList>
              </FoodItem>
            
          
        ))}
        
      </FoodContainer>
    </FoodWrapper>
  );
}

export default Food;
