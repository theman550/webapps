
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
            
        };
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
                                <h3 className="drinkName">{this.state.drinkName}</h3>
                            </div>
                
                            <form className="mainForm">
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Description</b>
                                    </div>
                                </Divider>
                
                                <div className="description">{this.state.description}</div>
                
                                <Divider align="left">
                                    <div className="p-d-inline-flex p-ai-center">
                                        <i className=""></i>
                                        <b>Ingredients</b>
                                    </div>
                                </Divider>
                
                                <div className="ingredients">{this.state.ingredients}</div>
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