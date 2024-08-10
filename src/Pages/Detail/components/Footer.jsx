import React from 'react';
import styled from 'styled-components';
import Detail from '../../../assets/DetailFooter.png';
import Background from '../../../assets/Detail_background.png';

const FooterContainer = styled.div`
  margin-bottom: 1.25rem; /* 20px -> 1.25rem */
  width: 100%;
  height: 21.75rem;
  padding: 1.25rem; /* 20px -> 1.25rem */
  background: url(${Background}) no-repeat center center; /* Background 이미지 설정 */
  background-size: cover; /* 배경 이미지 크기를 컨테이너에 맞춤 */
  border-radius: 0.625rem; /* 10px -> 0.625rem */
  color: #ffffff;
  text-align: left;
  position: relative; /* 내부 박스를 절대 위치로 배치하기 위해 설정 */
`;

const InnerBox = styled.div`
  position: absolute;
  top: 5.56rem;
  left: 7.31rem;
  right: 6.56rem;
  bottom: 5.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0; /* 내부 패딩 제거 */
`;

const Text = styled.p`
  margin: 0;
  font-size: 2rem;
  font-family: Pretendard, sans-serif;
  font-style: normal;
  font-weight: 600;
  color: #FFF;
  line-height: normal;
  max-width: calc(100% - 28.38rem); /* 텍스트와 이미지 사이의 간격을 28.38rem로 설정 */
  white-space: pre-line; /* 텍스트가 줄 바꿈을 할 수 있도록 설정 */
  display: flex;
  align-items: center;
`;

const Arrow = styled.span`
  font-size: 2rem;
  margin-left: 0.3rem; /* 약간의 간격을 두어 텍스트와 화살표를 분리 */
`;

const DetailImage = styled.img`
  height: auto;
  max-height: 100%;
  max-width: 100%;
  cursor: pointer; /* 이미지를 클릭 가능하게 시각적으로 표시 */
`;

function Footer() {
  const handleImageClick = () => {
    // 여기서 원하는 동작을 수행할 수 있습니다.
    window.location.href = 'https://example.com'; // 예시: 클릭 시 다른 페이지로 이동
  };

  return (
    <FooterContainer>
      <InnerBox>
        <Text>
          다른 여행지를 추천받으러{'\n'}채팅창으로 돌아가기 →
        </Text>
        <DetailImage src={Detail} alt="Detail 이미지" onClick={handleImageClick} />
      </InnerBox>
    </FooterContainer>
  );
}

export default Footer;
