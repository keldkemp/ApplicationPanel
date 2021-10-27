import React, { Component } from 'react';
import {Container, Form, FormGroup, Input, Label, Table, Button, UncontrolledAlert} from 'reactstrap';
import { Link, withRouter } from 'react-router-dom';

class ApplicationDetail extends Component {

    dockerDto = {
        application_id: null,
        application_name: null,
        application_original_name: null,
        result: null,
        status: null
    };

    emptyApplication = {
        name: null,
        original_name: null,
        status_applications: null,
        update_date_cron_format: null,
        last_update: null,
        is_auto_update: null,
        github_url: null,
        github_repo: {
            id: null,
            name: null,
            full_name: null,
            html_url: null,
            language: null
        }
    };

    options = {
        is_create_db: false,
        is_start_app: false
    };

    files = [
        {
            file_name: null,
            path: null,
            application_id: null,
            text: null
        }
    ];

    emptyTag = <div>-</div>;

    token = localStorage.getItem("token");

    constructor(props) {
        super(props);
        this.state = {application: this.emptyApplication, isLoading: false, isDeploy: false,
            options: this.options, files: this.files, docker: this.dockerDto, isOpen: false};
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleDeploy = this.handleDeploy.bind(this);
        this.changeOptions = this.changeOptions.bind(this);
        this.handleUpdateApp = this.handleUpdateApp.bind(this);
    }

    async componentDidMount() {
        await fetch(`/api/applications/${this.props.match.params.id}`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({application: data, isLoading: true}));
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let application = {...this.state.application};
        application[name] = value;
        this.setState({application});
    }

    async handleSubmit(event) {
        event.preventDefault();

        await fetch('/api/applications/edit', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.state.application),
        });
        //this.props.history.push('/applications');
    }

    async handleUpdateApp(event) {
        event.preventDefault();
        this.setState({isDeploy: true});

        await fetch('/api/applications/update', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(this.state.application),
        })
            .then(response => response.json()).then(data => this.setState({docker: data, isOpen: true}));
        this.setState({isDeploy: false});
    }

    async handleDeploy(event) {
        event.preventDefault();
        this.setState({isDeploy: true});
        const {application} = this.state;
        application.options = this.state.options;

        await fetch('/api/applications/deploy', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(application),
        });
        this.setState({isDeploy: false});
    }

    changeOptions(event) {
        const target = event.target;
        const value = target.checked;
        const name = target.name;
        let options = {...this.state.options};
        options[name] = value;
        this.setState({options});
    }

    toggle() {
        this.setState({isOpen: !this.state.isOpen})
    }

    render() {
        const {application} = this.state;

        if (!this.state.isLoading) {
            return null;
        }

        return (
            <div>
                <Container fluid>
                    <h3>{application.name}</h3>
                    <div className="row border-top mb-2">
                        <div className="container-fluid mt-1">
                            <div className="row">
                                <div className="col">

                                    { this.state.docker.result != null &&
                                    <UncontrolledAlert color="info" isOpen={this.state.isOpen} toggle={() => this.toggle()}
                                                       className={"error-alert"} fade={true}>
                                        <br/>
                                        Status: {this.state.docker.status ? 'true' : 'false'}<br/>
                                        Message: {this.state.docker.result}
                                    </UncontrolledAlert>
                                    }

                                    <Form onSubmit={this.handleSubmit}>
                                        <FormGroup>
                                            <Label for="name">Наименование</Label>
                                            <Input type="text" name="name" id="name" value={application.name || ''}
                                                   onChange={this.handleChange} autoComplete="name"/>
                                        </FormGroup>
                                        <FormGroup>
                                            <Label for="original_name">Полное наименование</Label>
                                            <Input readOnly={true} type="text" name="original_name" id="original_name" value={application.original_name || ''}
                                                   onChange={this.handleChange} autoComplete="original_name"/>
                                        </FormGroup>
                                        <FormGroup>
                                            <Label for="status_applications">Статус приложения</Label>
                                            <Input readOnly={true} type="number" name="status_applications" id="status_applications" value={application.status_applications || 0}
                                                   onChange={this.handleChange} autoComplete="status_applications"/>
                                        </FormGroup>
                                        <FormGroup>
                                            <Label for="is_auto_update">Автоматическое обновление</Label>
                                            <Input type="number" min={0} max={1} name="is_auto_update" id="is_auto_update" value={application.is_auto_update || ''}
                                                   onChange={this.handleChange} autoComplete="is_auto_update"/>
                                        </FormGroup>
                                        <FormGroup>
                                            <Label for="last_update">Крон для автоматического обновления</Label>
                                            <Input type="text" placeholder='секунды минуты часы день месяц день_в_недели' name="update_date_cron_format" id="update_date_cron_format" value={application.update_date_cron_format || ''}
                                                   onChange={this.handleChange} autoComplete="update_date_cron_format"/>
                                        </FormGroup>
                                        <FormGroup>
                                            <Label for="last_update">Последнее обновление</Label>
                                            <Input readOnly={true} type="datetime" name="last_update" id="last_update" value={new Date(application.last_update).toLocaleString() || ''}
                                                   onChange={this.handleChange} autoComplete="last_update"/>
                                        </FormGroup>

                                        <p></p>

                                        <FormGroup>
                                            <Button color="primary" type="submit">Сохранить</Button>{' '}
                                            <Button color="secondary" tag={Link} to="/applications">Отмена</Button>{' '}
                                            <Button color="secondary" tag={Link} to={`/applications/${application.id}/files`}>Файлы</Button>{' '}
                                            <Button color="secondary" tag={Link} to={`/applications/${application.id}/additionally`}>Дополнительно</Button>
                                        </FormGroup>
                                    </Form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <p></p>

                    <h5>GitHub</h5>
                    <div className="row border-top mb-2">
                        <div className="container-fluid mt-1">
                            <div className="row">
                                <div className="col-3 border-right">
                                    <div className="font-weight-bold">Id:</div>
                                    <div className="font-weight-bold">Наименование:</div>
                                    <div className="font-weight-bold">Полное наименование:</div>
                                    <div className="font-weight-bold">Язык:</div>
                                    <div className="font-weight-bold">Url:</div>
                                </div>
                                <div className="col">
                                    <div>{application.github_repo.id} </div>
                                    <div>{application.github_repo.name} </div>
                                    <div>{application.github_repo.full_name} </div>
                                    <div>{application.github_repo.language} </div>
                                    <a target="_blank" rel="noreferrer" href={application.github_repo.html_url}>{application.github_repo.html_url} </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <p></p>

                    <h5>Инструменты</h5>
                    <div className="row border-top mb-2"/>
                    <Form onSubmit={this.handleUpdateApp}>
                        <FormGroup>
                            <Button disabled={this.state.isDeploy} color="secondary" type="submit">Обновить приложение</Button>
                        </FormGroup>
                    </Form>
                    <br/>
                    {
                        (this.state.application.status_applications === 0 || this.state.application.status_applications === null) &&

                        <Form onSubmit={this.handleDeploy}>
                            <FormGroup>
                                <Label for="is_create_db">Создать базу?</Label>{' '}
                                <Input type="checkbox" name="is_create_db" id="is_create_db"
                                       onChange={this.changeOptions} autoComplete="is_create_db"/>
                            </FormGroup>
                            <FormGroup>
                                <Label for="is_start_app">Запустить приложение?</Label>{' '}
                                <Input type="checkbox" name="is_start_app" id="is_start_app"
                                       onChange={this.changeOptions} autoComplete="is_start_app"/>
                            </FormGroup>
                            <p></p>
                            <Button disabled={this.state.isDeploy} color="primary" type="submit">Деплой</Button>
                        </Form>
                    }
                </Container>
            </div>
        );
    }
}

export default withRouter(ApplicationDetail);