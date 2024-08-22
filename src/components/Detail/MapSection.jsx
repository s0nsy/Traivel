import React, { useEffect } from 'react';
import styled from 'styled-components';
import Background from '../../assets/Detail_background.png';

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
  background: url(${Background}) no-repeat center center;
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
  background-size: cover;
`;

function loadGoogleMaps(callback) {
  const existingScript = document.querySelector(`script[src*="https://maps.googleapis.com/maps/api/js"]`);

  if (!existingScript) {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=AIzaSyAWaI-Y8lIURXjytBxx7ksV8PBFW6aRsfk&callback=initMap&async=2`;
    script.async = true;
    script.defer = true;

    window.initMap = callback;

    document.head.appendChild(script);

    script.onload = () => {
      if (typeof window.google === 'object' && typeof window.google.maps === 'object') {
        callback();
      }
    };
  } else if (typeof window.google === 'object' && typeof window.google.maps === 'object') {
    callback();
  }
}

function MapSection() {
  useEffect(() => {
    loadGoogleMaps(() => {
      const map = new window.google.maps.Map(document.getElementById('map'), {
        center: { lat: 33.253, lng: 126.561 },
        zoom: 12,
      });
    });

    return () => {
      if (window.google && window.google.maps) {
        window.google.maps = undefined;
      }
    };
  }, []);

  return (
    <MapSectionWrapper>
      <Title>📍서귀포시는 여기에 위치해 있어요</Title>
      <MapSectionContainer>
        <LeftContainer>
          <ImageContainer />
          <TextContainer>한 줄 설명</TextContainer>
        </LeftContainer>
        <MapContainer id="map">
          {/* 여기에는 실제 지도가 표시됩니다 */}
        </MapContainer>
      </MapSectionContainer>
    </MapSectionWrapper>
  );
}

export default MapSection;
