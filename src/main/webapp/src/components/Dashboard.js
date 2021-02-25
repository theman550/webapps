import React, { Component } from "react";
import {AddDrinkPanel} from "./AddDrinkPanel.js";
import { Button } from 'primereact/button';

class Dashboard extends Component {
  constructor() {
    super();
    this.state = {
      show: false
    };
    this.showAddDrinkPanel = this.showAddDrinkPanel.bind(this);
    this.hideAddDrinkPanel = this.hideAddDrinkPanel.bind(this);
  }

  showAddDrinkPanel = () => {
    this.setState({ show: true });
  };

  hideAddDrinkPanel = () => {
    this.setState({ show: false });
  };
  
  render() {
      return (
        <main>
            <AddDrinkPanel show={this.state.show} handleClose={this.hideAddDrinkPanel}/>
            <Button class="p-button p-component" disabled={this.state.show} onClick={this.showAddDrinkPanel}>
            {!this.state.show ? 'Add drink' : 'You are adding a drink or the site is broken'}
            </Button>
        </main>
    );
    }
}
export default Dashboard