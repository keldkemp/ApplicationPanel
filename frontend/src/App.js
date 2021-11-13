import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import {Routes} from "./Routes";
import {useAuth} from "./hooks/auth.hook";
import {NavBar} from "./components/NavBar";
import {AuthContext} from "./context/AuthContext";
import {Loader} from "./components/Loader";
import 'materialize-css';

function App() {
    const {token, login, logout, refreshToken, ready, userId} = useAuth();
    const isAuth = !!token;
    const routes = Routes(isAuth);

    if (!ready) {
        return <Loader/>
    }

    return (
        <AuthContext.Provider value={{token, refreshToken, login, logout, isAuth, userId}}>
            <Router>
                { isAuth && <NavBar/> }
                {routes}
            </Router>
        </AuthContext.Provider>
    )
}

export default App;