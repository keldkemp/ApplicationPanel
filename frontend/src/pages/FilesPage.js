import React, {useCallback, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {useHttp} from "../hooks/http.hook";
import {Loader} from "../components/Loader";
import {Files} from "../components/Files";

export const FilesPage = () => {
    const appId = useParams().id;
    const {request, loading} = useHttp();
    const [fileList, setFileList] = useState(null);

    const getFileList = useCallback(async () => {
        try {
            const data = await request(`/api/applications/${appId}/files`);
            setFileList(data);
        } catch (e) { }
    }, [appId, request]);

    useEffect(() => {
        getFileList();
    }, [getFileList]);

    if (loading) {
        return <Loader/>
    }

    return (
        <>
            { !loading && fileList && <Files filesList={fileList}/>}
        </>
    );
}