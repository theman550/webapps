
/* global props */

import './Details.css';
import { Switch, Route, withRouter } from 'react-router-dom';
import React from 'react';
import { Divider } from 'primereact/divider';
import { Dialog } from 'primereact/dialog';
import { Button } from 'primereact/button';
import { Tag } from 'primereact/tag';
import { Knob } from 'primereact/knob';

class Details extends React.Component {

    constructor(props) {

        super(props);
        this.state = {
            visible: false,
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
    openDialog = () => {
        this.setState({visible: true});
    }

    closeDialog = () => {
        this.setState({visible: false});
    }

    calculateTotalAlc = () => {
        let alcVol = 0;
        let totalVol = 0;
        this.props.ingredients.map((ingredient) => {
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
    }

    render() {
        const data = this.props.data;
        const header = (
            <div>
                {this.props.creator}
            </div>
        );
        const ingredients = this.props.ingredients.map((ingredient) => 
            <div className="ingredient">
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
                    <Button label="Details" className="detailsButton p-button-text" onClick={this.openDialog}>
                    </Button>
                    <Dialog 
                        className="detailsDialog"                         
                        visible={this.state.visible}
                        header={header}
                        width='350px'
                        modal={true}
                        onHide={this.closeDialog}
                        maximizable={true}
                        closeOnEscape={true}
                        >
                        <div className="content p-jc-between">
                            <div className="leftSide" style={{backgroundImage: `linear-gradient(0deg,#00000088 30%, #ffffff44 100%), url(${this.props.src})`}}>
                                <h3 className="drinkName">{this.props.drinkName}</h3>
                            </div>
                            <div className="desc">
                                         
                            <Divider align="left">
                                <div className="p-d-inline-flex p-ai-center">
                                    <i className=""></i>
                                    <b>Description</b>
                                </div>
                            </Divider>
                            <div className="description">{this.props.description}</div>
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