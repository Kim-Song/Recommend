import styled from "styled-components";
import userImg from "../assets/UserImg.png";
import Slider from "react-slick";
import { Link } from "react-router-dom";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";

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
  justify-content: center;
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

const MainServiceSection = styled.div`
  display: flex;
  flex-direction: column;
  margin: 36px;
`;
const RecommandSection = styled.div``;
const RecommandationTitle = styled.div`
  color: rgba(0, 0, 0, 0.7);
  margin-bottom: 22px;
  font-family: Roboto;

  font-size: 30px;
  font-style: normal;
  font-weight: 600;
  line-height: 20px; /* 66.667% */
`;
const TableSection = styled.div`
  width: 844px;
  height: 300px;
  border-radius: 30px;
  background: #fff;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  overflow: auto;
`;

const ColumnSection = styled.div`
  display: flex;
`;

const IdColumn = styled.div`
  padding: 16px;
  color: #8f97a3;
  text-align: center;
  height: 20px;
  width: 59px;
  /* Subtitle */
  font-family: Roboto;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 125% */
`;
const NameColumn = styled.div`
  color: var(--neutral-500, #8f97a3);
  padding: 16px;
  text-align: center;
  height: 20px;
  width: 390px;
  /* Subtitle */
  font-family: Roboto;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 125% */
`;
const PercentageColumn = styled.div`
  color: var(--neutral-500, #8f97a3);
  text-align: center;
  padding: 16px;
  width: 202px;
  /* Subtitle */
  font-family: Roboto;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 125% */
`;
const ServiceColumn = styled.div`
  width: 117px;
  padding: 16px;
  color: var(--neutral-500, #8f97a3);
  text-align: center;

  /* Subtitle */
  font-family: Roboto;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 125% */
`;

const CarouselValue = styled.div`
  width: 200px;
  height: 140px;
  flex-shrink: 0;
  border-radius: 25px;
  background: url(<path-to-image>), lightgray 50% / cover no-repeat;
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
`;
const LectureSection = styled.div`
  overflow: auto;
`;
function Main() {
  const questions = [
    {
      id: 10111,
      name: "구슬 탈출 2",
      percentage: 11.5,
    },
    {
      id: 302011,
      name: "2048 (Easy)",
      percentage: 30.4,
    },
    {
      id: 402030,
      name: "테트로미노",
      percentage: 45,
    },
    {
      id: 202022,
      name: "스타트와 링크",
      percentage: 0.9,
    },
    {
      id: 132131,
      name: "백준 친구 프로그래머스",
      percentage: 2,
    },
    {
      id: 412412,
      name: "드래곤 커브",
      percentage: 10,
    },
    {
      id: 71111,
      name: "축구 경기 예측",
      percentage: 10,
    },
  ];

  const settings = {
    dots: true,
    infinite: true,
    speed: 500,
  };
  return (
    <Wrapper>
      <MainWrapper>
        <SideBar>
          <UserImg />
          <UserName>Shin Jjang-gu</UserName>
          <MyPageBtn>My Page</MyPageBtn>
        </SideBar>
        <MainServiceSection>
          <RecommandSection>
            <RecommandationTitle>Question recommendation</RecommandationTitle>
            <TableSection>
              <ColumnSection
                style={{
                  background: "#fff",
                  boxShadow: "0px -2px 0px 0px #E3E5E8 inset",
                }}
              >
                <IdColumn>문제</IdColumn> <NameColumn>문제 제목</NameColumn>{" "}
                <PercentageColumn>정답 비율</PercentageColumn>
                <ServiceColumn>서비스</ServiceColumn>
              </ColumnSection>

              {questions.map((user, idx) => (
                <ColumnSection
                  style={idx % 2 == 0 ? { background: "#F5F5F5" } : {}}
                >
                  <IdColumn>{user.id}</IdColumn>
                  <NameColumn>{user.name}</NameColumn>
                  <PercentageColumn>{user.percentage}%</PercentageColumn>
                  <ServiceColumn>💎</ServiceColumn>
                </ColumnSection>
              ))}
            </TableSection>
          </RecommandSection>
          <RecommandSection>
            <RecommandationTitle style={{ marginTop: "47px" }}>
              Lecture Recommendation
            </RecommandationTitle>
            <LectureSection>
              <Slider {...settings}>
                <div>
                  <div>1</div>
                </div>
                <div>
                  <div>1</div>
                </div>{" "}
                <div>
                  <div>1</div>
                </div>
              </Slider>
            </LectureSection>
          </RecommandSection>
        </MainServiceSection>
      </MainWrapper>
    </Wrapper>
  );
}
export default Main;
