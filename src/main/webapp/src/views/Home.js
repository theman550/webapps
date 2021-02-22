
import React from "react";
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Dropdown } from 'primereact/dropdown';

import DrinkCard from '../components/DrinkCard';
import DrinkListItem from '../components/DrinkListItem';

import './Home.css';

export default class Home extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      layout: "grid",
      sortOrder: null,
      sortField: null,
      sortOptions: [
        {
          label: "Most popular", value: "!voteCount"
        },
        {
          label: "Newest", value: "!datePosted"
        },
        {
          label: "Oldest", value: "datePosted"
        }
      ],
      drinks: 
        null,
    };
  }
  
  sendVote = (id, isUpvote) => {
    // First update the state
    if (isUpvote) {
      this.setState((state, props) => (
        {drinks: state.drinks.map(el => (el.id === id ? {...el, voteCount: el.voteCount + 1} : el))}
      ));
    }
    else {
      this.setState((state, props) => (
        {drinks: state.drinks.map(el => (el.id === id ? {...el, voteCount: el.voteCount - 1} : el))}
      ));
    }
  }

  componentDidMount() {
    fetch("http://localhost:8080/drink/ws/drinkre")
            .then(res => res.json())
            .then((data) => {this.setState({drinks: data})
	}).catch(console.log);   
  }

  onSortChange = (event) => {
    // TODO: Should fetch data from backend instead of sorting existing entries
    const value = event.value;
    if (value.indexOf('!') === 0) {
      this.setState({sortOrder: -1, sortField: value.substring(1,value.length), sortKey: value});
    } else {
      this.setState({sortOrder: 1, sortField: value, sortKey: value});

    }
  }


  render() {
    const header = (
      <div className="p-grid p-nogutter">
        <div className="p-col-6" style={{ textAlign: "left" }}>
          <Dropdown
            options={this.state.sortOptions}
            value={this.state.sortKey}
            optionLabel="label"
            placeholder="Most popular"
            onChange={this.onSortChange}
          />
        </div>
        <div className="p-col-6" style={{ textAlign: "right" }}>
          <DataViewLayoutOptions
            layout={this.state.layout}
            onChange={(e) => this.setState({layout: e.value})}
          />
        </div>
      </div>
    );

    const itemTemplate = (data, layout) => {
      if (layout === "list") {
        return <DrinkListItem data={data} sendVote={this.sendVote}></DrinkListItem>;
      }
      if (layout === "grid") {
        return <DrinkCard data={data} sendVote={this.sendVote}></DrinkCard>;
      }
    };
    return (
      <div>
        <h1>Home</h1>
        <DataView
          value={this.state.drinks}
          layout={this.state.layout}
          sortOrder={this.state.sortOrder}
          sortField={this.state.sortField}
          header={header}
          itemTemplate={itemTemplate}
        ></DataView>
      </div>
    );
  }
}