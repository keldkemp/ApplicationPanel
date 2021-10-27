import React, {Component} from 'react';
import {Navbar, NavbarBrand, Button} from 'reactstrap';
import {Link} from 'react-router-dom';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {isOpen: false, isAuth: false};
        this.toggle = this.toggle.bind(this);
    }

    toggle() {
        this.setState({
            isOpen: !this.state.isOpen
        });
    }

    render() {
        return (
            <header className="p-3 bg-dark text-white">
                <div className="container">
                    <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">

                        <ul className="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                            <li><a href="/applications" className="nav-link px-2 text-white">Мои приложения</a></li>
                            <li><a href="/all" className="nav-link px-2 text-white">Все приложения</a></li>
                        </ul>

                        {
                            !this.isAuth &&
                            <div className="text-end">
                                <Button type="button" className="btn btn-outline-light me-2" tag={Link} to={'/auth/login'}>Login</Button>
                            </div>
                        }
                    </div>
                </div>
            </header>
        );
    }
}