import React, { useState } from 'react';
import styled from 'styled-components';
import Left from '../assets/left.png';
import right from '../assets/right.png';
import minus from '../assets/minus.png';
import plus from '../assets/plus.png';
import bar from '../assets/bar.png';

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
    margin: 0 40px;
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
    margin: 10px 0;
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

const PeopleText = styled.p`
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
    font-size: 10px;
    font-weight: 400;
    margin: 0;
`;

const BarImg = styled.img`
    width: 320px;
    height: 2px;
    margin: 10px 0;
`;

const People = () => {
    const [adult, setAdult] = useState(0);
    const [child, setChild] = useState(0);
    const [infant, setInfant] = useState(0);

    const handleIncrease = (type) => {
        if (type === "adult") setAdult(adult + 1);
        if (type === "child") setChild(child + 1);
        if (type === "infant") setInfant(infant + 1);
    };

    const handleDecrease = (type) => {
        if (type === "adult" && adult > 0) setAdult(adult - 1);
        if (type === "child" && child > 0) setChild(child - 1);
        if (type === "infant" && infant > 0) setInfant(infant - 1);
    };

    return (
        <BoxCon>
            <Header>
                <ArrowImg src={Left} />
                <Text>인원</Text>
                <ArrowImg src={right} />
            </Header>
            <PeopleCon>
                <PeopleText>성인(13세이상)</PeopleText>
                <Minus src={minus} onClick={() => handleDecrease("adult")} />
                <Text>{adult}</Text>
                <Plus src={plus} onClick={() => handleIncrease("adult")} />
            </PeopleCon>
            <BarImg src={bar} />
            <PeopleCon>
                <PeopleText>어린이(2~12세)</PeopleText>
                <Minus src={minus} onClick={() => handleDecrease("child")} />
                <Text>{child}</Text>
                <Plus src={plus} onClick={() => handleIncrease("child")} />
            </PeopleCon>
            <BarImg src={bar} />
            <PeopleCon>
                <PeopleText>유아(2세 미만)</PeopleText>
                <Minus src={minus} onClick={() => handleDecrease("infant")} />
                <Text>{infant}</Text>
                <Plus src={plus} onClick={() => handleIncrease("infant")} />
            </PeopleCon>
        </BoxCon>
    );
};

export default People;