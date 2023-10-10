import React from "react";
import YouTube from "react-youtube";
import styled from "styled-components";

const Wrapper = styled.div`
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const MainWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 1188px;
  height: 657px;
  flex-shrink: 0;
  background: #b1d9db;
  box-shadow: 10px 72px 80px -48px #223a28;
`;

function Lecture() {
  const opts = {
    height: "390",
    width: "640",

    playerVars: {
      autoplay: 1,
    },
  };
  return (
    <Wrapper>
      <MainWrapper>
        <YouTube videoId="0bqfTzpWySY" opts={opts} />
      </MainWrapper>
    </Wrapper>
  );
}

export default Lecture;
