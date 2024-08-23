import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useNavigate } from 'react-router-dom'; 
import InfoBox1 from '../../assets/InfoBox1.png';
import InfoBox2 from '../../assets/InfoBox2.png';
import DetailBackground from '../../assets/Detail_background.png'; 

const InfoBoxContainer = styled.div`
  width: 73.75rem;
  height: 17.5rem;
  flex-shrink: 0;
  background: url(${DetailBackground}) no-repeat center center; 
  background-size: cover;
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 3.75rem;
`;

const Image = styled.img`
  width: 5rem;
  height: 5rem;
  margin-right: 1rem; 
`;

const PositionedImage = styled.img`
  position: absolute;
  top: 5.38rem;
  left: 51.75rem;
  width: auto;
  height: auto;
  max-width: 20rem; 
  max-height: 9rem; 
  cursor: pointer; 
`;

const StyledH2 = styled.h2`
  color: #ffffff;
  margin: 0; 
  padding: 0; 
`;

function InfoBox({ destination }) {
  const navigate = useNavigate(); 

  const handleImageClick = () => {

    navigate('/output'); 

  };

  return (
    <InfoBoxContainer>
      <Image src={InfoBox1} alt="여행 이미지" />
      <StyledH2>
        루트 포터가 짜주는<br /> 2박 3일 {destination} 여행 일정<br /> 1초만에 보러가기
      </StyledH2>
      <PositionedImage src={InfoBox2} alt="추가 이미지" onClick={handleImageClick} />
    </InfoBoxContainer>
  );
}

export default InfoBox;
