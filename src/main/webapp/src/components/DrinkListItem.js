import { Chip } from 'primereact/chip';
import { Avatar } from 'primereact/avatar';
import Details from '../views/Details.js';
import Voter from './Voter.js';
import './DrinkListItem.css';
export default function DrinkListItem(props) {
    const data = props.data;
    const tags = data.ingredients.map((ingredient) =>
        <span key={ingredient.id} onClick={() => props.handleIngredientTagClick(ingredient.name)}>
            <Chip 
                className={`ingredient-tag pmr-2 p-mb-2 ${props.queries.map((i) => i.name).includes(ingredient.name) ? "selected-ingredient" : ""}`} 
                label={ingredient.name}>
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
                        <Details 
                            src={data.image}
                            alt={data.name}
                            drinkName={data.name}
                            ingredients= {tags}
                            description={data.description}
                            />
                    </div>
                    <div className="product-list-end">
                        <Avatar icon="pi pi-user" shape="circle"></Avatar>
                    </div>
                </div>
            </div>
            );
}
