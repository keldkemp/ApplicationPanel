import React, {Component} from 'react';
import {Button, Col, Container, Form, FormGroup, Input, ListGroup, ListGroupItem, Row, Table} from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';

class ApplicationFiles extends Component {

    files = [
        {
            file_name: null,
            path: null,
            application_id: null,
            text: null
        }
    ];

    token = localStorage.getItem("token");

    constructor(props) {
        super(props);
        this.state = {isLoading: false, files: this.files, appId: this.props.match.params.id, fileEdit: null, isSendChange: false};
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.sendChange = this.sendChange.bind(this);
    }

    componentDidMount() {
        fetch(`/api/applications/${this.state.appId}/files`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({files: data, isLoading: true}));
    }

    handleSubmit = obj => event => {
        event.preventDefault();

        fetch(`/api/applications/${this.state.appId}/file?name=${obj.file.file_name}`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                const files = this.state.files;

                files.map((file, i) => {
                    if (file.file_name === data.file_name) {
                        files[i].text = data.text;
                    }
                    return file;
                })

                this.setState({files: files, fileEdit: data});
            });
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let fileEdit = {...this.state.fileEdit};
        fileEdit[name] = value;
        this.setState({fileEdit});
    }

    sendChange(event) {
        event.preventDefault();
        this.setState({isSendChange: true});

        fetch(`/api/applications/${this.state.appId}/file`, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${this.token}`
                },
                body: JSON.stringify(this.state.fileEdit),
            })
            .then(response => response.json())
            .then(data => {
                const files = this.state.files;

                files.map((file, i) => {
                    if (file.file_name === data.file_name) {
                        files[i].text = data.text;
                    }
                    return file;
                })

                this.setState({files: files, fileEdit: data, isSendChange: false});
            });
    }

    render() {
        if (!this.state.isLoading) {
            return null;
        }

        const fileList = this.state.files.map(file => {
            return <div onClick={this.handleSubmit({file})} className={file.file_name === (this.state.fileEdit && this.state.fileEdit.file_name) && "active-app"}>{file.file_name}</div>
        })

        return (
            <div>
                <Container fluid>
                    <h5>Файлы</h5>
                    <div className={"apps"}>
                        <div className={"apps-table"}>
                            {fileList}
                        </div>
                        <div className={"app-info"}>
                            {
                                this.state.fileEdit &&
                                <div>
                                    <Input type="textarea" name="text" id="text"
                                           style={{height: '500px'}} value={this.state.fileEdit.text || ''}
                                    onChange={this.handleChange}/>
                                    <p></p>
                                    <Form onSubmit={this.sendChange}>
                                        <Button disabled={this.state.isSendChange} color="success" type="submit">Ок</Button>
                                    </Form>
                                </div>
                            }
                        </div>
                    </div>

                    <p></p>

                    <FormGroup>
                        <Button color="secondary" tag={Link} to={`/applications/${this.state.appId}`}>Назад</Button>
                    </FormGroup>
                </Container>
            </div>
        );
    }
}

export default withRouter(ApplicationFiles);