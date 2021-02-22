import { Button } from "primereact/button";
import { Avatar } from 'primereact/avatar';
import { Chip } from 'primereact/chip';
import Voter from './Voter.js';

import "./DrinkCard.css";

export default function DrinkCard(props) {
  const data = props.data;
  console.log("aaaaa" + JSON.stringify(data));
  
  const tags = data.ingredients.map((ingredient) =>  
        <div className="ingredient-tag">{ingredient.name}</div>
  );
        
  return (
    <div className="p-col-12 p-md-4 p-lg-3">
      <div className="product-grid-item p-shadow-3">
        <div className="product-grid-item-top">
          <div>
            <i className="pi pi-tag product-category-icon"></i>
                {tags}
          </div>
          <Avatar icon="pi pi-user" shape="circle"></Avatar>
        </div>
        <div className="product-grid-item-content">
          <Voter data={data} sendVote={props.sendVote}></Voter>
          <img
            src={data.image}
            alt={data.name}
          />
          <div>
            <div className="product-name">{data.name}</div>
            <div className="product-description">{data.description}</div>
          </div>
        </div>
        <div className="product-grid-item-bottom">
          <Button label="Details" className="p-button-text"></Button>
        </div>
      </div>
    </div>
  );
}