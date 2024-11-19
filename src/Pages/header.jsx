import React from 'react';
import styled from 'styled-components';
import SideBarIcon from '../assets/ic_round-menu.png';
import Group from '../assets/Group 1000003732.png';
import Rectangle from '../assets/Rectangle 42986.png';

const NavContainer = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: linear-gradient(0deg, #013F44 -27.78%, #011E20 37.5%);
    height: 4rem;
    z-index: 1000;
`;

const NavLogo = styled.p`
    display: flex;
    font-family: Pretendard, sans-serif;
    color: #01ECFF;
    font-size: 24px;
    font-weight: 600;
    margin: 0 20px;
`;

const NavSelectCon = styled.div`
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 20px;
`;

const NavSelectImg = styled.img`
    width: 248px;
    height: auto;
`;

const NavSelect = styled.p`
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    font-family: Pretendard, sans-serif;
    color: #FFFFFF;
    font-size: 0.9rem;
`;

const NavSelectLeft = styled(NavSelect)`
    left: 25%; 
    transform: translate(-50%, -50%);
`;

const NavSelectRight = styled(NavSelect)`
    right: 25%; 
    transform: translate(50%, -50%);
`;

const SideRectangle = styled.img`
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    width: 4px;
    height: auto;
`;

const SideBarBtn = styled.img`
    display: flex;
    width: 30px;
    height: 30px;
    cursor: pointer;
    transform: translate(-1000%)
`;

const BtnCon = styled.div`
    display:flex;
    justify-content: space-between;
    align-items: center;
    gap:15px;
    transform: translate(70%)
    `
const Header = () => {
    return (
        <>
            <NavContainer>
                <BtnCon>
                <NavLogo>Route Porter</NavLogo>
                <NavSelectCon>
                    <NavSelectImg src={Group} alt="Group Icon" />
                    <NavSelectLeft>여행 날짜 선택</NavSelectLeft>
                    <SideRectangle src={Rectangle} alt="Separator" />
                    <NavSelectRight>인원 선택</NavSelectRight>
                </NavSelectCon>
                </BtnCon>
                <SideBarBtn src={SideBarIcon} alt="Sidebar Icon" />
            </NavContainer>
        </>
    );
};

export default Header;
