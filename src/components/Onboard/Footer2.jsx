import React from "react";
import styled from "styled-components";
import buttonImg from "../../assets/button.png";

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
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  border-radius: 36px;
`;

const Guide = styled.div`
  color: #e6e6e6;
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  left: 32px; 
`;

const Button = styled.img`
  position: absolute;
  right: 27px; 
  cursor: pointer;
  z-index: 2;
`;

function Footer2({ isVisible }) {
  return (
    <FooterContainer isVisible={isVisible}>
      <Guide>원하는 여행 테마를 잘 답변하려면?</Guide>
      <Button src={buttonImg} alt="button" />
    </FooterContainer>
  );
}

export default Footer2;
