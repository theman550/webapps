/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import './Login.css';
import InputPage from '../components/TextInput';
import { useState }
from 'react';
import Home from '../views/Home.js';
import About from '../views/About.js';
import App from '../App.js';
import TextInput from '../components/TextInput';

import { Switch, Route, withRouter }
from 'react-router-dom';
import 'primeicons/primeicons.css';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.css';
import 'primeflex/primeflex.css';
import { Divider } from 'primereact/divider';

import ReactDOM from 'react-dom';

import React, { Component }
from 'react';
import { InputText }
from "primereact/inputtext";
import { Button }
from "primereact/button";
import { Checkbox }
from "primereact/checkbox";
import { RadioButton }
from "primereact/radiobutton";
import fresh from '../images/fresh.jpg';
import fresh2 from '../images/fresh2.jpg';
import fresh3 from '../images/fresh3.jpg';
import fresh4 from '../images/fresh4.jpg';

class Login extends Component {

    constructor(props) {
        super(props);
                
        this.state = {
            showRegFields: false,
        };

        //todo: fix this 
        this.state = {
            tempSolutionFix: ''
        };

        this.state = {
            input: [],
            messages: []
        };
        
        this.handleChange = this.handleChange.bind(this);
    }
    
    handleChange(event) {
        let input = this.state.input;
        input[event.target.name] = event.target.value;
  
        this.setState({input});
    }
    
    navigate = (path) => {
        this.props.history.push(path);
    }
    
    toggleReg() {
        this.setState({showRegFields: !this.state.showRegFields})
    }
    
    loginUser() {
        fetch(process.env.REACT_APP_API_URL+'/user/login/'+
        this.state.input.logname+'/'+this.state.input.logpw, {
            method: 'GET',
            headers: {'Content-Type':'application/x-www-form-urlencoded'}
        })    
        .then(response => {
            if(response.ok){
                this.setState({tempSolutionFix: ["Login worked!"]});
                return response.json(); 
            }
            else{
                this.setState({tempSolutionFix: ["No such user"]});
            }
        })
        .then(UserAsJson => {
            console.log(UserAsJson);
            localStorage.setItem('currentUser', JSON.stringify(UserAsJson));
        })
    }

    addUser() {
        if(this.validateReg()){
            fetch(process.env.REACT_APP_API_URL+'/user/create/'+
            this.state.input.regname+'/'+this.state.input.regpw, {
                method: 'POST',
                headers: {'Content-Type':'application/x-www-form-urlencoded'}
            })
            this.setState({messages: ["Welcome " + this.state.input.regname + "!"]});
        }
    }

    validateReg(){
        let input = this.state.input;
        let newErrors = [];
        let isValid = true;
  
        
        if (!input.regname) {
            isValid = false;
            newErrors.push("Must enter name.");
        }

        if (!input.regpw) {
            isValid = false;
            newErrors.push("Must enter password."); 
        }
  
        if (!input.confpw) {
            isValid = false;
            newErrors.push("Must enter confirm password.");
        }
  
        if (input.regpw !== input.confpw) {
            isValid = false;
            newErrors.push("Passwords must match.");
        } 
  
        this.setState({messages: newErrors});
                        
        return isValid;
    }
    
    render() {
        const loginButton = <Button label="Login" onClick={() => this.loginUser()} ></Button>
        const toggleButton = <Button label="Click to register!" onClick={() => this.toggleReg()} ></Button>
        const regButton = <Button label="Register" onClick={() => this.addUser()} ></Button>

        if(localStorage.getItem("currentUser") === null){
            return (
               <div className="login-page">
                
                    <div  className="split left">
                        <div className="background" class="left-background">
                            <div class="container" >
                
                                <img src={fresh}/>
                                <img src={fresh2}/>
                                <img src={fresh3}/>
                                <img src={fresh4}/>
                
                            </div>
                        </div>
                
                    </div>
                
                    <div  className="split right">
                        <div className="card" class="card" >
                            <div class="logo">
                                <h2 class = "login-logo">Login</h2>
                            </div>
                
                            <div className="text-input">
                
                                <div className="p-username" class="username-input">
                                    <div className="p-inputgroup">
                                        <span className="p-inputgroup-addon">
                                            <i className="pi pi-user"></i>
                                        </span>
                                        <InputText 
                                            autoComplete="off"
                                            placeholder="Username" 
                                            name="logname"
                                            onChange={ this.handleChange }
                                            value={ this.state.input.logname }/>
                                    </div>
                                </div>
                                <div className="p-password" class="password-input">
                                    <div className="p-inputgroup">
                                        <span className="p-inputgroup-addon">**</span>
                                         <InputText 
                                            placeholder="Password" 
                                            type="password" 
                                            name="logpw" 
                                            onChange={ this.handleChange }
                                            value={ this.state.input.logpw }/>            
                                    </div>
                                </div>
                            </div>
                
                            {/* <div><TextInput/></div>*/}
                      
                            <div className="p-logButton">
                                <a href="/resetPassword/new">Forgot password?</a>
                                <div className="Login">
                                    <h4>{this.state.tempSolutionFix} </h4>
                                    <div className="btnLogIn">{loginButton}</div>
                                </div>
                            </div>
                            
                            <Divider layout="horizontal">
                                <b>OR</b>
                            </Divider>
                           
                            <div className="register">
                                <h3 class='child inline-block-child'>No account?</h3>
                                <div class='child inline-block-child'>{toggleButton} </div>
                                
                                    {
                                    this.state.showRegFields ?
                                    <div className="text-input">
                    
                                        <div className="p-username" class="username-input">
                                            <div className="p-inputgroup">
                                                <span className="p-inputgroup-addon">
                                                    <i className="pi pi-user"></i>
                                                </span>
                                                <InputText 
                                                    placeholder="Username" 
                                                    name="regname" 
                                                    onChange={ this.handleChange } 
                                                    value={ this.state.input.regname } />
                                            </div>
                                        </div>
                                        <div className="p-password" class="password-input">
                                            <div className="p-inputgroup">
                                                <span className="p-inputgroup-addon">**</span>
                                                <InputText 
                                                    placeholder="Password" 
                                                    type="password" 
                                                    name="regpw" 
                                                    onChange={ this.handleChange } 
                                                    value={ this.state.input.regpw } />              
                                            </div>
                                        </div>
                                        <div className="p-password" class="password-input">
                                            <div className="p-inputgroup">
                                                <span className="p-inputgroup-addon">**</span>
                                                <InputText 
                                                    placeholder="Repeat password" 
                                                    type="password" 
                                                    name="confpw" 
                                                    onChange={ this.handleChange } 
                                                    value={ this.state.input.confpw }  />              
                                            </div>
                                        </div>
                                        
                                        <h3>{this.state.messages[0]} </h3> 
                                        <div>{regButton}</div>
                    
                                    </div>
                                    : null
                                    }
                            </div>
                        </div>
                    </div>
                </div>

                );
                                }
                                else{
                                    return(
                                        <div>
                                            You're already logged in!
                                            (kör kommandot "localStorage.clear()"" i chrome-konsolen så loggas du ut)
                                        </div>
                                    )
                                }
    }

}
export default withRouter(Login);
