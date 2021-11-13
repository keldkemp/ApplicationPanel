import {useState, useCallback, useContext} from "react";
import {AuthContext} from "../context/AuthContext";

export const useHttp = () => {
    const checkAuthUrl = '/api/stats/check'
    const auth = useContext(AuthContext);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const updateRefreshToken = useCallback(async () => {
        const resp = await fetch('/auth/refreshtoken', {
            method: 'POST',
            body: JSON.stringify({'refreshToken': `${auth.refreshToken}`}),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.token}`
            }
        })

        if (!resp.ok) {
            return false;
        }

        const data = await resp.json();
        
        auth.login(data.accessToken, data.refreshToken, data.userId);

        return data.accessToken;
    }, [auth]);

    const request = useCallback(async (url, method = 'GET', body = null,
                                       headers = 
                                           {
                                               'Accept': 'application/json', 
                                               'Content-Type': 'application/json', 
                                               'Authorization': null
                                           }) => {
        setLoading(true);
        if (auth.isAuth) {
            headers.Authorization = `Bearer ${auth.token}`;

            const resp = await fetch(checkAuthUrl, {method: 'GET', headers: headers});

            if (resp.status === 401 || resp.status === 403) {
                const token = await updateRefreshToken();

                if (!token) {
                    auth.logout();
                    return false;
                }
                
                headers.Authorization = `Bearer ${token}`;
            }
        }

        if (body) {
            body = JSON.stringify(body);
        }

        try {
            const response = await fetch(url, {method, body, headers});
            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Что-то пошло не так');
            }

            return data;
        } catch (e) {
            setError(e.message);
            throw e;
        } finally {
            setLoading(false);
        }
    }, [auth, updateRefreshToken]);

    const clearError = useCallback(() => setError(null), []);

    return { loading, request, error, clearError }
}