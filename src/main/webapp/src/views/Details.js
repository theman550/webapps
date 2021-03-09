
/* global props */

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
        this.state = {
            drinkImage: null,
            drinkName: "Drink name",
            ingredients: "ingredints content",
            description: "description content",
            visible: false 
        };
        this.openDialog = this.openDialog.bind(this);
        this.closeDialog = this.closeDialog.bind(this);
    }
    openDialog() {
        this.setState({visible: true});
    }

    closeDialog() {
        this.setState({visible: false});
    }

    render() {
        const data = this.props.data;
        return (
                <div className="mainDetails">
                    <Button label="Details" className="detailsButton p-button-text" onClick={this.openDialog}>
                        <Dialog 
                            className="detailsDialog"                         
                            footer={this.footer}
                            visible={this.state.visible}
                            width='350px'
                            modal={true}
                            onHide={e => this.setState({visible: false})}
                            maximizable={true}
                            onRequestClose={this.closeDialog}
                            >
                            <div className="header">
                                <img className="mainImg" src={this.props.src} alt={this.props.alt}/>
                                <h3 className="drinkName">{this.props.drinkName}</h3>
                            </div>
                
                            <form className="mainForm">
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Description</b>
                                    </div>
                                </Divider>
                
                                <div className="description">{this.props.description}</div>
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Ingredients</b>
                                    </div>
                                </Divider>
                
                                <div className="ingredients">{this.props.ingredients}</div>
                            </form>
                
                        </Dialog>
                    </Button>
                </div>
                );
    }
}
export default withRouter(Details);