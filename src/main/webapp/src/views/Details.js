
/* global props */

import './Details.css';
import { Switch, Route, withRouter } from 'react-router-dom';
import React from 'react';
import { Divider } from 'primereact/divider';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { Tag } from 'primereact/tag';
import { Knob } from 'primereact/knob';
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import reddrink from "../images/reddrink.png";

class Details extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            unitMap: {
                DECILITRE: {unit:'dl',inLitres: 0.1},
                CENTILITRE: {unit: 'cl', inLitres: 0.01},
                MILLILITRE: {unit: 'ml', inLitres: 0.001},
                LITRE: {unit: 'L', inLitres: 1},
                GRAMS: {unit: 'g', inLitres: null},
                PIECES: {unit: 'pcs', inLitres: null}
            },
            totalAlc: 0
        };
    }

    calculateTotalAlc = () => {
        let alcVol = 0;
        let totalVol = 0;
        this.props.drink.ingredients.map((ingredient) => {
            // Only include ingredients measured in volume
            if (ingredient.unit != 'PIECES' && ingredient.unit != 'GRAMS') {
                console.log(ingredient.name);
                // Add the total volume of alcohol, converted into litres
                alcVol += ingredient.amount * (ingredient.abv / 100) * this.state.unitMap[ingredient.unit].inLitres;
                // Add the total volume, converted into litres
                totalVol += ingredient.amount * this.state.unitMap[ingredient.unit].inLitres;
            }
        this.setState({totalAlc: Math.round((alcVol / totalVol) * 100)});
        });
    }

    componentDidMount = () => {
        // Compute the total alc percentage of drink on load
        this.calculateTotalAlc();
        // Add drink id to url
        const params = new URLSearchParams(window.location.search);
        params.set('drink',this.props.drink.id);
        window.history.replaceState({},'', `${window.location.pathname}?${params}`);
    }

    onDelete = () => {
        fetch(process.env.REACT_APP_API_URL + "/drinks/" + this.props.drink.id, {
            method: "DELETE",
            headers: {'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`}
        })
                .then(response => response.ok ? "" : Promise.reject(response.status))
                .then(() => {
                                
                                const params = new URLSearchParams(window.location.search)
                                params.delete('drink')
                                window.history.replaceState({},'', `${window.location.pathname}?${params}`);
                                window.location.reload()
                            })
                .catch((error) => {
                    console.error(error);
                });
    }

    onHide = () => {
        // Remove drink id from url when exiting details view
        const params = new URLSearchParams(window.location.search);
        params.delete('drink');
        window.history.replaceState({},'', `${window.location.pathname}?${params}`);
        // Close the dialog
        this.props.closeDialog();
    }

    render() {
        const renderFooter = () => {
            return(
                <div>
                    {isCreator
                        ? <Button label="Delete" icon="pi pi-trash" className="p-button-danger deleteButton" tooltip="Delete this drink" onClick={() => this.onDelete()}/>
                        : ""
                    }
                </div>
            );
        }
        const isCreator = JSON.parse(localStorage.getItem("currentUser"))?.username == this.props.drink.user.accountName
        const header = (
            <div>
                Submitted by {this.props.drink.user.displayName}
            </div>
        );
        const ingredients = this.props.drink.ingredients.map((ingredient) => 
            <div key={ingredient.name} className="ingredient">
                <div className="p-d-flex p-flex-row amount">
                    <div className="p-mr-1 p-text-bold">{ingredient.amount}</div>
                    {/* Map the unit name to a more presentable format (DECILITRE -> dl) if possible */}
                    <div className="p-mr-1 p-text-bold">{this.state.unitMap[ingredient.unit] ? this.state.unitMap[ingredient.unit].unit : ingredient.unit}</div>
                </div>
                <div className="p-d-flex p-flex-row name">
                    <div className="p-mr-1">{ingredient.name}</div>
                    <div className="p-mr-1 tag"><Tag value={ingredient.abv + `%`}></Tag></div>
                </div>

            </div>); 
        return (
                <div className="mainDetails">
                    <Dialog 
                        className="detailsDialog"                         
                        visible={this.props.visible}
                        dismissableMask={true}
                        header={header}
                        width='350px'
                        onHide={this.onHide}
                        modal={true}
                        maximizable={true}
                        closeOnEscape={true}
                        footer={renderFooter}
                        >
                        <div className="p-d-lg-flex content p-jc-between">
                            <div className="leftSide" style={{backgroundImage: `linear-gradient(0deg,#00000088 30%, #ffffff44 100%), url(${this.props.drink.image == "" ? reddrink : this.props.drink.image})`}}>
                                <h3 className="drinkName">{this.props.drink.name}</h3>
                            </div>
                            <div className="desc">
                                         
                            <Divider align="left">
                                <div className="p-d-inline-flex p-ai-center">
                                    <i className=""></i>
                                    <b>Description</b>
                                </div>
                            </Divider>
                            <div className="description">{this.props.drink.description}</div>
                            <div className="stats p-mt-3 p-d-flex">
                                <div className="p-text-center p-text-bold">
                                    <Knob
                                        id="alcoholKnob" 
                                        value={this.state.totalAlc}
                                        size={75}
                                        readOnly={true}
                                    />
                                    <span>ALC %</span>
                                </div>
                            </div>

                            <Divider align="left">
                                <div className="p-d-inline-flex p-ai-center">
                                    <b>Ingredients</b>
                                </div>
                            </Divider>
                                <div className="ingredients">
                                    {ingredients}
                                </div>
                            </div>
                        </div>
                    </Dialog>
                </div>
                );
    }
}
export default withRouter(Details);