import './App.css';
import React, {Component} from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import Home from "./Home";
import ApplicationList from "./ApplicationList";
import ApplicationDetail from "./ApplicationDetail";
import AllApplicationList from "./AllAplicationList";
import AppNavbar from "./AppNavbar";
import ApplicationFiles from "./ApplicationFiles";
import ApplicationAdditionally from "./ApplicationAdditionally";
import AuthUser from "./AuthUser";

class App extends Component {

  render() {
    return (
        <Router>
            <AppNavbar/>
            <p/>
          <Switch>
              <Route path='/' exact={true} component={AuthUser}/>
              <Route path='/applications' exact={true} component={ApplicationList}/>
              <Route path='/applications/:id/additionally' component={ApplicationAdditionally}/>
              <Route path='/applications/:id/files' component={ApplicationFiles}/>
              <Route path='/applications/:id' component={ApplicationDetail}/>
              <Route path='/all' exact={true} component={AllApplicationList}/>
              <Route path='/auth/login' exact={true} component={AuthUser}/>
          </Switch>
        </Router>
    )
  }
}

export default App;
