import React, {useCallback, useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {useMessage} from "../hooks/message.hook";
import {useHttp} from "../hooks/http.hook";

export const ApplicationDetail = ({app}) => {
    const message = useMessage();
    const [application, setApplication] = useState({...app});
    const [options, setOptions] = useState({is_create_db: false, is_start_app: false});
    const {loading, request, error, clearError} = useHttp();

    useEffect(() => {
        message(error);
        clearError();
    }, [error, message, clearError])

    useEffect(() => {
        window.M.updateTextFields();
    }, [])

    const handleChange = async (event) => {
        setApplication({...application, [event.target.name]: event.target.value})
    }

    const handleChangeOptions = async (event) => {
        setOptions({...options, [event.target.name]: event.target.checked})
    }
    
    const saveHandle = useCallback(async (event) => {
        event.preventDefault();
        try {
            await request('/api/applications/edit', 'POST', application)

            message('Сохранено!');
        } catch (e) { }
    }, [application, message, request]);

    const updateAppHandler = useCallback( async () => {
        try {
            const data = await request('/api/applications/update', 'POST', application)

            if (data.status) {
                message('Приложение обновлено!');
            } else {
                message(data.result);
            }

        } catch (e) { }
    }, [application, message, request]);
    
    const deployAppHandler = useCallback(async (event) => {
        event.preventDefault();
        const app = application
        app.options = options;

        console.log(app);
        try {
            const data = await request('/api/applications/deploy', 'POST', app)

            if (data.status) {
                message('Deploy success');
            } else {
                message(data.result || 'Что-то пошло не так. Посмотрите Inspect');
            }
        } catch (e) { }
    }, [application, message, options, request]);

    return (
        <div>
            <div className="row">
                <div className="col m6">
                    <div className="card grey lighten-4">
                        <div className="card-content black-text">
                            <span className="card-title" style={{fontWeight: "bold"}}>{application.name}</span>
                            <div>
                                <form>
                                    <form-group>
                                        <label className="black-text" htmlFor="name">Наименование</label>
                                        <input type="text" name="name" id="name" value={application.name || ''}
                                               autoComplete="name" onChange={handleChange}/>
                                    </form-group>
                                    <form-group>
                                        <label className="black-text" htmlFor="original_name">Полное наименование</label>
                                        <input readOnly={true} type="text" name="original_name" id="original_name"
                                               value={application.original_name || ''}/>
                                    </form-group>
                                    <form-group>
                                        <label className="black-text" htmlFor="status_applications">Статус приложения</label>
                                        <input readOnly={true} type="number" name="status_applications"
                                               id="status_applications" value={application.status_applications || 0}
                                               autoComplete="status_applications"/>
                                    </form-group>
                                    <form-group>
                                        <label className="black-text" htmlFor="is_auto_update">Автоматическое обновление</label>
                                        <input type="number" min={0} max={1} name="is_auto_update" id="is_auto_update"
                                               value={application.is_auto_update || ''}
                                               autoComplete="is_auto_update"
                                               onChange={handleChange}/>
                                    </form-group>
                                    <form-group>
                                        <label className="black-text" htmlFor="last_update">Крон для автоматического обновления</label>
                                        <input type="text" placeholder='секунды минуты часы день месяц день_в_недели'
                                               name="update_date_cron_format" id="update_date_cron_format"
                                               value={application.update_date_cron_format || ''}
                                               autoComplete="update_date_cron_format"
                                               onChange={handleChange}/>
                                    </form-group>
                                    <form-group>
                                        <label className="black-text" htmlFor="last_update">Последнее обновление</label>
                                        <input readOnly={true} type="datetime" name="last_update" id="last_update"
                                               value={new Date(application.last_update).toLocaleString() || ''}
                                               autoComplete="last_update"/>
                                    </form-group>

                                    <form-group>
                                        <button className="btn green darken-2" onClick={saveHandle} disabled={loading}>Сохранить</button>
                                        {' '}
                                        <Link to="/applications">
                                            <button className="btn grey darken-3">Отмена</button>
                                        </Link>
                                        {' '}
                                        <Link to={`/applications/${application.id}/files`}>
                                            <button className="waves-effect waves-light btn">Файлы</button>
                                        </Link>
                                        {' '}
                                        <Link to={`/applications/${application.id}/additionally`}>
                                            <button className="waves-effect waves-light btn">Дополнительно</button>
                                        </Link>
                                    </form-group>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col m6">
                    <div className="card grey lighten-4">
                        <div className="card-content black-text">
                            <span className="card-title" style={{fontWeight: "bold"}}>GitHub</span>
                            <div>
                                <label className="black-text" htmlFor="github_id">Id</label>
                                <input readOnly={true} type="number" name="github_id"
                                       id="github_id" value={application.github_repo.id || 0}
                                       autoComplete="github_id"/>

                                <label className="black-text" htmlFor="github_name">Наименование</label>
                                <input readOnly={true} type="text" name="github_name"
                                       id="github_name" value={application.github_repo.name || ''}
                                       autoComplete="github_name"/>

                                <label className="black-text" htmlFor="github_full_name">Полное наименование</label>
                                <input readOnly={true} type="text" name="github_full_name"
                                       id="github_full_name" value={application.github_repo.full_name || ''}
                                       autoComplete="github_full_name"/>

                                <label className="black-text" htmlFor="github_language">Язык</label>
                                <input readOnly={true} type="text" name="github_language"
                                       id="github_language" value={application.github_repo.language || ''}
                                       autoComplete="github_language"/>

                                <label className="black-text" htmlFor="github_url">Url</label>
                                <a target='_blank' rel="noreferrer" href={application.github_repo.html_url}>
                                    <input className="blue-text" readOnly={true} type="url" name="github_url"
                                           id="github_url" value={application.github_repo.html_url || ''}
                                           autoComplete="github_url"/>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="col s12">
                    <div className="divider"/>
                    <div className="section">
                        <div className="card grey lighten-4">
                            <div className="card-content black-text">
                                <span className="card-title" style={{fontWeight: "bold"}}>Инструменты</span>
                                <button className="waves-effect waves-light btn" onClick={updateAppHandler} disabled={loading}>Обновить приложение</button>
                            </div>

                            <div className="card-content black-text">
                                {
                                    (application.status_applications === 0 || application.status_applications === null) &&
                                    <form>
                                        <p>
                                            <label>
                                                <input type="checkbox" onChange={handleChangeOptions} name="is_create_db"/>
                                                <span>Создать базу?</span>
                                            </label>
                                        </p>
                                        <p>
                                            <label>
                                                <input type="checkbox" onChange={handleChangeOptions} name="is_start_app"/>
                                                <span>Запустить приложение?</span>
                                            </label>
                                        </p>
                                        <button className="waves-effect waves-light btn" onClick={deployAppHandler} disabled={loading}>Деплой</button>
                                    </form>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}