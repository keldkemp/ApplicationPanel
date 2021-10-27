import React, {Component} from 'react';
import {
    Button,
    Col,
    Container,
    Form,
    FormGroup,
    Input,
    ListGroup,
    ListGroupItem,
    Row,
    Table,
    UncontrolledAlert
} from 'reactstrap';
import {Link, withRouter} from 'react-router-dom';

class ApplicationAdditionally extends Component {

    dockerDto = {
        application_id: null,
        application_name: null,
        application_original_name: null,
        result: null,
        status: null
    };

    dockerInspectDto = {
        Created: null,
        Id: null,
        State: {
            Dead: null,
            Error: null,
            ExitCode: null,
            FinishedAt: null,
            OOMKilled: null,
            Paused: null,
            Pid: null,
            Restarting: null,
            Running: null,
            StartedAt: null,
            Status: null
        }
    };

    token = localStorage.getItem("token");

    constructor(props) {
        super(props);
        this.state = {isLoading: true, appId: this.props.match.params.id, docker: this.dockerDto, isOpen: false,
            dockerInspectDto: this.dockerInspectDto};
        this.dockerComposeStart = this.dockerComposeStart.bind(this);
        this.dockerContainerStart = this.dockerContainerStart.bind(this);
        this.dockerContainerStop = this.dockerContainerStop.bind(this);
        this.dockerInspect = this.dockerInspect.bind(this);
    }

    toggle() {
        this.setState({isOpen: !this.state.isOpen})
    }

    componentDidMount() {

    }

    dockerComposeStart(event) {
        event.preventDefault();

        fetch(`/api/applications/docker/${this.state.appId}/start`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({docker: data, isOpen: true}));
    }

    dockerContainerStart(event) {
        event.preventDefault();

        fetch(`/api/applications/docker/${this.state.appId}/container/start`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({docker: data, isOpen: true}));
    }

    dockerContainerStop(event) {
        event.preventDefault();

        fetch(`/api/applications/docker/${this.state.appId}/container/stop`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({docker: data, isOpen: true}));
    }

    dockerInspect(event) {
        event.preventDefault();

        fetch(`/api/applications/docker/${this.state.appId}/inspect`, {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({dockerInspectDto: data, isOpen: true, docker: this.dockerDto}));
    }

    render() {
        let dockerInspectData = null;

        if (!this.state.isLoading) {
            return null;
        }

        if (this.state.dockerInspectDto.Id != null) {
            const inspect = this.state.dockerInspectDto;
            const dataStart = new Date(inspect.State.StartedAt)
            const dataEnd = new Date(inspect.State.FinishedAt)

            dockerInspectData = (
                <tr key={inspect.Id}>
                    <td>{inspect.State.Status}</td>
                    <td>{inspect.State.Running ? "Ok" : "Off"}</td>
                    <td>{inspect.State.Restarting ? "Ok" : "Off"}</td>
                    <td>{inspect.State.Paused ? "Ok" : "Off"}</td>
                    <td>{dataStart.toLocaleString()}</td>
                    <td>{dataEnd.toLocaleString()}</td>
                    <td>{inspect.State.Error || ''}</td>
                    <td>{inspect.State.Dead ? "Ok" : "Off"}</td>
                </tr>
            );
        }

        return (
            <div>
                <Container fluid>
                    { this.state.docker.result != null &&
                    <UncontrolledAlert color="info" isOpen={this.state.isOpen} toggle={() => this.toggle()}
                                       className={"error-alert"} fade={true}>
                        <br/>
                        Status: {this.state.docker.status ? 'true' : 'false'}<br/>
                        Message: {this.state.docker.result}
                    </UncontrolledAlert>
                    }

                    <Form onSubmit={this.dockerComposeStart}>
                        <FormGroup>
                            <Button color="primary" type="submit">DockerCompose start</Button>
                        </FormGroup>
                    </Form>

                    <p></p>

                    <Form onSubmit={this.dockerContainerStart}>
                        <FormGroup>
                            <Button color="primary">Container start</Button>
                        </FormGroup>
                    </Form>

                    <p></p>

                    <Form onSubmit={this.dockerContainerStop}>
                        <FormGroup>
                            <Button color="primary">Container stop</Button>
                        </FormGroup>
                    </Form>

                    <p></p>

                    <Form onSubmit={this.dockerInspect}>
                    <FormGroup>
                        <Button color="primary">Inspect</Button>
                    </FormGroup>
                    </Form>

                    <p></p>

                    {
                        dockerInspectData != null &&
                        <h3>Docker Inspect</h3> &&
                        <Table className="mt-4">
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
                                {dockerInspectData}
                            </tbody>
                        </Table>
                    }

                    <p></p>

                    <FormGroup>
                        <Button color="secondary" tag={Link} to={`/applications/${this.state.appId}`}>Назад</Button>
                    </FormGroup>
                </Container>
            </div>
        );
    }
}

export default withRouter(ApplicationAdditionally);