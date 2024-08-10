import React from 'react';
import styled from 'styled-components';
import Detail from '../../../assets/DetailFooter.png';
import Background from '../../../assets/Detail_background.png';

const FooterContainer = styled.div`
  margin-bottom: 1.25rem; 
  width: 100%;
  height: 21.75rem;
  padding: 1.25rem; 
  background: url(${Background}) no-repeat center center; 
  background-size: cover; 
  border-radius: 0.625rem; 
  color: #ffffff;
  text-align: left;
  position: relative; 
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
  padding: 0; 
`;

const Text = styled.p`
  margin: 0;
  font-size: 2rem;
  font-family: Pretendard, sans-serif;
  font-style: normal;
  font-weight: 600;
  color: #FFF;
  line-height: normal;
  max-width: calc(100% - 28.38rem); 
  white-space: pre-line; 
  display: flex;
  align-items: center;
`;

const Arrow = styled.span`
  font-size: 2rem;
  margin-left: 0.3rem; 
`;

const DetailImage = styled.img`
  height: auto;
  max-height: 100%;
  max-width: 100%;
  cursor: pointer; 
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
