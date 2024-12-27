import React, { useState } from "react";
import styled from "styled-components";
import SideBarIcon from "../../assets/ic_round-menu.png";
import { useNavigate } from "react-router-dom";
import {useSelector} from 'react-redux';

const NavContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 72px;
  z-index: 1000;
  background: linear-gradient(0deg, #013f44 -27.78%, #011e20 37.5%);
`;

const NavLogo = styled.p`
  position: absolute;
  top: 50%;
  left: 20%;  
  transform: translateY(-50%);
  font-family: Pretendard, sans-serif;
  color: #01ecff;
  font-size: 24px;
  font-weight: 600;
  cursor: pointer;
`;

const NavSelectCon = styled.div`
  position: absolute;
  top: 50%;
  left: 45%;
  transform: translate(-50%, -50%); 
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px 21px;
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.2) 0%, rgba(153, 153, 153, 0.2) 100%);
  border-radius: 20px;
  gap: 10px;
  white-space: nowrap;
`;

const NavSelect = styled.p`
  font-family: Pretendard, sans-serif;
  color: #ffffff;
  font-size: 0.9rem;
`;

const SideRectangle = styled.div`
  width: 2px;
  height: 20px;
  background-color: #ffffff;
  margin: 0 10px;
`;

const SideBarBtn = styled.img`
  position: absolute;
  top: 50%;
  right: 20%;  /* 사이드바 버튼을 오른쪽에 고정된 위치로 설정 */
  transform: translateY(-50%);
  width: 30px;
  height: 30px;
  cursor: pointer;
`;

const Header = () => {
  const navigate = useNavigate();
  const adults = useSelector((state) => state.survey.groupComposition.adults);
  const children = useSelector((state) => state.survey.groupComposition.children);
  const infants = useSelector((state) => state.survey.groupComposition.infants);
  const schedule = useSelector((state) => state.survey.schedule);

  const selectedPeople = adults || children || infants
    ? `${adults+children+infants}명`
    : "인원 추가";

  const handleLogo = () => {
    navigate("/");
  };
  const selectedSchedule = schedule ? schedule : "날짜 선택";

  

  return (
    <NavContainer>
      <NavLogo onClick={handleLogo}>Route Porter</NavLogo>
      <NavSelectCon>
        <NavSelect>{selectedSchedule}</NavSelect>
        <SideRectangle />
        <NavSelect>{selectedPeople}</NavSelect>
      </NavSelectCon>
      
    </NavContainer>
  );
};

export default Header;