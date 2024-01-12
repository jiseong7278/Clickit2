import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";
import {Route, Routes} from "react-router-dom";
import Login from "./login/Login";
import AfterLogin from "./login/afterLogin";
import routes from "./routes";

function App() {

  return (
      // <div>
      // <Login/>
      // </div>
    <Routes>
        <Route path={"/"} element={<Login/>}/>
        <Route path={"/afterLogin"} element={<AfterLogin/>}/>
    </Routes>
  );
}

export default App;
