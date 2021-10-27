import React, {Component} from 'react';
import {Button, Container, Form, FormGroup, Table} from 'reactstrap';
import {withRouter} from 'react-router-dom';
import {ApplicationDto} from "./dto/ApplicationDto";
import { UncontrolledAlert } from 'reactstrap';

class AllApplicationList extends Component {

    emptyGitHubApplications = {
        git_hub_user_repo_list: [
            {
                id: '',
                name: '',
                full_name: '',
                language: '',
                html_url: ''
            }
        ]
    };

    data = {};

    err = null;

    active = false;

    token = localStorage.getItem("token");

    constructor(props) {
        super(props);
        this.state = {gitHubApplications: this.emptyGitHubApplications, isLoading: false, err: {}, isOpen: false};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    getApplicationDto(obj) {
        return new ApplicationDto(null, null, obj.full_name, obj.html_url,
            null, null, null, null, obj);
    }

    createModalWindow(data) {

    }

    componentDidMount() {
        fetch('/api/applications/all', {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(async (data) => {
                await Promise.all(data.git_hub_user_repo_list.map((app, i) => {
                    return fetch(`api/applications/find?name=${app.full_name}`, {
                        headers: {
                            'Authorization': `Bearer ${this.token}`
                        }
                    })
                        .then(res => res.body.getReader().read())
                        .then(foundedApp => {
                            const parsedData = JSON.parse(new TextDecoder("utf-8").decode(foundedApp.value));
                            if (parsedData.original_name === null) {
                                data.git_hub_user_repo_list[i].loaded = false;
                            } else {
                                data.git_hub_user_repo_list[i].loaded = true;
                            }
                        });
                }));
                this.setState({gitHubApplications: data, isLoading: true});
            })

    }

    handleSubmit = obj => event => {
        event.preventDefault();
        const app = this.getApplicationDto(obj.app);

        fetch('/api/applications/add', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${this.token}`
            },
            body: JSON.stringify(app),
        })
            .then(response => response.json())
            .then(data => {
                if (data.message != null) {
                    this.setState({err: data, isOpen: true});
                }
                else
                    this.setState({applicationDto: data, err: null});
            });
    }

    toggle() {
        this.setState({isOpen: !this.state.isOpen})
    }

    render() {
        const {gitHubApplications} = this.state;
        console.log(this.state.applicationDto)

        if (!this.state.isLoading) {
            return null;
        }

        const appList = gitHubApplications.git_hub_user_repo_list.map(app => {
            return <tr key={app.id}>
                <td style={{whiteSpace: 'nowrap'}}>{app.name}</td>
                <td>{app.full_name}</td>
                <td>{app.language}</td>
                <td>
                    <a target="_blank" rel="noreferrer" href={app.html_url}>{app.html_url}</a></td>
                <td>
                    {
                        !app.loaded &&
                        <Form onSubmit={this.handleSubmit({app})}>
                            <FormGroup>
                                <Button size="sm" color="success" type="submit">Добавить</Button>
                            </FormGroup>
                        </Form>
                    }
                </td>
            </tr>
        });

        return (
            <div>
                { this.state.err != null &&
                    <UncontrolledAlert color="danger" isOpen={this.state.isOpen} toggle={() => this.toggle()}
                                       className={"error-alert"} fade={true}>
                        {this.state.err.message}
                    </UncontrolledAlert>
                }
                {/*<ModalExample isOpen={this.state.isOpenn ? } title='error' text={this.state.err.message} toggle={=}></ModalExample>*/}
                <Container fluid>
                    <h3>Все приложения</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Наименование</th>
                            <th width="20%">Оригинальное наименование</th>
                            <th width="10%">Язык</th>
                            <th width="30%">Url</th>
                            <th width="20%">Кнопки</th>
                        </tr>
                        </thead>
                        <tbody>
                        {appList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default withRouter(AllApplicationList);