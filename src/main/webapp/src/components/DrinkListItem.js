import { Chip } from 'primereact/chip';
import { Avatar } from 'primereact/avatar';
import Details from '../views/Details.js';
import Voter from './Voter.js';
import './DrinkListItem.css';
import React, { Component } from 'react';
import { Button } from 'primereact/button';
import Timestamp from "../components/Timestamp.js";
import reddrink from "../images/reddrink.png";
export default class DrinkListItem extends Component {

    constructor(props) {
        super(props);
        this.state = {
            detailsVisible: false
        }

    }

    navigate = (path) => {
        this.props.history.push(path);
    }

    openDetailsDialog = () => {
        this.setState({detailsVisible: true});

    }

    closeDetailsDialog = () => {
        this.setState({detailsVisible :false});
    }

    getImgUrl (image) {
        if (image === "") {
            return reddrink;
        }
        return image;
    }

    render(){
        const data = this.props.data;

        const tags = data.ingredients.map((ingredient) =>
            <span key={ingredient.id} onClick={() => this.props.handleIngredientTagClick(ingredient.name)}>
                <Chip 
                    className={`ingredient-tag pmr-2 p-mb-2 ${this.props.queries.map((i) => i.name).includes(ingredient.name) ? "selected-ingredient" : ""}`} 
                    label={ingredient.name}>
                </Chip>
            </span>
        );
        return (
                <div className="p-col-12">
                    <div className="product-list-item">
                        <Voter data={data} sendVote={this.props.sendVote}></Voter>
                        <img
                            src={this.getImgUrl(data.image)}
                            alt={data.name}
                            />
                        <div className="product-list-detail">
                            <i className="pi pi-tag product-category-icon"></i>
                            {tags}
                            <div className="product-name">{data.name}</div>
                            <span className="product-description">{data.description}</span>
                                <div className="list-footer">
                                    <div>
                                        <Button label="Details" className="p-button-text" onClick={this.openDetailsDialog}></Button>
                                        
                                        {/* Only render details when button is clicked */}
                                        {this.state.detailsVisible
                                            ? <Details
                                                visible={this.state.detailsVisible}
                                                drink={data}
                                                openDialog={this.openDetailsDialog}
                                                closeDialog={this.closeDetailsDialog}
                                                />
                                            : ''
                                            }

                                        <Timestamp 
                                            data={data}
                                            />
                                    </div>
                                </div>
                        </div>
                        <div className="product-list-end">
                            <Avatar icon="pi pi-user" shape="circle"></Avatar>
                        </div>
                    </div>
                </div>
                );
        }
}
