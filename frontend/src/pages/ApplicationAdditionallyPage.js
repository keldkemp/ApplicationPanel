import React from "react";
import {useParams} from "react-router-dom";
import {ApplicationAdditionally} from "../components/ApplicationAdditionally";

export const ApplicationAdditionallyPage = () => {
    const appId = useParams().id;

    return (
        <ApplicationAdditionally appId={appId}/>
    );
}