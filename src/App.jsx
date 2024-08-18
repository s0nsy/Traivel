import Header from "./components/Header";
import Body2 from "./components/Body2";
import List from "./Pages/List";
import Home from "./Pages/Home";
import Location from "./components/Location";
import Theme from "./components/Theme";
import Preference from "./components/Preference";
import Cost from "./components/Cost";
import Output from "./components/Output";
import People from "./Pages/People";
import { Routes, Route } from "react-router-dom";
import { createGlobalStyle } from "styled-components";
import { useState } from "react";

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
                <Body2   />
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
