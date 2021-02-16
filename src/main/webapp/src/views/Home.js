import React from "react";
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Dropdown } from 'primereact/dropdown';

import Search from '../components/Search';
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
      searchQueries: [],
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
      drinks: [
        {
          id: '1',
          name: "en drink",
          ingredients: ["vodka", "cyanid", "citron"],
          description: "en mycket god drink",
          voteCount: 32,
          datePosted: "2020",
          image:
            "https://grandbaby-cakes.com/wp-content/uploads/2019/12/New-Orleans-Hurricane-Drink-2.jpg",
        },
        {
          id: '2',
          name: "en drink till",
          description: "lorem ipsum",
          voteCount: 33,
          datePosted: "2021",
          ingredients: ["rom", "coke"],
        },
      ],
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

  onSortChange = (event) => {
    // TODO: Should fetch data from backend instead of sorting existing entries
    const value = event.value;
    if (value.indexOf('!') === 0) {
      this.setState({sortOrder: -1, sortField: value.substring(1,value.length), sortKey: value});
    } else {
      this.setState({sortOrder: 1, sortField: value, sortKey: value});

    }
  }

  onQueryChange = (event) => {
    this.setState({searchQueries: event.value});
  }

  handleIngredientTagClick = (ingredient) => {
    if (this.state.searchQueries.includes(ingredient)) {
      // Remove ingredient from query list
      this.setState((state) => ({searchQueries: state.searchQueries.filter((i) => i !== ingredient)}));
    } else {
      // Add ingredient to query list
      this.setState((state) => ({searchQueries: [...state.searchQueries, ingredient]}));

    }
  }

  render() {
    const header = (
      <div className="p-grid p-nogutter">
        <div className="p-col-10 p-d-sm-flex" style={{ textAlign: "left" }}>
          <Dropdown
            options={this.state.sortOptions}
            value={this.state.sortKey}
            optionLabel="label"
            placeholder="Most popular"
            onChange={this.onSortChange}
          />
          <div className="p-ml-sm-3 p-mt-2 p-mt-sm-0">
            <Search 
              searchQueries={this.state.searchQueries} 
              onQueryChange={this.onQueryChange} 
            />
          </div>
        </div>
        <div className="p-col-2" style={{ textAlign: "right" }}>
          <DataViewLayoutOptions
            layout={this.state.layout}
            onChange={(e) => this.setState({layout: e.value})}
          />
        </div>
      </div>
    );

    const itemTemplate = (data, layout) => {
      if (layout === "list") {
        return <DrinkListItem data={data} handleIngredientTagClick={this.handleIngredientTagClick} queries={this.state.searchQueries} sendVote={this.sendVote}></DrinkListItem>;
      }
      if (layout === "grid") {
        return <DrinkCard data={data} handleIngredientTagClick={this.handleIngredientTagClick} queries={this.state.searchQueries} sendVote={this.sendVote}></DrinkCard>;
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