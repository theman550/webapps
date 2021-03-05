import React from "react";
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Dropdown } from 'primereact/dropdown';

import Search from '../components/Search';
import DrinkCard from '../components/DrinkCard';
import DrinkListItem from '../components/DrinkListItem';
import Dashboard from "../components/Dashboard";

import './DrinkList.css';

export default class DrinkList extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      layout: "grid",
      sortKey: null,
      searchQueries: [],
      totalRecords: null,
      first: 0,
      sortOptions: [
        {
          label: "Most popular", value: "popular"
        },
        {
          label: "Newest", value: "new"
        }
      ],
      drinks:
        [],
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
    // Set default sorting choice (and call fetchDrinks)
    this.setState((state) => ({sortKey: state.sortOptions[0].value}),this.fetchDrinks);
  }

  // When user selects a new sorting option
  onSortChange = (event) => {
    this.setState({sortKey: event.value}, this.fetchDrinks);
  }

	fetchDrinks = () => {
    fetch(process.env.REACT_APP_API_URL+this.props.fetchType+this.state.sortKey, {
      method: "POST",
      body:"{\"offset\":"+this.state.first+",\"queries\":" + JSON.stringify(this.state.searchQueries)+"}"
    })
    .then(response =>  response.ok ? response.json() : Promise.reject(response.status))
    .then(data => this.setState({drinks: data.drinks, totalRecords: data.total}))
    .catch((error) => {
      console.error(error);
    });
  }

  // When the user enters a new search tag
  onQueryChange = (event) => {
    this.setState({searchQueries: event.value}, this.fetchDrinks);
  }

  // When the user clicks on an ingredient name in the search results
  handleIngredientTagClick = (ingredient) => {
    if (this.state.searchQueries.map((i) => i.name).includes(ingredient)) {
      // Remove ingredient from query list
      this.setState((state) => ({searchQueries: state.searchQueries.filter((i) => i.name !== ingredient)}), this.fetchDrinks);
    } else {
      // Add ingredient to query list
      this.setState((state) => ({searchQueries: [...state.searchQueries, {name:ingredient,type:"ingredient"}]}), this.fetchDrinks);

    }
  }
onPage = (event) => {
  this.setState ((state) => ({
      first: event.first,
    }),
    this.fetchDrinks
  )
}
	render() {
    const header = (
      <div className="p-grid p-nogutter">
        {/* <Dashboard/> */}
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
      if (!data) {
        return null;
      }
      if (layout === "list") {
        return <DrinkListItem data={data} handleIngredientTagClick={this.handleIngredientTagClick} queries={this.state.searchQueries} sendVote={this.sendVote}></DrinkListItem>;
      }
      if (layout === "grid") {
        return <DrinkCard data={data} handleIngredientTagClick={this.handleIngredientTagClick} queries={this.state.searchQueries} sendVote={this.sendVote}></DrinkCard>;
      }
    };
		return (
			<div className="drink-view">
				<DataView
						value={this.state.drinks}
						layout={this.state.layout}
						sortOrder={this.state.sortOrder}
						sortField={this.state.sortField}
						header={header}
						itemTemplate={itemTemplate}
            paginator={true}
            alwaysShowPaginator={true}
            paginatorPosition={"both"}
            emptyMessage={"No records found"}
            onPage={(e) => this.onPage(e)}
            lazy={true}
            first={this.state.first}
            totalRecords={22}
            rows={20} //rows = nr. elements according to the ABSOLUTE BUFOONS @primefaces
				></DataView>
			</div>
		);
	}
}

