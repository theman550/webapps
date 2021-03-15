
/* global props */

import './Details.css';
import App from '../App.js';
import AddDrink from '../views/AddDrink.js';
import { Switch, Route, withRouter } from 'react-router-dom';
import React from 'react';
import { Divider } from 'primereact/divider';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';

class Details extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            visible: false 
        };
    }
    openDialog = () => {
        this.setState({visible: true});
    }

    closeDialog = () => {
        this.setState({visible: false});
    }

    render() {
        const data = this.props.data;
        const ingredients = this.props.ingredients.map((ingredient) => 
            <div className="ingredient">
                <div className="p-mr-4">name: {ingredient.name}</div>
                <div className="p-mr-4">amount: {ingredient.amount}</div>
                <div className="p-mr-4">unit: {ingredient.unit}</div>
                <div className="p-mr-4">percentage: {ingredient.abv}</div>
            </div>);
        return (
                <div className="mainDetails">
                    <Button label="Details" className="detailsButton p-button-text" onClick={this.openDialog}>
                    </Button>
                    <Dialog 
                        className="detailsDialog"                         
                        footer={this.footer}
                        visible={this.state.visible}
                        width='350px'
                        modal={true}
                        onHide={this.closeDialog}
                        maximizable={true}
                        closeOnEscape={true}
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
                            <div className="ingredients">
                                {ingredients}
                            </div>
                        </form>
            
                    </Dialog>
                </div>
                );
    }
}
export default withRouter(Details);