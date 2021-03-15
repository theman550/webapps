import React from "react";
import {TabPanel, TabView} from "primereact/tabview";
import { Avatar } from "primereact/avatar";

import DrinkList from "../components/DrinkList.js";

import "./Profile.css";

export default class Profile extends React.Component {


	constructor(props) {
		super(props);
		this.state = {
			activeTab: 0,
			tabHashes: ["info","mydrinks","upvoted"],
			user: null
		}
	}
	
	// Changes the active tab and updates the hash in url
	changeTab = (e) => {
		this.setState({activeTab: e.index});
		window.location.hash = this.state.tabHashes[e.index];
	}
	
	componentDidMount = () => {
		// Change the active tab if a hash is included in url
		if (window.location.hash) {
			this.setState({activeTab: this.state.tabHashes.indexOf(window.location.hash.substring(1))});
		}
		this.getUser();
	}

	getUser = () =>  {
		fetch(process.env.REACT_APP_API_URL + "/user/me", {
			method: "GET",
			headers: (localStorage.getItem("currentUser") ? {'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`} : {})
		})
				.then(response => response.ok ? response.json() : Promise.reject(response.status))
				.then(data => this.setState({user: data}))
				.catch((error) => {
					console.error(error);
				});
	}

	render() {

		return (
			<div className="profile-page">
				<div className="p-d-flex p-m-2 p-ai-center">
					<Avatar className="p-mr-2" icon="pi pi-user" size="xlarge" shape="circle" />
					<h1>{this.props.user}</h1>
				</div>
				<TabView activeIndex={this.state.activeTab} onTabChange={(e) => this.changeTab(e)}>
					<TabPanel onClick={() => window.location.hash="info"} header="Info">
						<div className="user-details">
							<div className="p-d-flex p-ai-baseline">
								<h3><i className="pi pi-calendar"></i> Member since</h3>
								<p className="user-details p-ml-2">{this.state.user?.createdAt}</p>
							</div>
						</div>
					</TabPanel>
					<TabPanel onClick={() => window.location.hash="mydrinks"} header="My Drinks">
						<DrinkList createdOnly></DrinkList>
					</TabPanel>
					<TabPanel onClick={() => window.location.hash="upvoted"} header="Upvoted">
						<DrinkList upvoted></DrinkList>
					</TabPanel>
				</TabView>
			</div>
		)
	}
}
