import styled from "styled-components";
import userImg from "../assets/UserImg.png";
import communityImg from "../assets/community.png";
import { Link } from "react-router-dom";
const Wrapper = styled.div`
  margin: 0 auto;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const MainWrapper = styled.div`
  display: flex;
  width: 1188px;
  height: 657px;
  flex-shrink: 0;
  background: #b1d9db;
  box-shadow: 10px 72px 80px -48px #223a28;
`;

const SideBar = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 288px;
  height: 657px;
  flex-shrink: 0;
  border-radius: 0px 37px 37px 0px;
  background: #fff;
`;
const UserImg = styled.div`
  display: flex;

  width: 170px;
  margin-top: 37px;
  height: 170px;
  flex-shrink: 0;
  border-radius: 150px;
  background: url(${userImg}), lightgray 50% / cover no-repeat;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25) inset;
`;
const UserName = styled.span`
  display: flex;
  width: 363px;
  height: 60px;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
  color: rgba(0, 0, 0, 0.8);
  text-align: center;
  font-family: Roboto;
  font-size: 23px;
  margin-top: 28px;
  font-style: normal;
  font-weight: 600;
  line-height: normal;
`;

const MyPageBtn = styled.div`
  display: flex;
  width: 200px;
  height: 50px;
  flex-shrink: 0;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
  color: #fff;
  text-align: center;
  font-family: Roboto;
  cursor: pointer;
  font-size: 23px;
  border-radius: 8px;
  margin-top: 11px;
  background: #7cd2d7;
  box-shadow: 0px 10px 10px 0px rgba(46, 213, 115, 0.15);
  font-style: normal;
  font-weight: 500;
  line-height: normal;
`;

const CommunityBtn = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 200px;
  height: 200px;

  margin-top: 22px;
  border-radius: 30px;
  background: url(${communityImg}), white 10% / cover no-repeat;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.25);
`;
const CommunityName = styled.div`
  color: #070707;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-family: Roboto;
  font-size: 30px;
  margin-top: 40px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
`;

const CommunityInfo = styled.div`
  color: rgba(0, 0, 0, 0.7);
  text-align: center;
  font-family: Roboto;
  font-size: 18px;
  font-style: normal;
  font-weight: 400;
  line-height: normal;
  margin-top: 16px;
  padding: 10px;
`;

function Main() {
  return (
    <Wrapper>
      <MainWrapper>
        <SideBar>
          <UserImg />
          <UserName>Shin Jjang-gu</UserName>
          <MyPageBtn>My Page</MyPageBtn>
          <CommunityBtn>
            <CommunityName>Community</CommunityName>
            <CommunityInfo>
              Please share the code with each other and solve it
            </CommunityInfo>
          </CommunityBtn>
        </SideBar>
      </MainWrapper>
    </Wrapper>
  );
}
export default Main;
