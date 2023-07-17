import './App.css';

import AppContainer from "./components/utils/AppContainer";
import { AuthContext } from "./components/utils/AuthContext";
import { Navigate, Route, Routes} from "react-router-dom";
import * as View from './views/index'
import { useState } from "react";

function App() {

    const [user, setUser] = useState({
        id: undefined,
        email: localStorage.getItem("email"),
        token: localStorage.getItem("token"),
        role: localStorage.getItem("role"),
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
        <AuthContext.Provider value={auth}>
            <AppContainer>
                <Routes>
                    <Route index path={'/'} element={<View.Home/>}/>
                    <Route path='/login' element={user.token ? <Navigate replace to='/'/> : <View.Login/>}/>
                    <Route path='/profile' element={user.token ? <View.Profile/> : <Navigate replace to='/login'/>}/>
                    <Route path='/editProfile'
                           element={user.token ? <View.EditProfile/> : <Navigate replace to='/login'/>}/>
                    <Route path='/newProfile'
                           element={user.token ? <Navigate replace to='/profile'/> : <View.EditProfile/>}/>
                    <Route path='/dashboard'
                           element={user.token ? <View.Dashboard/> : <Navigate replace to='/login'/>}/>
                    <Route path='/ticket/:ticketId'
                           element={user.token ? <View.Ticket/> : <Navigate replace to='/login'/>}/>
                    <Route path='/ticket/:ticketId/chat'
                           element={user.token ? <View.Chat/> : <Navigate replace to='/login'/>}/>
                    <Route path='/ticket/create'
                           element={user.token ? <View.TicketCreate/> : <Navigate replace to='/login'/>}/>
                    <Route path='*' element={<View.Error/>}/>
                </Routes>
            </AppContainer>
        </AuthContext.Provider>
    );
}

export default App;
