import {useMessage} from "../hooks/message.hook";
import {useHttp} from "../hooks/http.hook";
import React, {useCallback, useEffect, useState} from "react";
import {Link} from "react-router-dom";


export const UserProfile = ({user_}) => {
    const message = useMessage();
    const {loading, request, error, clearError} = useHttp();
    const [user, setUser] = useState({...user_})
    const [userPasswordDto, setUserPasswordDto] = useState({old_password: null, new_password: null})

    useEffect(() => {
        message(error);
        clearError();
    }, [error, message, clearError]);

    useEffect(() => {
        window.M.updateTextFields();
    }, []);

    const editUserHandler = useCallback(async (event) => {
        event.preventDefault();
        try {
            await request(`/api/users/${user.id}`, 'POST', user);

            message('Сохранено!');
        } catch (e) { }
    }, [message, request, user]);
    
    const editPasswordHandler = useCallback(async (event) => {
        event.preventDefault();
        try {
            await request(`/api/users/${user.id}/change-password`, 'POST', userPasswordDto);
            
            message('Пароль успешно изменен!');
            setUserPasswordDto({old_password: null, new_password: null});
        } catch (e) { }
    }, [message, request, user.id, userPasswordDto])

    const handleChange = async (event) => {
        setUser({...user, [event.target.name]: event.target.value})
    }

    const handleChangePassword = async (event) => {
        setUserPasswordDto({...userPasswordDto, [event.target.name]: event.target.value})
    }

    return (
        <div>
            <div className="row">
                <div className="col m6">
                    <div className="card grey lighten-4">
                        <div className="card-content black-text">
                            <span className="card-title" style={{fontWeight: "bold"}}>{user.login}</span>
                            <div>
                                <form>
                                    <form-group>
                                        <label className="black-text" htmlFor="name">Имя</label>
                                        <input type="text" name="name" id="name" value={user.name || ''}
                                               autoComplete="name" onChange={handleChange}/>
                                    </form-group>

                                    <form-group>
                                        <button className="btn green darken-2" onClick={editUserHandler} disabled={loading}>Сохранить</button>
                                        {' '}
                                        <Link to="/applications">
                                            <button className="btn grey darken-3">Отмена</button>
                                        </Link>
                                    </form-group>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div className="row">
                <div className="col m6">
                    <div className="card grey lighten-4">
                        <div className="card-content black-text">
                            <span className="card-title" style={{fontWeight: "bold"}}>Смена пароля</span>
                            <div>
                                <form>
                                    <form-group>
                                        <label className="black-text" htmlFor="old_password">Старый пароль</label>
                                        <input type="password" name="old_password" id="old_password" value={userPasswordDto.old_password || ''}
                                               autoComplete="old_password" onChange={handleChangePassword}/>
                                    </form-group>

                                    <form-group>
                                        <label className="black-text" htmlFor="new_password">Новый пароль</label>
                                        <input type="password" name="new_password" id="new_password" value={userPasswordDto.new_password || ''}
                                               autoComplete="new_password" onChange={handleChangePassword}/>
                                    </form-group>

                                    <form-group>
                                        <button className="btn green darken-2" onClick={editPasswordHandler} disabled={loading}>Сменить пароль</button>
                                    </form-group>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}