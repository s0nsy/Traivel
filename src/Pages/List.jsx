import React, { useState, useEffect } from "react";
import styled from "styled-components";
import chattingbackground from "../assets/chattingbackground.png";
import movetochat from "../assets/movetochat.svg";
import listarrow from "../assets/listarrow.svg";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { setSelectedItem } from "../store/selectedItemSlice";
import { setRecommendations } from '../store/surveySlice';

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
  color: #fff;
  font-family: Pretendard;
  font-size: 36px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
  margin-bottom: 10px;
`;

const Subtitle1 = styled.div`
  color: #fff;
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
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(220, 253, 255, 0.05);
  box-shadow: 0px 12px 12px 4px rgba(1, 236, 254, 0.2);
  position: relative;
  margin: 0 auto;
`;

const ListItem = styled.button`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 1120px;
  height: 80px;
  background: rgba(0, 0, 0, 0);
  padding: 26px 20px;
  color: #fff;
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
    border-color: #01ecff;
    background: rgba(255, 255, 255, 0.2);
    box-shadow: 0px 12px 12px 4px rgba(1, 236, 254, 0.2);
  }
`;

const ListIndex = styled.div`
  width: 44px;
  text-align: left;
`;

const ListDetails = styled.div`
  display: flex;
  position: relative;
  width: 100%;
  align-items: left;
  text-align: left;
`;

const ListDetailItem = styled.div`
  &:nth-child(1) {
    margin-left: 28px;
    max-width: 500px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  &:nth-child(2) {
    position: absolute;
    left: 228px;  
    white-space: nowrap;
  }
  &:nth-child(3) {
    position: absolute;
    left: 373px; 
    white-space: nowrap;
  }
`;


const ArrowIcon = styled.img`
  width: 40px;
  height: 40px;
  display: ${({ isVisible }) => (isVisible ? "block" : "none")};
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
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  margin-top: 40px;
  cursor: pointer;
`;

const FooterContent = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  color: var(--White, #fff);
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
    const navigate = useNavigate();
    const dispatch = useDispatch();
    
    useEffect(() => {
      const storedRecommendations = localStorage.getItem('recommendations');
      if (storedRecommendations) {
        const parsedData = JSON.parse(storedRecommendations);
        
        if (parsedData && parsedData.data && Array.isArray(parsedData.data)) {
          dispatch(setRecommendations(parsedData.data));
        }
      }
    }, [dispatch]);
  
    // Redux store에서 recommendations 가져오기
    const recommendations = useSelector((state) => state.survey.recommendations);
    const recommendationsArray = Array.isArray(recommendations) ? recommendations : [];
    
    console.log("Recommendations in List:", recommendationsArray);
  
    const [hoveredIndex, setHoveredIndex] = useState(null);
    
    const chatNavigate = () => {
      navigate("/chat");
    };
  
    const detailNavigate = () => {
      navigate("/detail");
    };
  
    const handleItemClick = (item) => {
      dispatch(setSelectedItem(item));
    };
  
    return (
      <Background>
        <ListContainer>
          <Header1>추천 여행지 리스트를 알려드려요.</Header1>
          <Subtitle1>
            날짜와 일정을 추가한 뒤, 루트포터와 대화를 시작해보세요.
          </Subtitle1>
          <ListFrame>
            {recommendationsArray.length > 0 ? (
              recommendationsArray.map((item, index) => (
                <ListItem
                  key={index}
                  onMouseOver={() => setHoveredIndex(index)}
                  onMouseOut={() => setHoveredIndex(null)}
                >
                  <div style={{ display: "flex", alignItems: "center" }}>
                    <ListIndex>{index + 1}</ListIndex>
                    <ListDetails>
                      <ListDetailItem>{item.region || 'N/A'}</ListDetailItem>
                      <ListDetailItem>{item.district || 'N/A'}</ListDetailItem>
                      <ListDetailItem>{item.features ? item.features.join(", ") : 'N/A'}</ListDetailItem>
                    </ListDetails>
                  </div>
                  <ArrowIcon
                    src={listarrow}
                    isVisible={hoveredIndex === index}
                    onClick={() => {
                      detailNavigate();
                      handleItemClick(item);
                    }}
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

