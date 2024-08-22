import React from "react";
import styled  from "styled-components";
import button2 from "../../assets/button.png";
import { useNavigate } from "react-router-dom";

const FooterContainer = styled.div`
  display: ${({ isVisible }) => (isVisible ? "flex" : "none")};
  padding: 16px 380px 10px 380px;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 952px;
  left: 50%;
  transform: translateX(-50%);
  cursor: pointer;
  z-index: 1000;
  width: 1160px;
  height: 72px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  color: #e6e6e6;
  border-radius: 36px;
`;

const Start = styled.div`
  color: #e6e6e6;
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  left: 32px; 
`;

const Button = styled.img`
  cursor: pointer;
  position: absolute;
  right: 27px; 
`;

function Footer3({ isVisible }) {
  const navigate = useNavigate();

  return (
    <FooterContainer isVisible={isVisible} onClick={() => navigate("/chat")}>
      <Start>시작하기</Start>
      <Button src={button2} alt="start" />
    </FooterContainer>
  );
}

export default Footer3;
