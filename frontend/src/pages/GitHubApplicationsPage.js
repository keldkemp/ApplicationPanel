import {useHttp} from "../hooks/http.hook";
import React, {useCallback, useEffect, useState} from "react";
import {Loader} from "../components/Loader";
import {ApplicationList} from "../components/ApplicationList";


export const GitHubApplicationsPage = () => {
    const {request, loading} = useHttp();
    const [applicationList, setApplicationList] = useState(null);

    const getApplicationList = useCallback(async () => {
        try {
            await request('/api/applications/all')
                .then(async (data) => {
                    await Promise.all(data.git_hub_user_repo_list.map(async (app, i) => {
                        const response = await request(`/api/applications/find?name=${app.full_name}`)

                        return data.git_hub_user_repo_list[i].loaded = response.original_name !== null;
                    }))
                    setApplicationList(data);
                });
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
            { !loading && applicationList && <ApplicationList applicationList={applicationList} isGitHub={true}/>}
        </>
    )
}