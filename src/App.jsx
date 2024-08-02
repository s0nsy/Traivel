import React from 'react';
import './App.css';
import Header from './Pages/header';
import Main from './Pages/Main';
import Calendar from './Pages/Calendar';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';

function App() {
    return (
        <><Router>
        <Header/>
          <Routes>
            <Route path='/' element={<Main/>}/>
            <Route path='/temp' element={<Calendar/>}/>
          </Routes>
        </Router>
            
        </>
    );
}

export default App;
