import React, {useContext} from "react";
import {NavLink, useHistory} from "react-router-dom";
import {AuthContext} from "../context/AuthContext";

export const NavBar = () => {
    const auth = useContext(AuthContext);
    const history = useHistory();

    const logoutHandler = event => {
        event.preventDefault();
        auth.logout();
        history.push('/');
    }

    return (
        <nav>
            <div className="nav-wrapper grey darken-3" style={{ padding: '0 2rem' }}>
                <span className="brand-logo">Управление приложениями</span>
                <ul id="nav-mobile" className="right hide-on-med-and-down">
                    <li><NavLink to="/applications" className="nav-link px-2 text-white">Мои приложени</NavLink></li>
                    <li><NavLink to="/all" className="nav-link px-2 text-white">Все приложения</NavLink></li>
                    <li><NavLink to="/profile" className="nav-link px-2 text-white">Профиль</NavLink></li>
                    <li><a href="/" onClick={logoutHandler} className="nav-link px-2 text-white">Выйти</a></li>
                </ul>
            </div>
        </nav>
    )
}