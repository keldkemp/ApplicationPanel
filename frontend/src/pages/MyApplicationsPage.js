import React, {useCallback, useEffect, useState} from "react";
import {useHttp} from "../hooks/http.hook";
import {Loader} from "../components/Loader";
import {ApplicationList} from "../components/ApplicationList";

export const MyApplicationsPage = () => {
    const {request, loading} = useHttp();
    const [applicationList, setApplicationList] = useState(null);

    const getApplicationList = useCallback(async () => {
       try {
           const data = await request('/api/applications');
           setApplicationList(data);
       } catch (e) { }
    }, [request]);

    useEffect(() => {
        getApplicationList();
    }, [getApplicationList]);

    if (loading) {
        return <Loader/>
    }

    return (
        <>
            { !loading && applicationList && <ApplicationList applicationList={applicationList} isGitHub={false}/>}
        </>
    )
}