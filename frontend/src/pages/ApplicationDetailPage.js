import {useHttp} from "../hooks/http.hook";
import React, {useCallback, useEffect, useState} from "react";
import {Loader} from "../components/Loader";
import {useParams} from "react-router-dom";
import {ApplicationDetail} from "../components/ApplicationDetail";


export const ApplicationDetailPage = () => {
    const {request, loading} = useHttp();
    const [application, setApplication] = useState(null);
    const appId = useParams().id;

    const getApplication = useCallback(async () => {
        try {
            const data = await request(`/api/applications/${appId}`);
            setApplication(data);
        } catch (e) { }
    }, [request, appId]);

    useEffect(() => {
        getApplication();
    }, [getApplication]);

    if (loading) {
        return <Loader/>
    }

    return (
        <>
            { !loading && application && <ApplicationDetail app={application}/> }
        </>
    )
}