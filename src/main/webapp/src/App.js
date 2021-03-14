import React from 'react';
import './App.css';
import { Switch, Route, withRouter } from 'react-router-dom';
import { Menubar } from 'primereact/menubar';
import { Button } from 'primereact/button';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import Home from './views/Home.js';
import About from './views/About.js';
import Login from './views/Login.js';
import PrimeReact from 'primereact/api';
import { SelectItem } from 'primereact/api';
import { MenuItem } from 'primereact/api';
import Details from './views/Details.js';
import AddDrink from './views/AddDrink.js';
import Profile from './views/Profile.js';
class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: "User",
            token: null
        }
    }

    navigate = (path) => {
        this.props.history.push(path);
    }

    onLogin = (data) => {
        this.setState({username: data.username, token: data.token});
    }

    componentDidMount = () => {
        // Load potentially logged in user
        this.setState({username: JSON.parse(localStorage.getItem("currentUser"))?.username});
    }

    render() {
        const items = [
            {
                label: 'Home',
                command: () => {
                    this.navigate('/');
                }
            },
            {
                label: 'Add drink',
                command: () => {
                    this.navigate('/AddDrink');
                }
            },
            {
                label: 'About',
                command: () => {
                    this.navigate('/about');
                }
            }
        ];
        PrimeReact.ripple = true;  //button effect


        const logOut = () => {
            localStorage.removeItem("currentUser");
            window.location.reload();
        }

        const navbrand = <img alt="logo" src="./reddrink512.png" height="40" className="pmr"></img>


        const renderEnd = () => {
            if (localStorage.getItem("currentUser") === null) {
                return(
                        <div>
                            <Button label="Log in" onClick={() => this.navigate('/Login')} ></Button>
                        </div>
                        )
            } else {
                return(
                        <div>
                            <Button label="Log out" onClick={logOut} ></Button>
                            <Button icon="pi pi-user" className="p-button-rounded p-button-plain p-ml-2" onClick={() => this.navigate('/profile')}></Button>
                        </div>
                        )
            }
        }

        return (
                <div className="App">
                    <Menubar model={items} start={navbrand} end={renderEnd}></Menubar>
                    <Switch>
                    <Route path="/login">
                        <Login onLogin={this.onLogin}></Login>
                    </Route>             
                    <Route path="/about">
                        <About></About>
                    </Route>
                    <Route path="/AddDrink">
                        <AddDrink/>
                    </Route>
                    <Route path="/profile">
                        <Profile user={this.state.username}/>
                    </Route>
                    <Route path="/">
                        <Home></Home>
                    </Route>
                    </Switch>
                </div>
                );
    }
}

export default withRouter(App);
