import "./App.css";
import React, { useState } from "react";
import Header from "./components/Header";
import Main from "./components/Main";
import List from "./Pages/List/List";
import Home from "./Pages/Main";
import Location from "./components/Location";
import Theme from "./components/Theme";
import Preference from "./components/Preference";
import Cost from "./components/Cost";
import People from "./Pages/People";
import Detail from "./Pages/Detail/Main";
import Chat from "./Pages/Chat";
import { Routes, Route } from "react-router-dom";

function App() {
  const [selectedDateRange, setSelectedDateRange] = useState("");
  const [selectedPeople, setSelectedPeople] = useState("추가");

  return (
    <div className="App">
      <Header selectedDateRange={selectedDateRange} selectedPeople={selectedPeople} />
      <Routes>
        <Route
          path="/"
          element={
            <Home
              setSelectedDateRange={setSelectedDateRange}
              setSelectedPeople={setSelectedPeople}
            />
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
        />
      </Routes>
    </div>
  );
}

export default App;
