import React from "react";
import styled from "styled-components";
import buttonImg from "../assets/button.png";

const FooterContainer = styled.div`
  display: flex;
  padding: 16px 380px 10px 380px;
  justify-content: center;
  align-items: center;
  position: relative;
`;

const Input = styled.input`
  display: flex;
  width: 700px;
  height: 62px;
  padding: 0 100px 0 40px;
  justify-content: flex-end;
  align-items: center;
  gap: 210px;
  flex-shrink: 0;
  border-radius: 36px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0px 4px 12px 2px rgba(1, 236, 255, 0.4);
  &::placeholder {
    color: #e6e6e6;
    font-family: Pretendard;
    font-size: 17px;
    text-align: center;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    text-indent: -430px;
  }
`;

const Button = styled.img`
  cursor: pointer;
  z-index: +2;
  position: absolute;
  margin-left: 770px;
`;

function Footer2() {
  return (
    <FooterContainer>
      <Input placeholder="원하는 여행 테마를 잘 답변하려면?" />
      <Button src={buttonImg}></Button>
    </FooterContainer>
  );
}

export default Footer2;
