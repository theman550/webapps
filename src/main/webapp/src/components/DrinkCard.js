import { Button } from "primereact/button";
import { Avatar } from 'primereact/avatar';
import { Chip } from 'primereact/chip';
import Voter from './Voter.js';
import ReactDOM from 'react-dom';
import 'primeflex/primeflex.css';
import { Switch, Route, withRouter }
from 'react-router-dom';
import React, { Component }
from 'react';

import Home from '../views/Home.js';
import Details from '../views/Details.js';
import "./DrinkCard.css";
import Timestamp from "../components/Timestamp.js";
class DrinkCard extends Component {

    constructor(props) {
        super(props);

    }

    navigate = (path) => {
        this.props.history.push(path);
    }

    showDetailsDialog() {
        console.log('Click happened');

    }

    render() {

        const data = this.props.data;

        const tags = data.ingredients.map((ingredient) =>
            <span key={ingredient.name} onClick={() => this.props.handleIngredientTagClick(ingredient.name)}>
                <Chip 
                    className={`ingredient-tag pmr-2 p-mb-2 ${this.props.queries.map((i) => i.name).includes(ingredient.name) ? "selected-ingredient" : ""}`} 
                    label={ingredient.name}>
                </Chip>
            </span>
        );

        return (
                <div className="p-col-12 p-md-4 p-lg-3">
                    <div className="product-grid-item p-shadow-3">
                        <div className="product-grid-item-top">
                            <div>
                                <i className="pi pi-tag product-category-icon"></i>
                                {tags}
                            </div>
                            <Avatar icon="pi pi-user" shape="circle"></Avatar>
                        </div>
                        <div className="product-grid-item-content">
                            <Voter data={data} sendVote={this.props.sendVote}></Voter>
                            <img
                                src={data.image}
                                alt={data.name}
                                />
                            <div>
                                <div className="product-name">{data.name}</div>
                                <div className="product-description">{data.description}</div>
                            </div>
                        </div>
                        <div className="product-grid-item-bottom">
                            <Details 
                                src={data.image}
                                alt={data.name}
                                drinkName={data.name}
                                ingredients= {tags}
                                description={data.description}
                                />
                            <Timestamp 
                               data={data}
                                />
                        </div>
                    </div>
                </div>
                );
    }
}
export default withRouter(DrinkCard);