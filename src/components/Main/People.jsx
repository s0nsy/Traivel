import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Left from '../../assets/left.png';
import right from '../../assets/right.png';
import minus from '../../assets/minus.png';
import plus from '../../assets/plus.png';
import bar from '../../assets/bar.png';
import { useDispatch, useSelector } from 'react-redux';
import { setAdults, setChildren, setInfants } from '../../store/surveySlice';

const BoxCon = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 400px;
    height: 256px;
    border-radius: 24px;
    background-color:#014C4E;
    padding: 30px 20px;
    box-sizing: border-box;
`;

const Header = styled.div`
    display: flex;
    align-items: center;
    margin-bottom: 20px;
`;

const ArrowImg = styled.img`
    width: 16px;
    height: 16px;
    cursor: pointer;
    margin: 0 70px;
`;

const Text = styled.p`
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
    font-size: 15px;
    font-weight: 600;
    margin: 0;
`;

const PeopleCon = styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between; /* 좌우로 정렬 */
    margin: 10px 0;
    padding: 0 25px; /* 양쪽에 패딩을 추가하여 좌우 여백을 만듦 */
    width: 100%; /* 부모 컨테이너의 전체 너비 사용 */
`;

const Minus = styled.img`
    width: 40px;
    height: 40px;
    margin: 0 30px;
    cursor: pointer;
`;

const Plus = styled.img`
    width: 40px;
    height: 40px;
    margin: 0 30px;
    cursor: pointer;
`;

const PeopleText = styled.div`
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
    font-size: 13px;
    font-weight: 400;
    margin: 0;
   
    
`;

const BarImg = styled.img`
    width: 320px;
    height: 2px; 
`;

const CountCon = styled.div`
    display: flex;
    align-items: center;
    gap: 15px; /* Minus, Text, Plus 사이의 간격 */
    
`;

const SubText = styled.span`
  display: block; /* 이 부분을 block으로 처리 */
  font-size: 14px;
  font-weight: 400;
  color: white;
`;


const People = () => {
    const dispatch = useDispatch();

    const adults = useSelector((state) => state.survey.adults);
    const children = useSelector((state) => state.survey.children);
    const infants = useSelector((state) => state.survey.infants);

    const handleIncrease = (type) => {
        if (type === "adult") dispatch(setAdults(adults + 1));
        if (type === "child") dispatch(setChildren(children + 1));
        if (type === "infant") dispatch(setInfants(infants + 1));
    };

    const handleDecrease = (type) => {
        if (type === "adult" && adults > 0) dispatch(setAdults(adults - 1));
        if (type === "child" && children > 0) dispatch(setChildren(children - 1));
        if (type === "infant" && infants > 0) dispatch(setInfants(infants - 1));
    };

    return (
        <BoxCon>
          <Header>
            <ArrowImg src={Left} alt="left arrow" />
            <Text>인원</Text>
            <ArrowImg src={right} alt="right arrow" />
          </Header>
          <PeopleCon>
            <PeopleText>
              성인
              <SubText>(13세 이상)</SubText>
            </PeopleText>
            <CountCon>
              <Minus src={minus} onClick={() => handleDecrease("adult")} alt="minus" />
              <Text>{adults}</Text>
              <Plus src={plus} onClick={() => handleIncrease("adult")} alt="plus" />
            </CountCon>
          </PeopleCon>
          <BarImg src={bar} alt="bar" />
          <PeopleCon>
            <PeopleText>
              어린이
              <SubText>(2~12세)</SubText>
            </PeopleText>
            <CountCon>
              <Minus src={minus} onClick={() => handleDecrease("child")} alt="minus" />
              <Text>{children}</Text>
              <Plus src={plus} onClick={() => handleIncrease("child")} alt="plus" />
            </CountCon>
          </PeopleCon>
          <BarImg src={bar} alt="bar" />
          <PeopleCon>
            <PeopleText>
              유아
              <SubText>(2세 미만)</SubText>
            </PeopleText>
            <CountCon>
              <Minus src={minus} onClick={() => handleDecrease("infant")} alt="minus" />
              <Text>{infants}</Text>
              <Plus src={plus} onClick={() => handleIncrease("infant")} alt="plus" />
            </CountCon>
          </PeopleCon>
        </BoxCon>
      );
    };

export default People;