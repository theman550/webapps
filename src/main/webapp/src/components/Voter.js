import { Button } from 'primereact/button';
import './Voter.css';

export default function Voter(props) { 
              
    const upvote = (e) => {
        
        if(localStorage.getItem("currentUser") != null){
            const requestOptions = {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`},
                body: JSON.stringify(e)
            };

            fetch(process.env.REACT_APP_API_URL+"/votes/upvote/", requestOptions)
              .then(response => response.json())
              .then(data => props.sendVote(props.data.id, true, data))                 
        } else{
            console.log("Login to vote!");
        }
    }
    
    const downvote = (e) => {
        
        if(localStorage.getItem("currentUser") != null){
            const requestOptions = {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`},
                body: JSON.stringify(e)
            };

            fetch(process.env.REACT_APP_API_URL+"/votes/downvote/", requestOptions)
              .then(response => response.json())
              .then(data => props.sendVote(props.data.id, false, data))                  
        } else{
            console.log("Login to vote!");   
        }
    }
    
    return (
      <div className="votes">
        <Button
          onClick={() => {upvote(props.data)}}
          icon="pi pi-angle-up"
          className={`vote-button p-button-text p-button-plain ${props.data.voteStatus == 1 ? 'is-active-vote' : ''}`}
        ></Button>
        <span className="voteCount">{props.data.voteCount}</span>
        <Button
          onClick={() => {downvote(props.data)}}
          icon="pi pi-angle-down"
          className={`vote-button p-button-text p-button-plain ${props.data.voteStatus == -1 ? 'is-active-vote' : ''}`}
        ></Button>
      </div>
    );
}
