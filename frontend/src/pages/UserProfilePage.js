import React, {useCallback, useContext, useEffect, useState} from "react";
import {useHttp} from "../hooks/http.hook";
import {AuthContext} from "../context/AuthContext";
import {Loader} from "../components/Loader";
import {UserProfile} from "../components/UserProfile";

export const UserProfilePage = () => {
    const context = useContext(AuthContext);
    const {request, loading} = useHttp();
    const [user, setUser] = useState(null);

    const getUser = useCallback(async () => {
        try {
            const data = await request(`/api/users/${context.userId}`);
            setUser(data);
        } catch (e) {
            console.log(e);
        }
    }, [context.userId, request]);

    useEffect(() => {
        getUser();
    }, [getUser]);

    if (loading) {
        return <Loader/>
    }

    return (
        <>
            { !loading && user && <UserProfile user_={user}/> }
        </>
    );
}