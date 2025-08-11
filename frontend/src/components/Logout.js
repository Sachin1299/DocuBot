import axios from "axios";
import { useNavigate } from "react-router-dom";



const Logout = ()=>{
    const Navigate = useNavigate();
    const handlelogout = async()=>{
     const response = await axios.get("https://localhost:8443/api/auth/logout",{ withCredentials: true });
     console.log("response ",response);
     if(response.data == "Logout Successfull"){
         Navigate("/login");
     }
     else{
        Navigate("/home");
     }
    }
    return (
        <>
        <button onClick={handlelogout}>Logout</button>
        </>
    )
}

export default Logout;