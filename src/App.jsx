
import "./App.css";
import Header from "./components/Header";
import Main from "./components/Main";
import Footer from "./components/Footer";
import Theme from "./components/Theme";
import Location from "./components/Location";
import Cost from "./components/Cost";
import Preference from "./components/Preference";

import { Routes, Route } from "react-router-dom";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route
          path="/"
          element={
            <div>
              <Header />
              <Main />
              <Footer />
            </div>
          }
        />
        <Route
          path="/theme"
          element={
            <div>
              <Theme />
            </div>
          }
        />
        <Route
          path="/location"
          element={
            <div>
              <Location />
            </div>
          }
        />
        <Route
          path="/cost"
          element={
            <div>
              <Cost />
            </div>
          }
        />
        <Route
          path="/preference"
          element={
            <div>
              <Preference />
            </div>
          }
        />
      </Routes>
    </div>
  );


export default App;
