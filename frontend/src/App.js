import { Route, Routes } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import './index.css';
function App() {
  return (
  <Routes>
<Route path="/" element={<Login/>}/>
<Route path="/home"  element={<Home/>} />   
<Route path="/login" element={<Login/>} />
<Route path="/signup" element={<Signup/>}/>
  </Routes>
  )
}

  export default App;