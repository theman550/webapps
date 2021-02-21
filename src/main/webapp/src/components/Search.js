import React, { Component } from 'react'
import { AutoComplete } from 'primereact/autocomplete';

import './Search.css';

export default class Search extends Component {

    constructor(props) {
        super(props);
        this.state = {
            searchSuggestions: []
        }
    }

    /* 
    Fetches both drink and ingredient names that start with the
    current search query and updates the suggestion list.
    The api should return a JSON list of "suggestion objects":
    Example:
    [{"name":"a drink suggestion","type":"drink"},
     {"name":"an ingredient suggestion","type":"ingredient"}]
    */
    autoComplete = (e) => {
        fetch(process.env.REACT_APP_API_URL+"/autocomplete?s="+e.query)
            .then(response => response.json())
            .then(suggestions => {
                this.setState({searchSuggestions: suggestions});
            })
            .catch((error) => {
                console.error(error);
            });
    }

    render() {
        return (
            <div className="auto-complete-search">
                <span className="p-fluid">
                    <AutoComplete
                        multiple
                        field="name"
                        forceSelection={false}
                        placeholder="Search"
                        autoFocus={true}
                        autoHighlight={true}
                        value={this.props.searchQueries} 
                        suggestions={this.state.searchSuggestions}
                        completeMethod={(e) => this.autoComplete(e)}
                        onChange={(e) => this.props.onQueryChange(e)}
                    ></AutoComplete>
                </span>
            </div>
        )
    }
}
