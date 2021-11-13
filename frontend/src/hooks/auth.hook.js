import {useState, useCallback, useEffect} from "react";

const storageName = 'user_data';

export const useAuth = () => {
    const [token, setToken] = useState(null);
    const [ready, setReady] = useState(false);
    const [refreshToken, setRefreshToken] = useState(null);
    const [userId, setUserId] = useState(null);

    const login = useCallback((jwtToken, jwtRefreshToken, userId) => {
        setToken(jwtToken);
        setRefreshToken(jwtRefreshToken);
        setUserId(userId);

        localStorage.setItem(storageName, JSON.stringify({token: jwtToken, refreshToken: jwtRefreshToken, userId: userId}));
    }, []);

    const logout = useCallback(() => {
        setToken(null);
        setRefreshToken(null);
        setUserId(null);

        localStorage.removeItem(storageName);
    }, []);

    useEffect(() => {
       const data = JSON.parse(localStorage.getItem(storageName));

       if (data) {
           login(data.token, data.refreshToken, data.userId);
       }
       setReady(true);
    }, [login]);

    return { login, logout, token, refreshToken, ready, userId }
}