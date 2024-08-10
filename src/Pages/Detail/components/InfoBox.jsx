import React from 'react';
import styled from 'styled-components';
import InfoBox1 from '../../../assets/InfoBox1.png';
import InfoBox2 from '../../../assets/InfoBox2.png';
import DetailBackground from '../../../assets/Detail_background.png'; // 배경 이미지 추가

const InfoBoxContainer = styled.div`
  width: 73.75rem;
  height: 17.5rem;
  flex-shrink: 0;
  background: url(${DetailBackground}) no-repeat center center; /* 배경 이미지 설정 */
  background-size: cover; /* 배경 이미지 크기 조정 */
  position: relative;
  display: flex;
  align-items: center;
  padding-left: 3.75rem;
`;

const Image = styled.img`
  width: 5rem;
  height: 5rem;
  margin-right: 1rem; /* 이미지와 텍스트 사이의 간격 */
`;

const PositionedImage = styled.img`
  position: absolute;
  top: 5.38rem;
  left: 51.75rem;
  width: auto;
  height: auto;
  max-width: 20rem; /* 이미지 최대 너비 키움 */
  max-height: 9rem; /* 이미지 최대 높이 키움 */
  cursor: pointer; /* 마우스 포인터를 클릭 가능한 형태로 변경 */
`;

const StyledH2 = styled.h2`
  color: #ffffff;
  margin: 0; /* 기본 여백 제거 */
  padding: 0; /* 텍스트와 이미지 사이의 간격 제거 */
`;

function InfoBox() {
  const handleImageClick = () => {
    // 여기에 원하는 동작을 추가하세요. 예를 들어, 새로운 페이지로 이동하거나, 팝업을 띄우는 동작 등.
    window.open('https://example.com', '_blank'); // 새 탭에서 링크 열기
  };

  return (
    <InfoBoxContainer>
      <Image src={InfoBox1} alt="여행 이미지" />
      <StyledH2>
        루트 포터가 짜주는<br /> 2박 3일 서귀포 여행 일정<br /> 1초만에 보러가기
      </StyledH2>
      <PositionedImage src={InfoBox2} alt="추가 이미지" onClick={handleImageClick} />
    </InfoBoxContainer>
  );
}

export default InfoBox;
