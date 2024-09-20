import React, { useEffect } from 'react';
import styled from 'styled-components';
import Background from '../../assets/sea.png';

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
    script.src = `https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&callback=initMap&async=2`;
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

function MapSection({ destination, mapImage }) {
  console.log('MapSection rendered with destination:', destination);

  useEffect(() => {
    console.log('useEffect in MapSection triggered');
  }, [destination]);
  useEffect(() => {
    const geocodeAddress = async (address) => {
      const geocoder = new window.google.maps.Geocoder();

      return new Promise((resolve, reject) => {
        geocoder.geocode({ address: address }, (results, status) => {
          console.log('Geocode results:', results);  // Geocode 결과 확인
          if (status === 'OK' && results[0]) {
            const { lat, lng } = results[0].geometry.location;
            resolve({ lat: lat(), lng: lng() });
          } else {
            console.error('Geocode failed:', status);  // 에러 메시지 출력
            reject(new Error('Geocode was not successful: ' + status));
          }
        });
      });
    };

    loadGoogleMaps(async () => {
      try {
        console.log('Destination:', destination);  // destination 값 확인

        // Geocoding API를 사용해 좌표 얻기
        const location = await geocodeAddress(destination);

        // 좌표가 제대로 전달되었는지 확인
        console.log('Geocoded location:', location);

        const map = new window.google.maps.Map(document.getElementById('map'), {
          center: location,  // Geocoding API에서 얻은 좌표로 지도 중심 설정
          zoom: 12,
        });

        // 마커 추가
        new window.google.maps.Marker({
          position: location,
          map: map,
        });
      } catch (error) {
        console.error('Error loading map:', error);
      }
    });

    return () => {
      if (window.google && window.google.maps) {
        window.google.maps = undefined;
      }
    };
  }, [destination]);

  return (
    <MapSectionWrapper>
      <Title>📍{destination}는 여기에 위치해 있어요</Title>
      <MapSectionContainer>
        <LeftContainer>
          <ImageContainer src={mapImage} />
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
