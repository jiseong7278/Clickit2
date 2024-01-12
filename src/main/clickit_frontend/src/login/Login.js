import axios from "axios";
import {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import afterLogin from "./afterLogin";

const Login = () =>{

    const navigate = useNavigate();

    const [serverResponse, setServerResponse] = useState({
        id: "",
        password: "",
        accessToken: "",
        refreshToken:"",
    });

    function signIn() {
        axios.post("http://localhost:8080/login/signIn", {
            id: "test_member_id",
            password: "test_member_password"
        }).then((res) => {
            setServerResponse(res.data);
            console.log(res);
        }).catch((err) => {
            console.log(err);
        })
    }

    return (
        <div>
            <input type="text" placeholder="아이디"/>
            <input type="password" placeholder="비밀번호"/>
            <Link to={"/afterLogin"} state={serverResponse}>
                <button onClick={() => signIn()}>로그인</button>
            </Link>

            <div>
            {serverResponse.id}
            </div>
            <div>
                {serverResponse.accessToken}
            </div>
        </div>
    )
}

export default Login;