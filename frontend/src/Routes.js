import React from 'react';
import {Redirect, Route, Switch} from "react-router-dom";
import {AuthPage} from "./pages/AuthPage";
import {MyApplicationsPage} from "./pages/MyApplicationsPage";
import {ApplicationDetailPage} from "./pages/ApplicationDetailPage";
import {GitHubApplicationsPage} from "./pages/GitHubApplicationsPage";
import {FilesPage} from "./pages/FilesPage";
import {ApplicationAdditionallyPage} from "./pages/ApplicationAdditionallyPage";
import {UserProfilePage} from "./pages/UserProfilePage";

export const Routes = isAuth => {
    if (isAuth) {
        return (
            <Switch>
                <Route path='/applications' exact={true} component={MyApplicationsPage}/>
                <Route path='/applications/:id/additionally' component={ApplicationAdditionallyPage}/>
                <Route path='/applications/:id/files' component={FilesPage}/>
                <Route path='/applications/:id' component={ApplicationDetailPage}/>
                <Route path='/all' exact={true} component={GitHubApplicationsPage}/>
                <Route path='/profile' component={UserProfilePage}/>
                <Redirect to="/applications"/>
            </Switch>
        );
    }

    return (
      <Switch>
          <Route path='/' exact={true} component={AuthPage}/>
          <Redirect to="/"/>
      </Switch>
    );
};