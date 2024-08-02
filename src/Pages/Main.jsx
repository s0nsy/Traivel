import React from 'react';
import styled from 'styled-components';
import background from '../assets/background.png';
import flag from '../assets/flag.png';
import selectBox from '../assets/select.png';
import startBtn from '../assets/Start.png';

const Background = styled.div`
    display: flex;
    width: 100%;
    height: 100vh;
    background-image: url(${background});
    background-size: cover; /* 이미지를 뷰포트에 맞게 채우기 */
    background-position: center center; /* 가운데 정렬 */
`;

const FirstCon = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center; /* 가로 방향 중앙 정렬 */
    align-items: center; /* 세로 방향 중앙 정렬 */
    width: 100%;
    height: 100%; /* 부모 요소의 전체 높이 차지 */
    gap:10px;
`;

const FlagImg = styled.img`
    width: 100px;
    height: 100px;
`;

const Title =styled.p`
display: flex;
    font-family: Pretendard, sans-serif;
    color: #01ECFF;
    font-size: 60px;
    font-weight: 600;
`
const Description =styled.p`
    display: flex;
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
    font-size: 24px;
    font-weight: 300;`

const SelectBox =styled.img`
`
const StartBtn =styled.img`
`

const Main = () => {
    return (
        <Background>
            <FirstCon>
                <FlagImg src={flag} />
                <Title>Route Porter</Title>
                <Description>일정을 추가한 뒤 루트포터와 대화를 시작해보세요.</Description>
                <SelectBox src={selectBox} />
                <StartBtn src={startBtn}/>
            </FirstCon>
        </Background>
    );
}

export default Main;
