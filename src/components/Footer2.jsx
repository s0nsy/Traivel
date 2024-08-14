import React from "react";
import styled from "styled-components";
import buttonImg from "../assets/button.png";

const FooterContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  position: relative;
  top:100px;
  width: 100%; 
`;

const Guide = styled.div`
  display: flex;
  width: 730px;
  height: 62px;
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
  font-size: 17px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  text-indent: -330px;
`;

const Button = styled.img`
  cursor: pointer;
  z-index: +2;
  position: absolute;
  margin-left: 650px;
`;

function Footer2() {
  return (
    <FooterContainer>
      <Guide>원하는 여행 테마를 잘 답변하려면?</Guide>
      <Button src={buttonImg}></Button>
    </FooterContainer>
  );
}

export default Footer2;
