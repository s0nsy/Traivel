import "./App.css";
import Header from "./Pages/header";
import Main from "./Pages/Main";
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
              <List />
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
      </Routes>
    </div>
  );
}

export default App;