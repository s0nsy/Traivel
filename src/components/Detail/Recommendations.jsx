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
  display: block;
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

    // 데이터가 있는 경우에만 처리
    if (recommendation) {
      // "[목록]"을 기준으로 데이터 분리
      const parts = recommendation.split("[목록]");
      const tipPart = parts[0]?.trim() || ""; // 교통수단 추천 텍스트
      const linksPart = parts[1] ? parts[1].split("\n").filter((line) => line.trim() !== "") : []; // URL 목록

      // 디버깅: 분리된 tip과 links 데이터를 출력
      console.log('Parsed tip:', tipPart);
      console.log('Parsed links:', linksPart);

      setTip(tipPart);
      setLinks(linksPart);
    }
  }, [recommendation]);

  return (
    <RecommendationsWrapper>
      <Title>🚘 교통수단 추천</Title>
      <RecommendationsContainer>
        {/* 교통수단 추천 TIP */}
        <TextContainer>{tip}</TextContainer>

        {/* 교통수단 목록 */}
        <RecommendationLinkBox>
          {links.map((link, index) => (
            <RecommendationLink key={index} href={link} target="_blank" rel="noopener noreferrer">
              {link}
            </RecommendationLink>
          ))}
        </RecommendationLinkBox>
      </RecommendationsContainer>
    </RecommendationsWrapper>
  );
}

export default Recommendations;