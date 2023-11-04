import logo from './logo.svg';
import './App.css';
import {useEffect, useState} from "react";
import axios from "axios";

function App() {

  const [data, setData] = useState([]);
  const [members, setMembers] = useState([]);

  // useEffect(() => {
  //   axios.get('/dormitory/test')
  //     .then(response => {
  //       console.log(response.data);
  //       setData(response.data);
  //     })
  //     .catch(error => {
  //       console.log(error);
  //     });
  // },[]);

    useEffect(() => {
        axios.all([axios.get('/dormitory/test'), axios.get('/member/getAll')])
            .then(axios.spread((dormitory, member) => {
                console.log(dormitory.data);
                console.log(member.data);
                setData(dormitory.data);
                setMembers(member.data);
            }))
            .catch(error => {
                console.log(error);
            });
    },[]);

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
          <div>
              {data}
          </div>
          <div>
              {members.map((member) => (
                  <div key={member.id}>
                      {member.name}
                  </div>
              ))}
          </div>
      </header>
    </div>
  );
}

export default App;
