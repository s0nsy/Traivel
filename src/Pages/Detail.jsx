import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import InfoBox from '../components/Detail/InfoBox';
import MapSection from '../components/Detail/MapSection';
import Recommendations from '../components/Detail/Recommendations';
import Accommodation from '../components/Detail/Accommodation';
import Food from '../components/Detail/Food';
import Attractions from '../components/Detail/Attractions';
import Footer from '../components/Detail/Footer';
import { useSelector } from 'react-redux';

const HeaderTextContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100%;
`;

const HeaderText = styled.div`
  position: relative;
  z-index: 1;
  padding: 1rem;
  gap: 1rem;
  font-size: 3rem;
  font-weight: bold;
  text-align: center;
  width: 73.75rem;
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const GradientText = styled.span`
  background: var(--gradation_1, linear-gradient(105deg, #00E3F1 10.28%, #A3FFD4 93.01%));
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`;

const SvgGraphic = styled.svg`
  width: 15rem;
  height: 10.24094rem;
  flex-shrink: 0;
  stroke-width: 2px;
  stroke: rgba(0, 227, 241, 0.4);
  position: absolute;
  left: 0rem;
  top: 50%;
  transform: translateY(-50%);
  z-index: 0;
`;

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 1.25rem;
`;

const ContentDiv = styled.div`
  display: flex;
  width: 73.75rem;
  padding-top: 15.75rem;
  flex-direction: column;
  align-items: flex-start;
  gap: 4rem;
`;

const ErrorMessage = styled.div`
  color: red;
  font-weight: bold;
  text-align: center;
  margin-top: 2rem;
`;

const MainPage = () => {
  const [destination, setDestination] = useState('');
  const [mapImage, setMapImage] = useState('');
  const [recommendations, setRecommendations] = useState([]);
  const [accommodations, setAccommodations] = useState([]);
  const [foods, setFoods] = useState([]);
  const [attractions, setAttractions] = useState([]);
  const [error, setError] = useState(null);

  const { region, district, features } = useSelector((state) => state.selectedItem);

  
  useEffect(() => {
    const fetchData = async () => {
      try {
        const queryString = `?region=${encodeURIComponent(region)}&district=${encodeURIComponent(district)}&features=${encodeURIComponent(features)}`;
        
        const response = await fetch(`/api/detail${queryString}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
          },
        });
  
        if (!response.ok) {
          throw new Error('Failed to fetch data from the API');
        }
  
        const data = await response.json();
  
        console.log('API Response Data:', data);
  
        if (data.error) {
          throw new Error(`API Error: ${data.error}`);
        }
  
        const tourData = data?.data?.tourData?.item?.[0];
        if (!tourData) {
          throw new Error('Invalid data structure from API');
        }
  
        setDestination(tourData.addr1 || '서귀포');
        setMapImage(tourData.firstimage || 'default_image_path');
  
        const recommendationData = [
          {
            smallText: '렌트카',
            description: data?.data?.optComment?.traffic || '렌터카에 대한 설명이 없습니다.',
            links: ['사이트 1', '사이트 2', '사이트 3', '사이트 4', '사이트 5']
          },
          {
            smallText: '대중교통',
            description: data?.data?.optComment?.hotel || '대중교통에 대한 설명이 없습니다.',
            links: ['사이트 1', '사이트 2', '사이트 3', '사이트 4', '사이트 5']
          },
          {
            smallText: '자전거 대여',
            description: data?.data?.optComment?.food || '자전거 대여에 대한 설명이 없습니다.',
            links: ['사이트 1', '사이트 2', '사이트 3', '사이트 4', '사이트 5']
          }
        ];
  
        setRecommendations(recommendationData);
        setAccommodations([tourData]);
        setFoods([tourData]);
        setAttractions([tourData]);
      } catch (error) {
        console.error('API Error:', error.message);
        setError(error.message);
      }
    };
  
    fetchData();
  }, [region, district, features]);
  
  

  return (
    <>
      <PageContainer>
        <ContentDiv>
          <HeaderTextContainer>
            <HeaderText>
              <SvgGraphic
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 241 169"
                fill="none"
              >
                <path
                  d="M213.35 106.41L212.963 107.332L215.134 108.243L214.283 106.048L213.35 106.41ZM42.4747 41.0888L41.7289 40.4225L39.3812 43.0504L42.7593 42.0474L42.4747 41.0888ZM127.636 119.695L126.649 119.859L128.633 119.772L127.636 119.695ZM241 4L229.977 0.560625L232.51 11.8264L241 4ZM1.90847 168.273C2.4558 167.084 3.04401 165.87 3.67324 164.635L1.89133 163.727C1.25026 164.985 0.650334 166.223 0.0915275 167.437L1.90847 168.273ZM7.60952 157.557C8.95962 155.315 10.4322 153.031 12.0277 150.725L10.383 149.587C8.76367 151.928 7.26818 154.247 5.89614 156.525L7.60952 157.557ZM16.8466 144.185C18.4822 142.096 20.2165 140.003 22.0498 137.919L20.548 136.598C18.6893 138.711 16.9307 140.834 15.2719 142.952L16.8466 144.185ZM27.5805 131.975C29.4514 130.074 31.4072 128.191 33.4481 126.337L32.1032 124.856C30.0342 126.736 28.0516 128.645 26.155 130.572L27.5805 131.975ZM39.6277 121.03C41.7016 119.348 43.8508 117.701 46.0755 116.096L44.9053 114.474C42.6495 116.101 40.4705 117.772 38.368 119.477L39.6277 121.03ZM52.7986 111.53C55.0557 110.09 57.3814 108.696 59.7759 107.356L58.7989 105.611C56.3704 106.97 54.0117 108.384 51.7228 109.844L52.7986 111.53ZM66.9851 103.59C69.3948 102.419 71.8672 101.303 74.4023 100.249L73.6343 98.4022C71.0629 99.4716 68.5551 100.603 66.1109 101.791L66.9851 103.59ZM82.0021 97.3445C84.529 96.462 87.1129 95.6407 89.7539 94.886L89.2044 92.963C86.5261 93.7283 83.9056 94.5612 81.3426 95.4563L82.0021 97.3445ZM97.6297 92.8768C100.238 92.2897 102.898 91.7672 105.61 91.3141L105.28 89.3414C102.532 89.8007 99.8351 90.3303 97.1905 90.9257L97.6297 92.8768ZM113.668 90.1931C116.325 89.8968 119.027 89.6664 121.776 89.5058L121.659 87.5092C118.875 87.6718 116.138 87.9052 113.447 88.2055L113.668 90.1931ZM129.898 89.2397C132.566 89.2197 135.274 89.2647 138.024 89.3782L138.106 87.3799C135.324 87.2651 132.583 87.2195 129.883 87.2398L129.898 89.2397ZM146.136 89.9038C148.795 90.1379 151.491 90.4353 154.223 90.7986L154.487 88.816C151.725 88.4489 149 88.1483 146.311 87.9115L146.136 89.9038ZM162.278 92.0446C164.919 92.5098 167.592 93.0359 170.298 93.6252L170.723 91.671C167.992 91.0761 165.292 90.5448 162.625 90.0749L162.278 92.0446ZM178.217 95.5084C180.799 96.1734 183.408 96.8952 186.046 97.6756L186.614 95.7578C183.953 94.9707 181.321 94.2426 178.716 93.5717L178.217 95.5084ZM193.83 100.123C196.387 100.974 198.97 101.88 201.578 102.841L202.27 100.965C199.642 99.9959 197.039 99.0833 194.462 98.2255L193.83 100.123ZM209.133 105.756C210.404 106.268 211.681 106.793 212.963 107.332L213.737 105.488C212.446 104.945 211.16 104.417 209.88 103.901L209.133 105.756ZM214.283 106.048C213.826 104.871 213.325 103.664 212.779 102.433L210.95 103.243C211.484 104.447 211.973 105.625 212.418 106.771L214.283 106.048ZM209.308 95.4092C208.11 93.2098 206.784 90.9618 205.326 88.6864L203.642 89.7654C205.074 92.0011 206.377 94.2082 207.551 96.3655L209.308 95.4092ZM200.895 82.2563C199.383 80.2103 197.766 78.158 196.043 76.1144L194.514 77.4037C196.209 79.4141 197.8 81.4327 199.286 83.4448L200.895 82.2563ZM190.826 70.309C189.056 68.4598 187.194 66.6297 185.241 64.8304L183.886 66.3015C185.808 68.0719 187.639 69.8725 189.381 71.692L190.826 70.309ZM179.338 59.7265C177.344 58.109 175.267 56.528 173.109 54.9931L171.95 56.6231C174.073 58.133 176.116 59.6884 178.078 61.2798L179.338 59.7265ZM166.604 50.6717C164.423 49.3208 162.168 48.0187 159.838 46.7733L158.895 48.5371C161.187 49.7622 163.405 51.0431 165.55 52.372L166.604 50.6717ZM152.83 43.3082C150.491 42.2425 148.084 41.2344 145.61 40.2904L144.897 42.159C147.332 43.0879 149.7 44.0798 152.001 45.1283L152.83 43.3082ZM138.236 37.7362C135.791 36.973 133.286 36.2719 130.721 35.6384L130.241 37.5801C132.768 38.2039 135.234 38.8941 137.64 39.6453L138.236 37.7362ZM123.092 33.9924C120.575 33.526 118.005 33.1237 115.381 32.79L115.128 34.774C117.715 35.1029 120.248 35.4994 122.727 35.9589L123.092 33.9924ZM107.616 32.0185C105.066 31.8346 102.469 31.7144 99.8234 31.6615L99.7834 33.6611C102.394 33.7133 104.957 33.8319 107.472 34.0133L107.616 32.0185ZM92.0255 31.6998C89.4721 31.7749 86.877 31.9115 84.2398 32.1127L84.3919 34.1069C86.9984 33.9081 89.5624 33.7731 92.0842 33.6989L92.0255 31.6998ZM76.4798 32.879C73.9417 33.1858 71.3667 33.5513 68.7546 33.978L69.077 35.9518C71.6621 35.5296 74.2096 35.168 76.7198 34.8646L76.4798 32.879ZM61.0691 35.3899C58.5758 35.8981 56.0502 36.4611 53.4922 37.0807L53.963 39.0244C56.4977 38.4105 58.9994 37.8529 61.4686 37.3496L61.0691 35.3899ZM45.9797 39.0389C44.724 39.3892 43.4607 39.7528 42.19 40.1301L42.7593 42.0474C44.0195 41.6732 45.272 41.3126 46.517 40.9654L45.9797 39.0389ZM43.2204 41.755C44.0402 40.8374 44.9017 39.9308 45.8015 39.0441L44.3976 37.6196C43.4672 38.5366 42.5764 39.4739 41.7289 40.4225L43.2204 41.755ZM51.47 34.1505C53.4636 32.6551 55.5697 31.2988 57.7627 30.1419L56.8295 28.3729C54.5342 29.5838 52.3392 30.9984 50.2699 32.5506L51.47 34.1505ZM64.619 27.3172C66.9858 26.6152 69.4122 26.1797 71.8737 26.0701L71.7848 24.0721C69.1457 24.1895 66.5581 24.656 64.0503 25.3998L64.619 27.3172ZM79.1744 26.7395C81.4466 27.2693 83.7378 28.116 86.0301 29.3304L86.9663 27.563C84.525 26.2698 82.0721 25.3615 79.6285 24.7918L79.1744 26.7395ZM92.1438 33.4844C93.9219 34.9733 95.6926 36.7147 97.4461 38.7337L98.9561 37.4222C97.139 35.3299 95.2934 33.5131 93.4278 31.951L92.1438 33.4844ZM102.008 44.695C103.336 46.6497 104.649 48.7831 105.94 51.1065L107.688 50.1346C106.37 47.763 105.026 45.5784 103.662 43.5708L102.008 44.695ZM109.359 57.8163C110.379 59.9962 111.383 62.3086 112.367 64.7594L114.223 64.014C113.226 61.5307 112.207 59.1841 111.17 56.9684L109.359 57.8163ZM115.025 71.8559C115.828 74.1524 116.615 76.552 117.386 79.0582L119.297 78.4706C118.52 75.9413 117.725 73.5176 116.913 71.1959L115.025 71.8559ZM119.493 86.3423C120.138 88.7102 120.769 91.162 121.385 93.7001L123.328 93.2285C122.708 90.6731 122.073 88.2033 121.423 85.8168L119.493 86.3423ZM123.09 101.134C123.616 103.555 124.128 106.046 124.627 108.61L126.59 108.228C126.089 105.651 125.573 103.145 125.045 100.71L123.09 101.134ZM125.996 116.027C126.217 117.288 126.435 118.565 126.649 119.859L128.622 119.532C128.407 118.232 128.188 116.949 127.966 115.682L125.996 116.027ZM128.633 119.772C128.728 118.547 128.86 117.263 129.032 115.927L127.049 115.672C126.872 117.04 126.736 118.357 126.639 119.618L128.633 119.772ZM130.361 108.253C130.881 105.832 131.515 103.301 132.272 100.683L130.351 100.128C129.581 102.789 128.936 105.365 128.406 107.833L130.361 108.253ZM134.684 93.2547C135.541 90.875 136.496 88.4466 137.555 85.9839L135.718 85.1937C134.642 87.694 133.673 90.16 132.803 92.5773L134.684 93.2547ZM140.861 78.887C142.004 76.6109 143.237 74.318 144.565 72.019L142.833 71.0187C141.485 73.352 140.234 75.6792 139.073 77.9894L140.861 78.887ZM148.673 65.3633C150.081 63.2242 151.574 61.0888 153.157 58.9656L151.553 57.7704C149.947 59.9254 148.431 62.0927 147.002 64.2637L148.673 65.3633ZM158.013 52.8343C159.663 50.8718 161.394 48.9273 163.211 47.008L161.758 45.6334C159.914 47.5815 158.157 49.5552 156.482 51.5471L158.013 52.8343ZM168.754 41.4838C170.624 39.727 172.571 37.9985 174.598 36.3044L173.316 34.7697C171.258 36.489 169.282 38.2432 167.384 40.0263L168.754 41.4838ZM180.734 31.4707C182.797 29.9397 184.934 28.4442 187.146 26.9894L186.047 25.3184C183.803 26.7941 181.635 28.3112 179.542 29.8646L180.734 31.4707ZM193.808 22.8707C196.024 21.5838 198.309 20.337 200.664 19.1342L199.754 17.353C197.367 18.5721 195.051 19.8362 192.803 21.1411L193.808 22.8707ZM207.714 15.7642C210.056 14.7184 212.462 13.7151 214.933 12.7576L214.21 10.8927C211.708 11.8623 209.272 12.8785 206.899 13.938L207.714 15.7642ZM222.295 10.1084C224.73 9.29743 227.223 8.52961 229.776 7.80778L229.232 5.88323C226.65 6.61333 224.127 7.39016 221.663 8.2109L222.295 10.1084Z"
                  fill="url(#paint0_linear_660_9982)"
                />
                <defs>
                  <linearGradient
                    id="paint0_linear_660_9982"
                    x1="6.56961"
                    y1="65.8757"
                    x2="221.521"
                    y2="153.218"
                    gradientUnits="userSpaceOnUse"
                  >
                    <stop stopColor="#00E3F1" stopOpacity="0.4" />
                    <stop offset="1" stopColor="#A3FFD4" stopOpacity="0.2" />
                  </linearGradient>
                </defs>
              </SvgGraphic>
              요청해주신 <GradientText>{destination}</GradientText>의 여행 정보를 알아왔어요!
            </HeaderText>
          </HeaderTextContainer>

          {error ? (
            <ErrorMessage>데이터를 불러오는데 실패했습니다: {error}</ErrorMessage>
          ) : (
            <>
              <InfoBox destination={destination} />
              <MapSection mapImage={mapImage} description="서귀포시는 여기에 위치해 있어요" />
              <Recommendations recommendations={recommendations} />
              <Accommodation accommodations={accommodations} />
              <Food foods={foods} />
              <Attractions attractions={attractions} />
            </>
          )}
          <Footer />
        </ContentDiv>
      </PageContainer>
    </>
  );
};

export default MainPage;
