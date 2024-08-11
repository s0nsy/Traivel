import React from 'react';
import styled from 'styled-components';
import Background from '../../../assets/Detail_background.png';

const MapSectionWrapper = styled.div`
  width: 100%;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem; 
  color: #ffffff;
`;

const MapSectionContainer = styled.div`
  margin-bottom: 1.25rem; 
  width: 100%;
  height: 30.9375rem;
  padding: 1.25rem; 
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem; 
  color: #ffffff;
  display: flex;
  gap: 1.25rem; 
`;

const LeftContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 30%; 
  gap: 0.625rem; 
`;

const ImageContainer = styled.div`
  flex: 2;
  background: url(${props => props.imageUrl}) no-repeat center center;
  background-size: cover;
  border-radius: 0.625rem; 
  height: 100%;
`;

const TextContainer = styled.div`
  flex: 1;
  background: rgba(0, 0, 0, 0.5); 
  padding: 0.625rem; 
  border-radius: 0.625rem; 
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
`;

const MapContainer = styled.div`
  flex: 1; 
  width: 70%;
  border-radius: 0.625rem; 
  background: url(${Background}) no-repeat center center;
  background-size: cover; /* Background 이미지를 넣음 */
`;

function MapSection() {
  return (
    <MapSectionWrapper>
      <Title><h2>📍서귀포시는 여기에 위치해 있어요</h2></Title>
      <MapSectionContainer>
        <LeftContainer>
          <ImageContainer imageUrl={Background} />
          <TextContainer>한 줄 설명</TextContainer>
        </LeftContainer>
        <MapContainer>
          {/* 여기에는 실제 지도를 넣으시면 됩니다 */}
        </MapContainer>
      </MapSectionContainer>
    </MapSectionWrapper>
  );
}

export default MapSection;
