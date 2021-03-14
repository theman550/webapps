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
                    .then(response => {
              console.log(response);          
            })
        } else{
            console.log("Oi! reg or login!");
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
                    .then(response => {
              console.log(response);          
            })
        } else{
            console.log("Oi! reg or logissn!");   
        }
    }
    
    return (
      <div className="votes">
        <Button
          onClick={() => {upvote(props.data)}}
          icon="pi pi-angle-up"
          className="p-button-text p-button-plain"
        ></Button>
        <span className="voteCount">{props.data.voteCount}</span>
        <Button
          onClick={() => {downvote(props.data)}}
          icon="pi pi-angle-down"
          className="p-button-text p-button-plain"
        ></Button>
      </div>
    );
}
