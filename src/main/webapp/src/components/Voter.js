import { Button } from 'primereact/button';
import './Voter.css';

export default function Voter(props) {
    return (
      <div className="votes">
        <Button
          onClick={() => {props.sendVote(props.data.id, true)}}
          icon="pi pi-angle-up"
          className="p-button-text p-button-plain"
        ></Button>
        <span className="voteCount">{props.data.voteCount}</span>
        <Button
          onClick={() => {props.sendVote(props.data.id, false)}}
          icon="pi pi-angle-down"
          className="p-button-text p-button-plain"
        ></Button>
      </div>
    );
}
