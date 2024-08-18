import React from "react";
import { DayData } from "./DayData";
import styled from "styled-components";
import Circle from "../assets/circle.png";
import ShortLine from "../assets/Frame 2085666344.png";
import Icon from "../assets/travel-icon.png";



const TotalContainer = styled.div`
`;
const DayContainer = styled.div``;

const DayDate = styled.div`
  margin: 20px 0 10px 0;
  margin-left: 350px;
  color: #fff;
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 36px; /* 112.5% */
`;

const DayFrame = styled.div`
  margin-left: 350px;
  width: 1000px;
  flex-shrink: 0;
  border-radius: 12px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  padding: 20px;
`;

const PlanContainer = styled.div`
  color: #fff;
  display: flex;
  font-family: Pretendard;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
`;

const PlanImg1 = styled.img`
  margin: 20px 10px;
  width: 12px;
  height: 12px;
`;

const PlanTitle = styled.div`
  color: #fff;
  margin: 10px 0;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const PlanImg2 = styled.img`
  width: 3px;
  height: 28px;
  flex-shrink: 0;
  margin: 10px 6px;
`;

const PlanDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  margin: 10px 0;
  font-weight: 200;
  line-height: 28px; /* 116.667% */
`;

const TipContainer = styled.div`
  margin: 20px 0 0 350px;
  color: #fff;
`;

const TipFrame = styled.div`
  margin-top: 10px;
  width: 1000px;
  flex-shrink: 0;
  border-radius: 12px;
  border: 1px solid var(--Main_2, #01ecff);
  background: rgba(255, 255, 255, 0.05);
  padding: 20px;
`;

const TipSubHeader = styled.div`
  margin: 15px 0;
`;
const TipHeader = styled.div`
  display: flex;
  font-family: Pretendard;
  font-size: 18px;
  font-style: normal;
  font-weight: 600;
  line-height: 28px; /* 116.667% */
`;

const TipTitle = styled.div`
  font-family: Pretendard;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 116.667% */
`;

const TipImg = styled.img`
  width: 20px;
  height: 20px;
  margin: 6px 4px;
`;

const TipSubTitle = styled.div`
  color: #fff;

  font-family: Pretendard;
  font-size: 17px;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
`;
const TipDetail = styled.div`
  color: #fff;
  font-family: Pretendard;
  font-size: 16px;
  font-style: normal;
  font-weight: 200;
  line-height: normal;
`;

function DayLists() {
  return (
    <TotalContainer>
      {DayData.map((d, i) =>
        d.date === "여행 Tip" ? (
          <TipContainer>
            <TipHeader>
              <TipTitle>여행 Tip</TipTitle>
              <TipImg src={Icon}></TipImg>
            </TipHeader>
            <TipFrame>
              {d.plan.map((t) => (
                <TipSubHeader>
                  <TipSubTitle>{t.title}</TipSubTitle>
                  <TipDetail>{t.detail}</TipDetail>
                </TipSubHeader>
              ))}
            </TipFrame>
          </TipContainer>
        ) : (
          <DayContainer>
            <DayDate>{d.date}</DayDate>
            <DayFrame>
              {d.plan.map((p, index) => (
                <PlanContainer key={index}>
                  <PlanImg1 src={Circle} alt="circle" />
                  <PlanTitle>{p.title}</PlanTitle>
                  <PlanImg2 src={ShortLine} alt="shortline" />
                  <PlanDetail>{p.detail}</PlanDetail>
                </PlanContainer>
              ))}
            </DayFrame>
          </DayContainer>
        )
      )}
    </TotalContainer>
  );
}

export default DayLists;
