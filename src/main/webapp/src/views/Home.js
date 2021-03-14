import React from "react";
import DrinkList from "../components/DrinkList.js";
import Details from "../views/Details.js";

export default class Home extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        showDrinkDialog: false,
        selectedDrink: null
    }
  }

  componentDidMount = () => {
    const params = new URLSearchParams(window.location.search);
    if (params.get('drink')) {
      fetch (process.env.REACT_APP_API_URL + "/drinks/"+params.get('drink'),{
        method: "GET"
      })
      .then(response => response.ok ? response.json() : Promise.reject(response.status))
      .then(data => this.setState({selectedDrink: data[0]}))
      .catch((error) => {
        console.error(error);
      })
    }
  }

  closeDetailsDialog = () => {
      this.setState({selectedDrink :null});
  }
      
  render() {
    return (
      <div>
        <h1>Home</h1>
				<DrinkList fetchType="/drinks/"></DrinkList>
                {this.state.selectedDrink
                ? <Details
                    visible={this.state.selectedDrink}
                    drink={this.state.selectedDrink}
                    closeDialog={this.closeDetailsDialog}
                  />
                : ''
                }
      </div>
    );
  }
}
