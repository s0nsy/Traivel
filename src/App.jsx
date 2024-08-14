
import "./App.css";
import React, { useState } from "react";
import Header from "./components/Header";
import Main from "./components/Main";
import List from "./Pages/List/List";
import Home from "./Pages/Main";
import Header from "./components/Header";
import Body from "./components/Body";
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
import People from "./Pages/People";
import { Routes, Route } from "react-router-dom";
import { createGlobalStyle } from "styled-components";
import React from 'react';
import "./App.css";
import { RouterProvider } from "react-router-dom";
import router from "./libs/router";


const Global = createGlobalStyle`
 body {;
    background: var(--chat-style, linear-gradient(180deg, #001516 0%, #084B50 100%));
`;
function App() {

  const [selectedDateRange, setSelectedDateRange] = useState("");
  const [selectedPeople, setSelectedPeople] = useState("추가");

  return (
    <div className="App">
      <Header selectedDateRange={selectedDateRange} selectedPeople={selectedPeople} />


  return (
    <>
    <Global/>   
     <div className="App">

      <Routes>
        <Route
          path="/"
          element={

            <><Home
              setSelectedDateRange={setSelectedDateRange}
              setSelectedPeople={setSelectedPeople} /><div>
                <Header />
                <Home />

              </div></>
          }
        />
          <Route
          path="/location"
          element={
            <div>
              <Header />
              <Location />

            </div>

          }
        />
        <Route
          path="/main"
          element={<Main />}
        />
        <Route
          path="/main/location"
          element={<Location />}
        />
        <Route
          path="/theme"

          element={<Theme />}
        />
        <Route
          path="/main/cost"
          element={<Cost />}
        />
        <Route
          path="/main/preference"
          element={<Preference />}
        />
        <Route
          path="/lists"
          element={<List />}
        />
        <Route
          path="/test"
          element={<People />}
        />
        <Route
          path="/detail"
          element={<Detail />}
        />
        <Route
          path="/chat"
          element={<Chat />}

          element={
            <div>
              <Header />
              <Theme />

            </div>
          }
        />
          <Route
          path="/cost"
          element={
            <div>
              <Header />
              <Cost />

            </div>
          }
        />
        <Route
          path="/preference"
          element={
            <div>
              <Header />
              <Preference />

            </div>
          }
        />
        <Route
          path="/main"
          element={
            <div>
              <Header />
              <Body />
            </div>
          }
        />
          <Route
          path="/location"
          element={
            <div>
              <Header />
              <Location />
            </div>
          }
        />
        <Route
          path="/theme"
          element={
            <div>
              <Header />
              <Theme />

            </div>
          }
        />
          <Route
          path="/cost"
          element={
            <div>
              <Header />
              <Cost />

            </div>
          }
        />
        <Route
          path="/preference"
          element={
            <div>
              <Header />
              <Preference />
            </div>
          }

        />
        <Route
          path="/lists"
          element={
            <div>
              <Header />
              <List />
            </div>
          }
        />
        <Route
          path="/test"
          element={
            <div>
              
              <People />

            </div>
          }
        />
        <Route
          path="/route"
          element={
            <div>
              <Header/>
              <Output/>
            </div>
          }
        
        />

        
      </Routes>
    </div>
    </>

  );



    return <RouterProvider router={router} />;



export default App;