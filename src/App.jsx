import React, { useState } from "react";
import { Routes, Route } from "react-router-dom";
import { createGlobalStyle } from "styled-components";
import Header from "./components/Header";
import Main from "./components/Body";
import List from "./Pages/List";
import Home from "./Pages/Home";
import Location from "./components/Location";
import Theme from "./components/Theme";
import Preference from "./components/Preference";
import Cost from "./components/Cost";
import People from "./Pages/People";
import Detail from "./Pages/Detail/Main";
import Chat from "./Pages/Chat";
import Output from "./components/Output";
import "./App.css";

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
            element={
              <>
                <Home setSelectedDateRange={setSelectedDateRange} setSelectedPeople={setSelectedPeople} />
              </>
            }
          />
          <Route
            path="/location"
            element={
              <>
                <Header />
                <Location />
              </>
            }
          />
          <Route path="/main" element={<Main />} />
          <Route path="/main/location" element={<Location />} />
          <Route path="/theme" element={<Theme />} />
          <Route path="/main/cost" element={<Cost />} />
          <Route path="/main/preference" element={<Preference />} />
          <Route path="/lists" element={<List />} />
          <Route path="/test" element={<People />} />
          <Route path="/detail" element={<Detail />} />
          <Route
            path="/chat"
            element={
              <>
                <Header />
                <Chat />
              </>
            }
          />
          <Route
            path="/cost"
            element={
              <>
                <Header />
                <Cost />
              </>
            }
          />
          <Route
            path="/preference"
            element={
              <>
                <Header />
                <Preference />
              </>
            }
          />
          <Route
            path="/route"
            element={
              <>
                <Header />
                <Output />
              </>
            }
          />
        </Routes>
      </div>
    </>
  );
}

export default App;
