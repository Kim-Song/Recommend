import styled from "styled-components";
import three_space from "../assets/three_space.png";
import sign_up_google from "../assets/GoogleLogin.png";
import sing_up_apple from "../assets/AppleLogin.png";
import three_space_background from "../assets/backgroundSpace.png";
import OffBtn from "../assets/off.png";
import { Link } from "react-router-dom";
import { useState } from "react";
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

const SignWrapper = styled.div`
  position: absolute;
  width: 790px;
  height: 657px;
  flex-shrink: 0;
  margin-left: 410px;
  z-index: 1;
  border-radius: 37px 0px 0px 37px;
  background: #fff;
`;
const SignUpInfo = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-left: 250px;
`;
const ThreeSpace = styled.div`
  position: absolute;
  z-index: 2;
  bottom: 43px;
  left: -50px;
  width: 727.455px;
  height: 648.92px;

  transform: rotate(${(props) => props.degree});
  flex-shrink: 0;
  background-image: url(${(props) => props.image});
`;
const BackgroundSpace = styled.div`
  position: absolute;
  z-index: 0;
  top: 28px;
  left: 108px;
  width: 570px;
  height: 630px;

  background-image: url(${(props) => props.image});
`;
const CreateAccount = styled.span`
  color: #000;
  font-family: Poppins;
  font-size: 32px;
  font-style: normal;
  font-weight: 600;
  line-height: 100%; /* 32px */
  margin-top: 86px;
`;

const OrSpan = styled.span`
  color: #909090;
  font-family: Poppins;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  margin-top: 20px;
  margin-left: 180px;
`;

const InputList = styled.div`
  margin-top: 10px;
  display: flex;
  flex-direction: column;
`;
const Input = styled.input`
  display: flex;
  width: 421px;
  height: 56px;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
  color: rgba(0, 0, 0, 0.3);
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: 100%; /* 16px */
  border-radius: 8px;
  border: 1px solid #7cd2d7;
  background: #fff;
  margin-bottom: 20px;
`;
const EyeOff = styled.button`
  position: absolute;
  width: 24px;
  height: 24px;
  top: 460px;
  left: 634px;
  cursor: pointer;
  border: none;
  background: url(${OffBtn});
`;

const CreateBtn = styled.button`
  border-radius: 8px;
  background: #7cd2d7;
  border: none;
  color: #fff;
  text-align: center;
  font-family: Inter;
  font-size: 16px;
  font-style: normal;
  font-weight: 600;
  line-height: 100%; /* 16px */
  width: 427px;
  display: flex;
  padding: 16px;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  flex: 1 0 0;
`;

function SignUp() {
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [off, setOff] = useState(true);

  const SignUpInfoData = () => {
    console.log(fullName);
    console.log(email);
    console.log(password);
  };
  return (
    <Wrapper>
      <MainWrapper>
        <ThreeSpace image={three_space} degree="-11.982deg" />
        <SignWrapper>
          <SignUpInfo>
            <CreateAccount>Create Account</CreateAccount>
            <div className="Sign up" style={{ marginTop: "46px" }}>
              <img
                src={sign_up_google}
                style={{ marginRight: "22px", cursor: "pointer" }}
              />
              <img src={sing_up_apple} style={{ cursor: "pointer" }} />
            </div>
            <OrSpan>- OR -</OrSpan>
            <InputList>
              <Input
                type="text"
                id="fullName"
                value={fullName}
                placeholder="Full Name"
                onChange={(e) => setFullName(e.target.value)}
              />
              <Input
                type="email"
                id="email"
                value={email}
                placeholder="Email Address"
                onChange={(e) => setEmail(e.target.value)}
              />
              <Input
                type={off ? "password" : "email"}
                id="password"
                value={password}
                placeholder="Password"
                onChange={(e) => setPassword(e.target.value)}
              />
              <EyeOff onClick={() => setOff(!off)} />
            </InputList>
            <Link to={"/login"} style={{ textDecoration: "none" }}>
              <CreateBtn onClick={SignUpInfoData}>Create Account</CreateBtn>
            </Link>
          </SignUpInfo>
        </SignWrapper>
        <BackgroundSpace image={three_space_background} />
      </MainWrapper>
    </Wrapper>
  );
}

export default SignUp;
