import React, {useCallback, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useMessage} from "../hooks/message.hook";
import {useHttp} from "../hooks/http.hook";


export const ApplicationList = ({applicationList, isGitHub}) => {
    const message = useMessage();
    const {loading, request, error, clearError} = useHttp();
    const [newState, setNewState] = useState(null);
    let listApp;

    useEffect(() => {
        message(error);
        clearError();
    }, [error, message, clearError])

    useEffect(() => {
        window.M.updateTextFields();
    }, [])
    
    const updateApplicationList = useCallback(async (app) => {
        const githubId = app.github_repo.id;
        const objIndx = applicationList.git_hub_user_repo_list.findIndex((obj => obj.id === githubId));

        applicationList.git_hub_user_repo_list[objIndx].loaded = true;
        setNewState(githubId);
    }, [applicationList]);

    const addHandler = useCallback(async ({app}) => {
        const newApp = {'name': app.full_name, 'original_name': app.full_name, 'github_url': app.html_url, 'github_repo': app}
        try {
            const data = await request('/api/applications/add', 'POST', newApp);
            await updateApplicationList(data);
            message('Приложение добавлено!')
        } catch (e) { }
    }, [message, request, updateApplicationList]);

    if (isGitHub) {
        listApp = applicationList.git_hub_user_repo_list.map(app => {
            return <tr key={app.id}>
                <td>{app.full_name}</td>
                <td>{app.language}</td>
                <td>
                    <a className="black-text" target="_blank" rel="noreferrer"
                       href={app.html_url}>{app.html_url}</a>
                </td>
                <td>
                    {!app.loaded && <button className="waves-effect waves-light btn" onClick={() => addHandler({app})} disabled={loading}>Добавить</button>}
                </td>
            </tr>
        });
    } else {
        listApp = applicationList.map(app => {
            return <tr key={app.id}>
                <td style={{whiteSpace: 'nowrap'}}>{app.name}</td>
                <td>{app.original_name}</td>
                <td>{app.github_repo.language}</td>
                <td>
                    <a className="black-text" target="_blank" rel="noreferrer"
                       href={app.github_url}>{app.github_url}</a>
                </td>
                <td>
                    <Link to={"/applications/" + app.id}>
                        <button className="waves-effect waves-light btn">Редактировать</button>
                    </Link>
                </td>
            </tr>
        });
    }

    return (
        <div className="row">
            <div className="col s12">
                <div className="card grey lighten-4">
                    <div className="card-content black-text">
                        <span className="card-title" style={{fontWeight: "bold"}}>Мои приложения</span>
                        <table className="highlight">
                            <thead>
                            <tr>
                                {!isGitHub && <th width="30%">Наименование</th>}
                                <th width="30%">Оригинальное наименование</th>
                                <th width="10%">Язык</th>
                                <th width="30%">Url</th>
                                <th width="40%">Кнопки</th>
                            </tr>
                            </thead>
                            <tbody>
                            {listApp}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    )
}