import React, { useState, useEffect } from "react";
import { Oval } from "react-loader-spinner";
import styled from "styled-components";
import cat1 from "../assets/firstCat.png";
import cat2 from "../assets/secondCat.png";
import cat3 from "../assets/thirdCat.png";

// 로더를 감싸는 컨테이너 스타일
const LoaderWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
`;

const TextWrapper = styled.div`
  margin-top: 20px;
  font-family: Pretendard;
  font-size: 36px;
  font-weight: 500;
  line-height: 42.96px;
  color: white;
  text-align: center;
`;

const ImageWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  gap: 20px;
`;

const CatImage = styled.img`
  width: 100px;
  height: 100px;
`;

const Loader = () => {
  const [step, setStep] = useState(0);

  const texts = [
    "선택하신 지역의 정보를 찾고 있어요.",
    "조금만 기다려주세요!",
    "거의 완성되었어요!",
  ];

  const images = [cat1, cat2, cat3];  // 가져온 고양이 이미지들을 배열에 추가

  useEffect(() => {
    const interval = setInterval(() => {
      setStep((prevStep) => (prevStep + 1) % 3); // 0 -> 1 -> 2 -> 0 순환
    }, 2000); // 2초마다 변경

    return () => clearInterval(interval); // 컴포넌트 언마운트 시 인터벌 제거
  }, []);

  return (
    <LoaderWrapper>
      <Oval
        height={160}  // 로더의 높이
        width={160}   // 로더의 너비
        color="#33C1B8"  // 로더 색상
        secondaryColor="#012A2E"  // 이중 색상 설정
        ariaLabel="loading-indicator"
        strokeWidth={8} // 로더의 굵기 조정
      />
      <TextWrapper>{texts[step]}</TextWrapper>
      <ImageWrapper>
        {images.slice(0, step + 1).map((image, index) => (
          <CatImage key={index} src={image} alt={`Cat ${index + 1}`} />
        ))}
      </ImageWrapper>
    </LoaderWrapper>
  );
};

export default Loader;
