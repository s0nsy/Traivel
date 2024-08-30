import React from "react";
import styled from "styled-components";

const ErrorContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  color: #fff;
`;

const ErrorMessage = styled.div`
  font-size: 24px;
  font-weight: bold;
`;

function Error({ message = "알 수 없는 오류가 발생했습니다." }) {
  return (
    <ErrorContainer>
      <ErrorMessage>{message}</ErrorMessage>
      <p>서버와의 연결에 문제가 발생했습니다. 나중에 다시 시도해 주세요.</p>
    </ErrorContainer>
  );
}

export default Error;