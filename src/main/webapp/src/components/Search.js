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

    autoComplete = (e) => {
        // TODO: Send a GET request to api (e.g /search?query=searchQuery)
        // The api should respond with a list of suggestions of drinks and/or ingredients (names only)
        setTimeout(() => {
            this.setState({searchSuggestions: ['a suggestion','vodka']});
        }, 250);
    }

    render() {
        return (
            <div className="auto-complete-search">
                <span className="p-fluid">
                    <AutoComplete
                        multiple
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
