import "./App.css";
import Header from "./components/Header";
import Main from "./components/Main";
import Footer from "./components/Footer";
import Footer2 from "./components/Footer2";
import Footer3 from "./components/Footer3";
import Body2 from "./components/Body2";
import { Routes, Route, Link } from "react-router-dom";

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
          path="/details"
          element={
            <div>
              <Header />
              <Body2 />
              <Footer2 />
            </div>
          }
        />
      </Routes>
    </div>
  );
}

export default App;
