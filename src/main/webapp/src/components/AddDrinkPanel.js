import React, { Component } from 'react'
import 'primeflex/primeflex.css';
import { InputText } from 'primereact/inputtext';
import { Button } from 'primereact/button';

import './AddDrinkPanel.css';

export const AddDrinkPanel = ({ handleClose, show}) => {
  const showHideClassName = show ? "AddDrinkPanel display-block" : "AddDrinkPanel display-none";

  return (
    <div className={showHideClassName}>
      <div className="p-fluid">
        <div className="p-field">
            <label htmlFor="drinkName">Drink name</label>
            <InputText id="drinkName" type="text"/>
        </div>
        <div className="p-field">
            <label htmlFor="description">Description</label>
            <InputText id="description" type="text"/>
        </div>
        <Button class="p-button p-component" onClick={handleClose}>Submit drink </Button>
      </div>
    </div>
  );
};


/* 
  return(
    <div className={showHideClassName}>
      <div className="p-fluid">
        <div className="p-field">
            <label htmlFor="drinkName">Drink name</label>
            <InputText id="drinkName" type="text"/>
        </div>
        <div className="p-field">
            <label htmlFor="description">Description</label>
            <InputText id="description" type="text"/>
        </div>
        <Button class="p-button p-component" onClick={handleClose}>Submit drink </Button>
      </div>
    </div>
    );

  render() {
    return (
      <div className="addDrinkComponent">
        <button class="p-button p-component" onClick={this.handleClick} disabled={!this.state.isVisible}>
          {this.state.isVisible ? 'Add drink' : 'You are adding a drink or the site is broken'}
        </button>
        <div className="inputForm">
          {this.renderInputForm()}
        </div>
      </div>
    );
  } */