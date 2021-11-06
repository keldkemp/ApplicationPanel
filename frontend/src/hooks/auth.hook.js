import {useState, useCallback, useEffect} from "react";

const storageName = 'user_data';

export const useAuth = () => {
    const [token, setToken] = useState(null);
    const [ready, setReady] = useState(false);
    const [refreshToken, setRefreshToken] = useState(null);

    const login = useCallback((jwtToken, jwtRefreshToken) => {
        setToken(jwtToken);
        setRefreshToken(jwtRefreshToken);

        localStorage.setItem(storageName, JSON.stringify({token: jwtToken, refreshToken: jwtRefreshToken}));
    }, []);

    const logout = useCallback(() => {
        setToken(null);
        setRefreshToken(null);

        localStorage.removeItem(storageName);
    }, []);

    useEffect(() => {
       const data = JSON.parse(localStorage.getItem(storageName));

       if (data) {
           login(data.token, data.refreshToken);
       }
       setReady(true);
    }, [login]);

    return { login, logout, token, refreshToken, ready }
}