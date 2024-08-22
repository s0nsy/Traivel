import React from "react";
import styled from "styled-components";
import button2 from "../../assets/button2.png";

const FooterContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  position: absolute; 
  top: 952px;
  left: 50%; 
  transform: translateX(-50%); 
  cursor: pointer;
  z-index: 1000;
  width: 1160px;
  height:72px;
`;

const Plan = styled.div`
  display: flex;
  width: 1160px;
  height:72px;
  padding: 0 100px 0 75px;
  justify-content: center;
  align-items: center;
  gap: 210px;
  flex-shrink: 0;
  border-radius: 36px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  color: #e6e6e6;
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
`;

const Button = styled.img`
  cursor: pointer;
  position: absolute;
  left: 1746px;  
  top: 952px;   
  z-index: 2;
`;

function Footer() {
  return (
    <FooterContainer>
      <Plan>루트포터의 질문에 답하고 완벽한 여행 계획을 세워보세요.</Plan>
      <Button src={button2} />
    </FooterContainer>
  );
}

export default Footer;
