import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import chattingbackground from "../assets/chattingbackground.png";
import movetochat from "../assets/movetochat.svg";
import listarrow from "../assets/listarrow.svg";
import { useNavigate, useLocation } from 'react-router-dom';

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

const ListItem = styled.button`
    display: flex;
    justify-content: space-between; // ListDetails은 왼쪽, ArrowIcon은 오른쪽. 
    align-items: center;  
    width: 1120px;
    height:80px;
    background: rgba(0, 0, 0, 0);
    padding: 26px 20px;
    color: #FFF;
    transition: background-color 0.3s, border 0.3s, box-shadow 0.3s;
    font-family: Pretendard;
    font-size: 24px;
    font-style: normal;
    font-weight: 600;
    line-height: normal;
    border: 1px solid transparent;
    box-sizing: border-box;
    border-radius: 28px;

    &:hover {
        border-color: #01ECFF;
        border-radius: 28px;
        background: rgba(255, 255, 255, 0.20);
        box-shadow: 0px 12px 12px 4px rgba(1, 236, 254, 0.20);
    }
`;

const ListIndex = styled.div`
    width: 44px;
    text-align: left;
`;

const ListDetails = styled.div`
    display: flex;
    align-items: left;
    text-align: left;
    width: 100%;
`;

const ListDetailItem = styled.div` 
    &:nth-child(1) {
        margin-left: 28px;
    }
    &:nth-child(2) {
        margin-left: 59px;
    }
    &:nth-child(3) {
        margin-left: 100px;
    }
`;

const ArrowIcon = styled.img`
    width: 40px;
    height: 40px;
    display: ${({ isVisible }) => (isVisible ? 'block' : 'none')};
    cursor: pointer;
`;

const ListFooter = styled.div`
    display: flex;
    width: 1160px;
    height: 73px;
    padding: 19px 0px 18px 0px;
    justify-content: center;
    align-items: center;
    border-radius: 36px;
    border: 1px solid var(--Main_2, #01ECFF);
    background: rgba(255, 255, 255, 0.30);
    box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.40);
    margin-top: 40px;
    cursor: pointer;
`;

const FooterContent = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px; 
    color: var(--White, #FFF);
    text-align: center;
    font-family: Pretendard;
    font-size: 24px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
`;

const MoveToChatIcon = styled.img`
    width: 36px;
    height: 36px;
`;
function List() {
    const location = useLocation();
    const navigate = useNavigate();
    
    const [recommendations, setRecommendations] = useState(() => {
        const savedData = localStorage.getItem('recommendations');
        return savedData ? JSON.parse(savedData) : [];
    });

    useEffect(() => {
        if (location.state?.recommendations?.data) {
            const data = location.state.recommendations.data;
            console.log('Received recommendations:', data);
            setRecommendations(data);
            localStorage.setItem('recommendations', JSON.stringify(data));
        }
    }, [location.state]);

    useEffect(() => {
        if (recommendations.length === 0) {
            console.log('No recommendations available.');
        }
    }, [recommendations]);

    const [hoveredIndex, setHoveredIndex] = useState(null); 

    const chatNavigate = () => {
        navigate("/chat");
    };

    const detailNavigate = () => {
        navigate("/detail");
    };

    return (
        <Background>
            <ListContainer>
                <Header1>추천 여행지 리스트를 알려드려요.</Header1>
                <Subtitle1>날짜와 일정을 추가한 뒤, 루트포터와 대화를 시작해보세요.</Subtitle1>
                <ListFrame>
                    {recommendations.length > 0 ? (
                        recommendations.map((item, index) => (
                            <ListItem 
                                key={index}
                                onMouseOver={() => setHoveredIndex(index)}
                                onMouseOut={() => setHoveredIndex(null)}
                            >
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <ListIndex>{index + 1}</ListIndex>
                                    <ListDetails>
                                        <ListDetailItem>{item.region}</ListDetailItem>
                                        <ListDetailItem>{item.district}</ListDetailItem>
                                        <ListDetailItem>{item.features.join(', ')}</ListDetailItem>
                                    </ListDetails>
                                </div>
                                <ArrowIcon 
                                    src={listarrow} 
                                    isVisible={hoveredIndex === index} 
                                    onClick={detailNavigate} 
                                />
                            </ListItem>
                        ))
                    ) : (
                        <p>No recommendations available.</p>
                    )}
                </ListFrame>
                
                <ListFooter onClick={chatNavigate}>
                    <FooterContent>
                        <MoveToChatIcon src={movetochat} alt="Move to Chat" />
                        <div>채팅으로 돌아가기</div>
                    </FooterContent>
                </ListFooter>
            </ListContainer>
        </Background>
    );
}

export default List;