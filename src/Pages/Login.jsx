import React from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Button = styled.button`
  font-size: 100px;
  margin-top: 200px;
  cursor: pointer;
`;

function Login() {
  const navigate = useNavigate();
  const PROXY = "/api";

  const Integration = async () => {
    try {
      
      const response = await axios.get(`${PROXY}/oauth`, {
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      });

      console.log(response.data);

     
      navigate("/chat");
    } catch (error) {
      console.error("API 호출 중 오류 발생:", error);
    }
  };

  return (
    <div>
      <Button onClick={Integration}>버어튼</Button>
    </div>
  );
}

export default Login;
