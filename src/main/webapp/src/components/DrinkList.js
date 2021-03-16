import React from "react";
import { DataView, DataViewLayoutOptions } from 'primereact/dataview';
import { Dropdown } from 'primereact/dropdown';

import Search from '../components/Search';
import DrinkCard from '../components/DrinkCard';
import DrinkListItem from '../components/DrinkListItem';
import { Button } from 'primereact/button';

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
                    label: "Newest", value: "newest"
                }
            ],
            drinks:
                    [],
        };
    }
    sendVote = (id, isUpvote, response) => {
        // First update the state
        if (isUpvote) { 
            this.setState((state, props) => (
                        {drinks: state.drinks.map(el => (el.id === id ? {...el, voteCount: response, voteStatus: (el.voteStatus != 1 ? 1 : 0)} : el))}
                ));
        } else {
            this.setState((state, props) => (
                        {drinks: state.drinks.map(el => (el.id === id ? {...el, voteCount: response, voteStatus: (el.voteStatus != -1 ? -1 : 0)} : el))}
                ));
        }
    }
           
    componentDidMount() {
        // Use URL params to set up sorting and offset values, otherwise use defaults
        const params = new URLSearchParams(window.location.search);
        this.setState((state) => ({
            sortKey: params.get('sortBy') ? params.get('sortBy') : state.sortOptions[0].value, 
            first: params.get('offset') ? params.get('offset') : 0,
            searchQueries: params.get('query') ? JSON.parse(params.get('query')) : []
        }), this.fetchDrinks);
    }

    // When user selects a new sorting option
    onSortChange = (event) => {

        // Update the URL params
        const params = new URLSearchParams(window.location.search);
        params.set('sortBy',event.value);
        window.history.replaceState({},'', `${window.location.pathname}?${params}`);
        
        this.setState({sortKey: event.value}, this.fetchDrinks);
    }


   fetchDrinks = () => {

        fetch(process.env.REACT_APP_API_URL + '/drinks/' + this.state.sortKey 
            + (this.props.createdOnly ? '?createdOnly=true' : '')
            + (this.props.upvoted ? '?upvoted=true' : ''), {
            method: "POST",
            headers: (localStorage.getItem("currentUser") ? {'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`} : {}),
            body: "{\"offset\":" + this.state.first + ",\"queries\":" + JSON.stringify(this.state.searchQueries) + "}"
        })
                .then(response => response.ok ? response.json() : Promise.reject(response.status))
                .then(data => this.setState({drinks: data.drinks, totalRecords: data.total}))
                .catch((error) => {
                    console.error(error);
                });
    }

    fetchBrave = () => {
        fetch(process.env.REACT_APP_API_URL + "/drinks/brave", {
            method: "GET"
        })
                .then(response => response.ok ? response.json() : Promise.reject(response.status))
                .then(data => this.setState({drinks: [data], totalRecords: 1}))
                .catch((error) => {
                    console.error(error);
                });
    }

    setURLParam = (param,value) => {
        const params = new URLSearchParams(window.location.search);
        if (!value || value?.length == 0) {
            params.delete(param);
        } else {
            params.set(param,JSON.stringify(value));
        }
        window.history.replaceState({},'',`${window.location.pathname}?${params}`);
    }

    // When the user enters a new search tag
    onQueryChange = (event) => {
        this.setURLParam('query',event.value);
                
        this.setState({searchQueries: event.value}, this.fetchDrinks);
    }

    // When the user clicks on an ingredient name in the search results
    handleIngredientTagClick = (ingredient) => {
        if (this.state.searchQueries.map((i) => i.name).includes(ingredient)) {
            const newQueries = this.state.searchQueries.filter((i) => i.name !== ingredient);
            this.setURLParam('query',newQueries);
            // Remove ingredient from query list
            this.setState((state) => ({searchQueries: newQueries}), this.fetchDrinks);
        } else {
            // Add ingredient to query list
            this.setURLParam('query',this.state.searchQueries.concat({name: ingredient, type: "ingredient"}));
            this.setState((state) => ({searchQueries: [...state.searchQueries, {name: ingredient, type: "ingredient"}]}), this.fetchDrinks);

        }
    }
    onPage = (event) => {

        // Update the URL params
        const params = new URLSearchParams(window.location.search);
        params.set('offset',event.first);
        window.history.replaceState({},'',`${window.location.pathname}?${params}`);
        this.setState((state) => ({
                first: event.first,
            }),
                this.fetchDrinks
                )
    }
    render() {
        const header = (
                <div className="p-grid p-nogutter">
                    {/* <Dashboard/> */}
                    <div className="p-col-10 p-d-sm-flex" style={{textAlign: "left"}}>
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
                        <div className="brave-button">
                            <Button
                                label="I'm brave"
                                tooltip="What does fate have in store for you?"
                                onClick={() => this.fetchBrave()}
                                />
                        </div>
                    </div>
                    <div className="p-col-2" style={{textAlign: "right"}}>
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
                    totalRecords={this.state.totalRecords}
                    rows={20} //rows = nr. elements according to the ABSOLUTE BUFOONS @primefaces
				></DataView>
			</div>
		);
	}
}