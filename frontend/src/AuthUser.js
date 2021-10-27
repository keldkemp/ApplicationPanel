import React, {Component} from 'react';
import {withRouter} from "react-router-dom";

class AuthUser extends Component {
    state = {
        login: "",
        password: ""
    }

    constructor(props) {
        super(props);
        this.state = {auth: this.state};
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let auth = {...this.state.auth};
        auth[name] = value;
        this.setState({auth});
    }

    handleSubmit = event => {
        event.preventDefault();
        this.userLogin();
    }

    userLogin() {
        fetch(`/auth/login`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this.state.auth),
        })
            .then(response => response.json())
            .then(data => {
                if (data.token != null) {
                    localStorage.setItem("token", data.token);
                    this.props.history.push("/applications");
                }
            });
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <h1>Login</h1>

                <label>Username</label>
                <input
                    name='login'
                    placeholder='login'
                    value={this.state.login}
                    onChange={this.handleChange}
                /><br/>

                <label>Password</label>
                <input
                    type='password'
                    name='password'
                    placeholder='Password'
                    value={this.state.password}
                    onChange={this.handleChange}
                /><br/>

                <input type='submit'/>
            </form>
        )
    }
}

export default withRouter(AuthUser);