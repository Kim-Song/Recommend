import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Landing from "./components/Landing";
import SignUp from "./components/SignUp";
import Main from "./components/Main";
import Lecture from "./components/Lecture";
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />}></Route>
        <Route path="/" element={<Landing />}></Route>
        <Route path="/Main" element={<Main />}></Route>
        <Route path="/SignUp" element={<SignUp />}></Route>
        <Route path="/Lecture" element={<Lecture />}></Route>
      </Routes>
    </Router>
  );
}

export default App;
