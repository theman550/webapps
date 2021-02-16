import { Chip } from 'primereact/chip';
import { Avatar } from 'primereact/avatar';

import Voter from './Voter.js';
import './DrinkListItem.css';
export default function DrinkListItem(props) {
    const data = props.data;
    const tags = data.ingredients.map((ingredient) => 
      <span key={ingredient} onClick={() => props.handleIngredientTagClick(ingredient)}>
        <Chip 
          className={`ingredient-tag pmr-2 p-mb-2 ${props.queries.includes(ingredient) ? "selected-ingredient" : ""}`} 
          label={ingredient}>
        </Chip>
      </span>
    );
    return (
      <div className="p-col-12">
        <div className="product-list-item">
        <Voter data={data} sendVote={props.sendVote}></Voter>
          <img
            src={data.image}
            alt={data.name}
          />
          <div className="product-list-detail">
            <i className="pi pi-tag product-category-icon"></i>
            {tags}
            <div className="product-name">{data.name}</div>
            <span className="product-description">{data.description}</span>
          </div>
          <div className="product-list-end">
            <Avatar icon="pi pi-user" shape="circle"></Avatar>
          </div>
        </div>
      </div>
    );
}
