import React from "react";
import DrinkList from "../components/DrinkList.js";

export default class Home extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
                <div>
                    <h1>Home</h1>
                    <DrinkList></DrinkList>
                </div>
                );
    }
}
