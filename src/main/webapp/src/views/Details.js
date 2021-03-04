
import './Details.css';
import App from '../App.js';
import AddDrink from '../views/AddDrink.js';
import { Switch, Route, withRouter } from 'react-router-dom';
import React from 'react';
import { Divider } from 'primereact/divider';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';

class Details extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        const data = this.props.data;
        return (
                <div className="mainDetails">
                    <div>
                        <Dialog 
                            className="detailsDialog"                         
                            footer={this.footer}
                            visible={this.state.visible}
                            width='350px'
                            modal={true}
                            onHide={e => this.setState({visible: false})}
                            maximizable={true}
                            >
                            <div className="header">
                                <img className="mainImg"/>
                                <h3 className="drinkName">Drink name</h3>
                            </div>
                
                            <form className="mainForm">
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Description</b>
                                    </div>
                                </Divider>
                
                                <div className="description">description content Lorem ipsum dolor sit
                                    amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et 
                                    dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation
                                    ullamco laboris nisi ut aliquip ex ea commodo consequat. 
                                    Duis aute irure dolor in reprehenderit in voluptate velit es
                                    se cillum dolore eu fugiat nulla pariatur.
                                    Excepteur sint occaecat cupidatat non proident, sunt in culp
                                    a qui officia deserunt mollit anim id est laborum.</div>
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Ingredients</b>
                                    </div>
                                </Divider>
                
                                <div className="ingredients">ingredients content Lorem ip
                                    sum dolor sit amet, consectetur adipiscing elit, sed do 
                                    eiusmod tempor incididunt ut labore et dolore magna aliqua. 
                                    Ut enim ad minim veniam, quis nostrud exercitation
                                    ullamco laboris nisi ut aliquip ex ea commodo consequat.
                                    Duis aute irure dolor in reprehenderit in voluptate velit
                                    esse cillum dolore eu fugiat nulla pariatur.
                                    Excepteur sint occaecat cupidatat non proident, sunt in
                                    culpa qui officia deserunt mollit anim id est laborum.</div>
                            </form>
                
                        </Dialog>
                
                        <Button
                            label='Details'
                            icon='pi pi-info-circle'
                            onClick={e => this.setState({visible: true}
                                    )}
                            />
                    </div>
                
                
                
                
                </div>
                );
    }
}
export default withRouter(Details);