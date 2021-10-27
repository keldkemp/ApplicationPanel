import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import { Link } from 'react-router-dom';

class ApplicationList extends Component {

    constructor(props) {
        super(props);
        this.state = {applications: []};
    }

    token = localStorage.getItem("token");

    componentDidMount() {
        fetch('/api/applications', {
            headers: {
                'Authorization': `Bearer ${this.token}`
            }
        })
            .then(response => response.json())
            .then(data => this.setState({applications: data}));
    }

    render() {
        const {applications, isLoading} = this.state;

        if (isLoading) {
            return null;
        }

        const appList = applications.map(app => {
            return <tr key={app.id}>
                <td style={{whiteSpace: 'nowrap'}}>{app.name}</td>
                <td>{app.original_name}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/applications/" + app.id}>Редактировать</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <Container fluid>
                    <h3>Мои приложения</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="30%">Наименование</th>
                            <th width="30%">Оригинальное наименование</th>
                            <th width="40%">Кнопки</th>
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

export default ApplicationList;