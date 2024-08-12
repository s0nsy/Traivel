
import "./App.css";
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
  return (
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
              <Main />

            </div>
          }
        />
          <Route
          path="/main/location"
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
          path="/main/cost"
          element={
            <div>
              <Header />
              <Cost />

            </div>
          }
        />
        <Route
          path="/main/preference"
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
          path="/detail"
          element={
            <div>
              <Header />
              <Detail />

            </div>
          }
        />
        <Route
          path="/chat"
          element={
            <div>
              <Header />
              <Chat />

            </div>
          }
        />
        
      </Routes>
    </div>
  );
}


export default App;