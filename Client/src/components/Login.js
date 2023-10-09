import styled from "styled-components";
import robot from "../assets/robot.png";
import loginBI from "../assets/background.png";
import apple from "../assets/apple.png";
import google from "../assets/google.png";
import { Link } from "react-router-dom";
import { useState } from "react";
import OffBtn from "../assets/off.png";
const Wrapper = styled.div`
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const MainWrapper = styled.div`
  position: relative;

  width: 1188px;
  height: 657px;
  flex-shrink: 0;
  background: #b1d9db;
  box-shadow: 10px 72px 80px -48px #223a28;
`;

const LoginWrapper = styled.div`
  display: flex;
  flex-direction: column;

  position: relative;
  width: 609px;
  height: 657px;
  flex-shrink: 0;
  border-radius: 0px 37px 37px 0px;
  background: #fff;
`;

const RobotImg = styled.div`
  position: absolute;
  left: 498px;
  top: 28px;
  background-image: url(${robot});
  z-index: 2;
  width: 345px;
  height: 375px;
`;
const LoginBackgroundImg = styled.div`
  position: absolute;
  left: 608px;
  bottom: 0px;
  width: 580px;
  height: 630px;
  background: url(${loginBI});
  overflow: hidden;
`;

const AppTitle = styled.span`
  margin: 72px 0 0 144px;
  color: rgba(0, 0, 0, 0.8);
  font-family: Poppins;
  font-size: 32px;
  font-style: normal;
  font-weight: 600;
  line-height: 100%; /* 32px */
`;
const AppName = styled.span`
  display: flex;
  margin-top: 25px;
  margin-left: 80px;
  color: rgba(0, 0, 0, 0.7);
  font-family: Poppins;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 150%; /* 36px */
`;

const ExplanAccount = styled.span`
  color: rgba(63, 64, 64, 0.8);
  font-family: Poppins;
  font-size: 16px;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 16px */
  margin-left: 80px;
  margin-top: 17px;
`;

const InputList = styled.div`
  margin-top: 32px;
  margin-left: 80px;
  display: flex;
  flex-direction: column;
`;
const Input = styled.input`
  color: rgba(0, 0, 0, 0.7);
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  font-weight: 400;
  line-height: 24px; /* 150% */
  padding: 12px;
  margin-top: 8px;
  margin-bottom: 24px;
  margin-right: 106px;
  align-items: center;
  border-radius: 8px;
  border: 1px solid #7cd2d7;
  background: #fff;
`;

const EyeOff = styled.button`
  position: absolute;
  width: 24px;
  height: 24px;
  top: 370px;
  left: 462px;
  border: none;
  background: url(${OffBtn});
`;

const RememberSpan = styled.span`
  color: rgba(0, 0, 0, 0.7);
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  margin-left: 10px;
  font-weight: 400;
  line-height: 100%; /* 16px */
`;

const ForgotSpan = styled.span`
  color: rgba(0, 0, 0, 0.5);
  text-align: right;
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  font-weight: 400;
  line-height: 100%; /* 16px */
  text-decoration-line: underline;
  margin-left: 170px;
  cursor: pointer;
`;

const MoveBtn = styled.button`
  display: flex;
  width: 199px;
  padding: 10px 16px;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  border-style: solid;
  border-color: #7cd2d7;
  color: ${(props) => props.text};
  background-color: ${(props) => props.color};
  text-align: center;
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  margin-right: 24px;
  font-weight: 600;
  line-height: 100%; /* 16px */
  cursor: pointer;
`;

function Login() {
  const [Email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [off, setOff] = useState(true);

  const handleLogin = () => {
    console.log("email:", Email);
    console.log("password:", password);
  };
  return (
    <Wrapper>
      <MainWrapper>
        <LoginWrapper>
          <AppTitle>RECOM &lt;&lt; END</AppTitle>
          <AppName>백준 기반 개인화 문제 추천 플랫폼</AppName>
          <ExplanAccount>
            Welcome Back, Please login to your account
          </ExplanAccount>
          <InputList>
            <label style={{ color: "rgba(47, 61, 76, 0.3)" }}>Email</label>
            <Input
              type="email"
              id="email"
              value={Email}
              placeholder="cvtcvt007@naver.com"
              onChange={(e) => setEmail(e.target.value)}
            />
            <label style={{ color: "rgba(47, 61, 76, 0.3)" }}>Password</label>

            <Input
              type={off ? "password" : "email"}
              id="password"
              placeholder="*******"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <EyeOff onClick={() => setOff(!off)} />
          </InputList>
          <div style={{ marginLeft: "80px" }}>
            <input type="checkbox" style={{ accentColor: "#7CD2D7" }}></input>
            <RememberSpan>Remember me</RememberSpan>
            <ForgotSpan>Forgot password?</ForgotSpan>
          </div>

          <div
            className="MoveBtn"
            style={{
              marginLeft: "80px",
              marginTop: "24px",
              display: "flex",
            }}
          >
            <Link to={"/main"} style={{ textDecoration: "none" }}>
              <MoveBtn onClick={handleLogin} color="#7CD2D7" text="white">
                Login
              </MoveBtn>
            </Link>
            <Link to={"/SignUp"} style={{ textDecoration: "none" }}>
              <MoveBtn color="white" text="#7CD2D7">
                Sign Up
              </MoveBtn>
            </Link>
          </div>

          <div
            className="socialBtn"
            style={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
              marginTop: "20px",
            }}
          >
            <img
              src={apple}
              style={{
                width: "250px",
                height: "39.13px",
                marginBottom: "18px",
                cursor: "pointer",
              }}
            />
            <img
              src={google}
              style={{ width: "250px", height: "39.13px", cursor: "pointer" }}
            />
          </div>
          <RobotImg />
        </LoginWrapper>
        <LoginBackgroundImg />
      </MainWrapper>
    </Wrapper>
  );
}

export default Login;
