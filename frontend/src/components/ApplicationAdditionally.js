import React, {useCallback, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useMessage} from "../hooks/message.hook";
import {useHttp} from "../hooks/http.hook";

export const ApplicationAdditionally = ({appId}) => {
    const [dockerInspectData, setDockerInspectData] = useState(null);
    const message = useMessage();
    const {loading, request, error, clearError} = useHttp();
    
    const dockerComposeUrl = `/api/applications/docker/${appId}/start`;
    const dockerContainerStartUrl = `/api/applications/docker/${appId}/container/start`;
    const dockerContainerStopUrl = `/api/applications/docker/${appId}/container/stop`;
    

    useEffect(() => {
        message(error);
        clearError();
    }, [error, message, clearError])

    useEffect(() => {
        window.M.updateTextFields();
    }, [])
    
    const dockerInspectHandler = useCallback(async () => {
        try {
            const data = await request(`/api/applications/docker/${appId}/inspect`);
            setDockerInspectData(data);
        } catch (e) { }
    }, [appId, request]);
    
    const dockerHandler = useCallback(async (url) => {
        try {
            const data = await request(url);

            if (data.status) {
                message('Ok');
            } else {
                message(data.result || 'Что-то пошло не так. Посмотрите Inspect');
            }
        } catch (e) { }
    }, [message, request]);

    return (
        <div>
            <div className="col m6">
                <div className="card grey lighten-4">
                    <div className="card-content black-text">
                        <span className="card-title" style={{fontWeight: "bold"}}>Комманды</span>
                        <div style={{marginTop: '2%'}}>
                            <button className="waves-effect waves-light btn"
                                    onClick={() => dockerHandler(dockerComposeUrl)}
                                    disabled={loading}>DockerCompose start</button>
                        </div>
                        <div style={{marginTop: '1%'}}>
                            <button className="waves-effect waves-light btn"
                                    onClick={() => dockerHandler(dockerContainerStartUrl)}
                                    disabled={loading}>Container start</button>
                        </div>
                        <div style={{marginTop: '1%'}}>
                            <button className="waves-effect waves-light btn"
                                    onClick={() => dockerHandler(dockerContainerStopUrl)}
                                    disabled={loading}>Container stop</button>
                        </div>
                        <div style={{marginTop: '1%'}}>
                            <button className="waves-effect waves-light btn" onClick={dockerInspectHandler} disabled={loading}>Inspect</button>
                        </div>
                    </div>
                </div>
            </div>

            {
                dockerInspectData != null &&
                <div className="col m6">
                    <div className="card grey lighten-4">
                        <div className="card-content black-text">
                            <span className="card-title" style={{fontWeight: "bold"}}>Inspect</span>
                            <div>
                                <table className="mt-4">
                                    <thead>
                                    <tr>
                                        <th width="10%">Статус</th>
                                        <th width="5%">Запущен</th>
                                        <th width="5%">Рестарт</th>
                                        <th width="5%">Пауза</th>
                                        <th width="10%">Запустился в</th>
                                        <th width="10%">Закончил в</th>
                                        <th width="20%">Ошибка</th>
                                        <th width="5%">Dead</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <tr key={dockerInspectData.Id}>
                                            <td>{dockerInspectData.State.Status}</td>
                                            <td>{dockerInspectData.State.Running ? "Ok" : "Off"}</td>
                                            <td>{dockerInspectData.State.Restarting ? "Ok" : "Off"}</td>
                                            <td>{dockerInspectData.State.Paused ? "Ok" : "Off"}</td>
                                            <td>{new Date(dockerInspectData.State.StartedAt).toLocaleString()}</td>
                                            <td>{new Date(dockerInspectData.State.FinishedAt).toLocaleString()}</td>
                                            <td>{dockerInspectData.State.Error || ''}</td>
                                            <td>{dockerInspectData.State.Dead ? "Ok" : "Off"}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            }

            <Link to={`/applications/${appId}`}>
                <button className="btn grey darken-3">Назад</button>
            </Link>

        </div>
    );
}