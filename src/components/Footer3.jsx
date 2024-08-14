import React from "react";
import styled from "styled-components";
import button2 from "../assets/button2.png";
import { useNavigate } from "react-router-dom";

const FooterContainer = styled.div`
  display: flex;
  padding: 16px 380px 10px 380px;
  justify-content: center;
  align-items: center;
  position: relative;
  top:100px;
  cursor: pointer;
  
`;

const Start = styled.div`
  display: flex;
  width: 730px;
  height: 62px;
  padding: 0 100px 0 90px;
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
`;
const Button = styled.img`
  cursor: pointer;
  z-index: +2;
  position: absolute;
  margin-left: 650px;
`;

function Footer3() {
  const navigate = useNavigate();


  return (
    <FooterContainer onClick={()=>{
      navigate("/chat");
    }}>
      <Start>시작하기</Start>
      <Button src={button2}></Button>
    </FooterContainer>
  );
}

export default Footer3;
