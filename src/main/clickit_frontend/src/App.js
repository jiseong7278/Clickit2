import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";
import {Route, Routes} from "react-router-dom";
import Login from "./member/Login";

function App() {

  const [data, setData] = useState([]);
  const [members, setMembers] = useState([]);

    // useEffect(() => {
    //     axios.all([axios.get('/dormitory/test'), axios.get('/member/getAll')])
    //         .then(axios.spread((dormitory, member) => {
    //             console.log(dormitory.data);
    //             console.log(member.data);
    //             setData(dormitory.data);
    //             setMembers(member.data);
    //         }))
    //         .catch(error => {
    //             console.log(error);
    //         });
    // },[]);

  return (
    <Routes>
        <Route path={"/login"} element={<Login/>}/>
    </Routes>
  );
}

export default App;
