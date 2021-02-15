import React from 'react';
import './App.css';
import { Switch, Route, withRouter } from 'react-router-dom';
import { Menubar } from 'primereact/menubar';
import { Button } from 'primereact/button';

import Home from './views/Home.js';
import About from './views/About.js';

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
      command: ()=>{this.navigate('/')}
    },
    {
      label: 'About',
      command: ()=>{this.navigate('/about')}
    }
  ]

  const navbrand = <img alt="logo" src="./logo512.png" height="40" className="pmr"></img>
  const loginButton = <Button label="Log in"></Button>
  return (
    <div className="App">
      <Menubar model={items} start={navbrand} end={loginButton}></Menubar>
      <Switch>
        <Route path="/about">
          <About></About>
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