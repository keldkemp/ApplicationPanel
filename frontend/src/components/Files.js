import React, {useCallback, useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {useMessage} from "../hooks/message.hook";
import {useHttp} from "../hooks/http.hook";

export const Files = ({filesList}) => {
    const message = useMessage();
    const {loading, request, error, clearError} = useHttp();
    const [fileList, setFileList] = useState(filesList);
    const [fileEdit, setFileEdit] = useState(null);
    const appId = useParams().id;

    useEffect(() => {
        message(error);
        clearError();
    }, [error, message, clearError])

    useEffect(() => {
        window.M.updateTextFields();
    }, [])

    const clickFileHandler = useCallback(async (file) => {
        try {
            const data = await request(`/api/applications/${file.application_id}/file?name=${file.file_name}`);
            
            setFileEdit(data);
        } catch (e) { }
    }, [request]);

    const sendChangeFileHandler = useCallback(async (event) => {
        event.preventDefault();
        try {
            const data = await request(`/api/applications/${fileEdit.application_id}/file`, 'POST', fileEdit)
            setFileEdit(data);
            
            message('Файл изменен!')
        } catch (e) { }
    }, [fileEdit, message, request]);
    
    const fileTextHandler = useCallback(async (event) => {
        setFileEdit({ ...fileEdit, [event.target.name]: event.target.value})
    }, [fileEdit]);
    
    const listFiles = fileList.map(file => {
        return <div onClick={() => clickFileHandler(file)}
                    className={file.file_name === (fileEdit && fileEdit.file_name) && "active-app"}>
            {file.file_name}
        </div>
    })

    return (
        <div>
            <h5>Файлы</h5>
            <div className={"apps"}>
                <div className={"apps-table"}>
                    {listFiles}
                </div>
                <div className={"app-info"}>
                    {
                        fileEdit &&
                        <div className="input-field">
                            <textarea  className="materialize-textarea" id="textarea" name="text"
                                       value={fileEdit.text || ''}
                                       onChange={fileTextHandler}
                                       style={{height: '500px'}}
                                       disabled={loading}
                                   />
                            <form onSubmit={sendChangeFileHandler}>
                                <button className="waves-effect waves-light btn" type="submit">Ок</button>
                            </form>
                        </div>
                    }
                </div>

            </div>

            <p/>

            <Link to={`/applications/${appId}`}>
                <button className="btn grey darken-3">Назад</button>
            </Link>
        </div>
    )
}