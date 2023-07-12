import './App.css';

import AppContainer from "./components/utils/AppContainer";
import { AuthContext } from "./components/utils/AuthContext";
import {Route, Routes} from "react-router-dom";
import * as View from './views/index'
import {useState} from "react";

function App() {
    const [user, setUser] = useState({
        id: undefined,
        email: undefined,
        token: undefined,
        username: undefined,
        name: undefined,
        surname: undefined,
        dateOfBirth: undefined });

    const resetUser = () => {
        setUser({
            id: undefined,
            email: undefined,
            token: undefined,
            username: undefined,
            name: undefined,
            surname: undefined,
            dateOfBirth: undefined })
        localStorage.setItem("token", "")
    }

    const auth = { user, setUser, resetUser };
    return (
      <AppContainer>
          <AuthContext.Provider value={auth}>
            <Routes>
              <Route index path={'/'} element={<View.Home/>}/>
                <Route path='/login' element={<View.Login/>} />
                <Route path='/profile' element={<View.Profile/>} />
                <Route path='/editProfile' element={<View.EditProfile/>} />
                <Route path='/newProfile' element={<View.EditProfile/>} />
                <Route path='/dashboard' element={<View.Dashboard/>} />
                <Route path='/ticket/:ticketId' element={<View.Ticket/>} />
                <Route path='*' element={<View.Error/>}/>
            </Routes>
          </AuthContext.Provider>
      </AppContainer>
  );
}

export default App;
