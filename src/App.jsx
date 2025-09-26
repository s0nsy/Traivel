import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import { createGlobalStyle } from "styled-components";
import Access from "./Pages/Access";
import Auth from "./Pages/Auth";

// Importing Components from Organized Folders
import Header from "./components/Main/header";
import Onboard from "./Pages/Onboard";
import List from "./Pages/List";
import Home from "./Pages/Home";
import Location from "./Pages/Location";
import Theme from "./Pages/Theme";
import Preference from "./Pages/Preference";
import Cost from "./Pages/Cost";
import Detail from "./Pages/Detail.jsx";
import Chat from "./Pages/Chat";
import Output from "./Pages/Output";
import Loader from "./Pages/infoLoad"
import Login from "./Pages/Login.jsx";
import Error from "./Pages/Error";
import "./App.css";
import RecomRoutePage from "./Pages/Result.jsx";
import MemberManagePage from "./Pages/MemberManagePage.jsx";
import NaverServicePage from "./Pages/naver.jsx";
import RoutePage from "./Pages/RouteManagePage.jsx";
import Mainhome from "./Pages/Mainhome.jsx";

// Global Styles using styled-components
const Global = createGlobalStyle`
  body {
    background: var(--chat-style, linear-gradient(180deg, #001516 0%, #084B50 100%));
  }
`;

function App() {
  

  return (
    <>
      <Global />
      <div className="App">       
        <Header/>

        <Routes>
          <Route
            path="/"
            element={<Mainhome/>}
          />
          <Route path="/onboard" element={<><Onboard/></>}/>
          <Route path="/cost" element={<><Cost/></>}/>
          <Route path="/preference" element={<><Preference/></>}/>
          <Route path="/location" element={<><Location/></>}/>
          <Route path="/theme" element={<><Theme/></>}/>
          <Route path="/chat" element={<><Chat/></>}/>
          <Route path="/detail" element={<><Detail/></>}/>
          <Route path="/lists" element={<><List/></>}/>
          <Route path="/output" element={<><Output/></>}/>
          <Route path="/info" element={<><Loader/></>}/>
          <Route path="/oauth" element={<><Login/></>}/>
          <Route path="/error" element={<><Error/></>}/>

          <Route path="/access" element={<><Access/></>}/>
          <Route path="/auth" element={<Auth/>} />
          <Route path="/result" element={<RecomRoutePage/>} />
          <Route path="/member" element={<MemberManagePage />} />
          <Route path="/route" element={<RoutePage />} />
          <Route path="/naver/:routeId" element={<NaverServicePage />} />
        </Routes>
      </div>
    </>
  );
}

export default App;