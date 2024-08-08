import React from 'react';
import styled from 'styled-components';
import chattingbackground from "../../assets/chattingbackground.png";

const Background = styled.div`
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100vh;
    background-image: url(${chattingbackground});
    background-size: cover;
    background-position: center center;
    position: relative;
    background-color: white;
`;

const ListContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    flex: 1;
    overflow-y: auto;
    padding-top: 125px;
    position: relative;
    text-align: center;
`;

const Header1 = styled.div`
    color: #FFF;
    font-family: Pretendard;
    font-size: 36px;
    font-style: normal;
    font-weight: 500;
    line-height: normal;
    margin-bottom: 10px;
`;

const Subtitle1 = styled.div`
    color: #FFF;
    font-family: Pretendard;
    font-size: 24px;
    font-style: normal;
    font-weight: 300;
    line-height: normal;
    margin-bottom: 40px;
`;

const ListFrame = styled.div`
    display: flex;
    width: 1160px;
    height: 640px;
    padding: 16px 20px;
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    flex-shrink: 0;
    border-radius: 36px;
    border: 1px solid var(--Main_2, #01ECFF);
    background: rgba(220, 253, 255, 0.05);
    box-shadow: 0px 12px 12px 4px rgba(1, 236, 254, 0.20);
    position: relative;
    margin: 0 auto;
`;

const ListItem = styled.div`
    display: flex;
    align-items: center;
    width: 100%;
    background: rgba(0, 0, 0, 0);
    padding: 20px;
    border-radius: 28px;
    color: #FFF;
    transition: background-color 0.3s, border 0.3s;
    font-family: Pretendard;
    font-size: 24px;
    font-style: normal;
    font-weight: 600;
    line-height: normal;

    /* 초기에는 투명한 테두리 */
    border: 1px solid transparent;

    &:hover {
        border-color: #01ECFF; /* hover 시 테두리 색상 */
        background: rgba(255, 255, 255, 0.20);
        box-shadow: 0px 12px 12px 4px rgba(1, 236, 254, 0.20);
    }

    &:first-child {
        margin-top: 16px;
    }

    &:last-child {
        margin-bottom: 16px;
    }

    &:not(:first-child) {
        margin-top: 8px;
    }
`;

const ListIndex = styled.div`
    width: 44px;
    text-align: center;
`;

const ListDetails = styled.div`
    display: flex;
    align-items: center;
    width: 100%;
    gap: 28px;
`;

const ListDetailItem = styled.div`
    &:nth-child(2) {
        margin-left: 28px;
    }
    &:nth-child(3) {
        margin-left: 78px;
    }
`;



const List = () => {
    return (
        <Background>
            <ListContainer>
                <Header1>추천 여행지 리스트를 알려드려요.</Header1>
                <Subtitle1>날짜와 일정을 추가한 뒤, 루트포터와 대화를 시작해보세요.</Subtitle1>
                <ListFrame>
                    {Array.from({ length: 7 }, (_, index) => (
                        <ListItem key={index}>
                            <ListIndex>{index + 1}</ListIndex>
                            <ListDetails>
                                <div>제주도</div>
                                <ListDetailItem>서귀포</ListDetailItem>
                                <ListDetailItem>메인 키워드 | 메모</ListDetailItem>
                            </ListDetails>
                        </ListItem>
                    ))}
                </ListFrame>
            </ListContainer>
        </Background>
    );
}

export default List;


