import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Body from './components/Body2'; // Body2는 Body로 수정되었습니다.
import List from './Pages/List';
import Home from './Pages/Home';
import Location from './components/Location';
import Theme from './components/Theme';
import Preference from './components/Preference';
import Cost from './components/Cost';
import Output from './components/Output';
import People from './Pages/People';
import { createGlobalStyle } from 'styled-components';
import './App.css';

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
        <Routes>
          <Route
            path="/"
            element={
              <div>
                <Header />
                <Home />
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
            element={<People />}
          />
          <Route
            path="/routes"
            element={
              <div>
                <Header />
                <Output />
              </div>
            }
          />
        </Routes>
      </div>
    </>
  );
}

export default App;
