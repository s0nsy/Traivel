import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Car from '../../assets/recommendation1.png';
import ArrowLeft from '../../assets/Arrow.png';
import ArrowRight from '../../assets/ArrowRight.png';

const RecommendationsWrapper = styled.div`
  width: 73.5rem;
`;

const Title = styled.h2`
  margin-bottom: 0.625rem;
  color: #ffffff;
`;

const RecommendationsContainer = styled.div`
  width: 73.5rem;
  height: 23rem;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 0.625rem;
  color: #ffffff;
  position: relative;
`;

const TextContainer = styled.div`
  position: absolute;
  top: 5.5rem;
  left: 13rem;
  padding-right: 2.06rem;
  max-width: calc(100% - 13.75rem);
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1.5rem;
  font-style: normal;
  font-weight: 400;
  line-height: 2rem;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
`;

const CarImage = styled.img`
  position: absolute;
  top: 2.75rem;
  left: 1.62rem;
  width: auto;
  height: auto;
  max-width: calc(62.94rem - 1.62rem);
  max-height: calc(21.75rem - 2.75rem - 8.25rem);
  object-fit: contain;
`;

const SmallText = styled.div`
  position: absolute;
  top: 2.5rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
`;

const SmallText2 = styled.div`
  position: absolute;
  top: 13rem;
  left: 1.62rem;
  color: #ffffff;
  font-family: Pretendard;
  font-size: 1.2rem;
  font-weight: 400;
`;

const RecommendationLinkBox = styled.div`
  display:flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 15.81rem;
  left: 2rem;
  padding: 1.75rem 2.06rem;
  border-radius: 0.75rem;
  border: 1px solid var(--Main_2, #01ECFF);
  color: var(--White, #FFF);
  font-family: Pretendard;
  font-size: 1rem;
  font-weight: 400;
  text-align: left;
  width: calc(100% - 4.06rem);
`;

const RecommendationLink = styled.a`
  display: block;
  margin-bottom: 0.5rem;
  color: #01ECFF;
  text-decoration: none;

  &:hover {
    text-decoration: underline;
  }
`;

const ArrowImageLeft = styled.img`
  position: absolute;
  top: 50%;
  left: -2rem;
  transform: translateY(-50%);
  cursor: pointer;
`;

const ArrowImageRight = styled.img`
  position: absolute;
  top: 50%;
  right: -2rem;
  transform: translateY(-50%);
  cursor: pointer;
`;



function Recommendations({ recommendation }) {
  const [tip, setTip] = useState("");
  const [links, setLinks] = useState([]);

  useEffect(() => {
    // 디버깅: 전달된 recommendation 데이터를 출력
    console.log('Received recommendation:', recommendation);

    // 파싱 함수
    const parseRecommendation = (recommendationString) => {
      // recommendationString을 섹션별로 분리
      const sections = recommendationString.split("[목록]");
      
      // tip 텍스트 추출
      const extractedTip = sections[0].replace("[교통수단]", "").trim();
  
      // Carlist 항목 추출
      const carlistRaw = sections[1].trim().split('\n'); // 줄바꿈 기준으로 분리
      const carlist = carlistRaw.map(item => {
        // 정규식을 사용하여 이름과 URL 추출
        const match = item.match(/^\d+\.\s*(.*)\s+\((http.*)\)$/);
        if (match) {
          // 이름과 URL 분리
          const name = match[1].replace(/\s*\(.*\)/, '').trim();
          const url = match[2];
          return { name, url };
        } else {
          return { name: item, url: '' };
        }
      });
      console.log('Parsed Carlist:', carlist);

      return { tip: extractedTip, Carlist: carlist };
    };
    

    if (recommendation.length > 0 && recommendation[0].description) {
      // 배열의 첫 번째 요소의 description 추출
      const recommendationString = recommendation[0].description;
      const { tip: extractedTip, Carlist } = parseRecommendation(recommendationString);
      setTip(extractedTip);
      setLinks(Carlist);
    }
  }, [recommendation]);
 
  return (
    <RecommendationsWrapper>
      <Title>🚘 이런 교통수단을 이용하면 더욱 편리해요</Title>
      <RecommendationsContainer>
        
        <SmallText>렌트카</SmallText>
        <CarImage src={Car}/>
        <SmallText2>추천 사이트</SmallText2>
        <TextContainer>{tip}</TextContainer>
        
        
        <RecommendationLinkBox>
          {links.map((link, index) => (
            <RecommendationLink 
              key={index} 
              href={link.url} 
              target="_blank" 
              rel="noopener noreferrer"
            >
              {link.name}
            </RecommendationLink>
          ))}
        </RecommendationLinkBox>
      </RecommendationsContainer>
    </RecommendationsWrapper>
  );
}

export default Recommendations;