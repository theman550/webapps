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
import AddDrink from './views/AddDrink.js';
class App extends React.Component {

    constructor(props) {
        super(props);
    }

    navigate = (path) => {
        this.props.history.push(path);
    }

    render() {
        const items = [
            {
                label: 'Home',
                command: () => {
                    this.navigate('/')
                }
            },
            {
              label: 'Add drink',
              command: ()=>{this.navigate('/AddDrink')}
            },
            {
                label: 'About',
                command: () => {
                    this.navigate('/about')
                }
            }

        ]
        PrimeReact.ripple = true;  //button effect

        const navbrand = <img alt="logo" src="./logo512.png" height="40" className="pmr"></img>
        const loginButton = <Button label="Log in" onClick={() => this.navigate('/Login')} ></Button>
        return (
                <div className="App">
                    <Menubar model={items} start={navbrand} end={loginButton}></Menubar>
                    <Switch>
                    <Route path="/login">
                        <Login></Login>
                    </Route>
                    <Route path="/about">
                        <About></About>
                    </Route>
                    <Route path="/AddDrink">
                        <AddDrink/>
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