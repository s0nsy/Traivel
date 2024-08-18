import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import { createGlobalStyle } from "styled-components";

// Importing Components from Organized Folders
import Header from "./components/Main/header";
import Onboard from "./Pages/Onboard";
import List from "./Pages/List";
import Home from "./Pages/Home";
import Location from "./Pages/Location";
import Theme from "./Pages/Theme";
import Preference from "./Pages/Preference";
import Cost from "./Pages/Cost";
import Detail from "./Pages/Detail";
import Chat from "./Pages/Chat";
import Output from "./Pages/Output";
import "./App.css";

// Global Styles using styled-components
const Global = createGlobalStyle`
  body {
    background: var(--chat-style, linear-gradient(180deg, #001516 0%, #084B50 100%));
  }
`;

function App() {
  const [selectedDateRange, setSelectedDateRange] = useState("");
  const [selectedPeople, setSelectedPeople] = useState("추가");

  return (
    <>
      <Global />
      <div className="App">       
        <Header selectedDateRange={selectedDateRange} selectedPeople={selectedPeople} />

        <Routes>
          <Route
            path="/"
            element={<Home setSelectedDateRange={setSelectedDateRange} setSelectedPeople={setSelectedPeople} />}
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
        </Routes>
      </div>
    </>
  );
}

export default App;
