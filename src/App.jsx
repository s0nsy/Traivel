
import "./App.css";
import Header from "./components/Header";
import Main from "./components/Main";
import List from "./Pages/List/List";


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
        
      </Routes>
    </div>
  );
}


export default App;