import React, { useRef } from 'react';
import { View, StyleSheet, TextInput } from 'react-native';
import { Formik, Field, Form, ErrorMessage, FieldArray } from 'formik';
import * as Yup from 'yup';
import { Button } from 'primereact/button';
import { Messages } from 'primereact/messages';
import 'primeflex/primeflex.css';


const ingredientItem = {
      name: '',
      abv: '',
      amount: '',
      unit: '',
};

const initialValues = {
      name: '',
      description: '',
      image: '',
    ingredients: [
      {
      name: '',
      abv: '',
      amount: '',
      unit: '',
      }
    ]
  };
  
const validationSchema=Yup.object({
    name: Yup.string()
    .required('A name is required'),
    description: Yup.string()
    .required('Please describe how to make your drink'),
    image: Yup.string().url(),
    ingredients: Yup.array().of(
      Yup.object().shape({
        name: Yup.string()
        .required('A name is required'),
        abv: Yup.number('Must be numeric')
        .typeError('Must be numeric')
        .required('Required')
        .min(0, "Must be at least 0%")
        .max(100, "Must be less than 100%"),
        amount: Yup.number()
        .required('Required')
        .typeError('Must be numeric'),
        unit: Yup.string()
        .required('Required'),
      })
    )
})

function AddDrink () {
  const msg = useRef(null);

  function showCreatedMessage() {
    msg.current.show({severity: 'success', summary: '', detail: 'Drink created!'})
  }
  function showErrorMessage() {
    msg.current.show({severity: 'failure', summary: '', detail: 'Something went wrong.'})
  }

  const AddDrinkComponent = ({
    handleChange,
    handleBlur,
    values,
    }) => (
    <Form>
      <center>
      <div>
      <h1>Add a drink</h1>
      <View style={styles.container}>
      <Messages ref={msg}></Messages>
      <br/>
        <TextInput
            type="text"
            onChange={handleChange('name')}
            onBlur={handleBlur('name')}
            value={values.name}
            name="name"
            autoFocus
            placeholder="Name of the drink"
            style={styles.input}
        />
        <small><ErrorMessage name={`name`}>
          {mess => <div style={{ color: 'red', textAlign: 'left' }}>{mess}</div> }
        </ErrorMessage></small>
        <br/>
        <TextInput
            type="text"
            multiline={true}
            numberOfLines={4}
            onChange={handleChange('description')}
            onBlur={handleBlur('description')}
            value={values.description}
            name="description"
            placeholder="Describe how to make your drink"
            style={styles.descriptionInput}
        />
        <small>
        <ErrorMessage name={`description`}>
          {mess => <div style={{ color: 'red', textAlign: 'left' }}>{mess}</div> }
        </ErrorMessage>
        </small>
        <br/>
        <TextInput
            type="text"
            onChange={handleChange('image')}
            onBlur={handleBlur('image')}
            value={values.image}
            name="image"
            autoFocus
            placeholder="URL to a picture"
            style={styles.input}
        />
        <FieldArray name="ingredients">
          {({ remove, push }) => (
            <div>
              <h2>Ingredients</h2>
              <div className="p-grid">
              {values.ingredients.length > 0 &&
                values.ingredients.map((ingredient, index) => (
                  <div className="p-row p-a-center" style={{display: 'flex', textAlign: "center"}} key={index}>

                    <div className="p-col">
                      <label htmlFor={`ingredients.${index}.name`}>Name:&ensp;</label>
                      <Field name={`ingredients[${index}].name`} style={{width: '60%'}} />
                      <small> 
                      <div style={{ color: 'red' }}>
                      <ErrorMessage name={`ingredients.${index}.name`}>
                        {mess => <>{mess}</> }
                      </ErrorMessage>
                      </div>
                      </small>
                    </div>
                    
                    <div className="p-col">
                    <label htmlFor={`ingredients.${index}.abv`}>&emsp;Alcohol:&ensp;</label>
                      <Field name={`ingredients.${index}.abv`} style={{width: '20px'}}/> %   
                      <small>            
                      <ErrorMessage name={`ingredients.${index}.abv`}>
                        {mess => <div style={{ color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                      </small>
                    </div>
                    
                    <div className="p-col">
                    <label htmlFor={`ingredients.${index}.amount`}>&emsp;Amount:&ensp;</label>
                      <Field name={`ingredients.${index}.amount`}  style={{width: '40px'}} />
                      <small> 
                      <ErrorMessage name={`ingredients.${index}.amount`}>
                        {mess => <div style={{ color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                      </small>
                    </div>
                    
                    <div className="p-col">
                    <label htmlFor={`ingredients.${index}.unit`}>&emsp;Unit:&ensp;</label>
                      <Field as="select" name={`ingredients.${index}.unit`}>
                        <option value="">Select</option>
                        <option value="MILLILITRE">Ml</option>
                        <option value="CENTILITRE">Cl</option>
                        <option value="DECILITRE">Dl</option>
                        <option value="LITRE">L</option>
                        <option value="GRAMS">Grams</option>
                        <option value="PIECES">Pieces</option>
                      </Field>
                      <small> 
                      <ErrorMessage name={`ingredients.${index}.unit`}>
                        {mess => <div style={{ color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                      </small>
                    </div>

                      &emsp;
                      <Button type="Button"
                      className="secondary"
                      style={{height: '25px'}} 
                      onClick={() => remove(index)}>
                        Remove
                      </Button>
                </div>
              ))}
              </div>
                <br/>
                <Button
                type="Button"
                className="secondary"
                onClick={() => push(ingredientItem)}
                >
                Add another ingredient
                </Button>
            </div>
            )}
            </FieldArray>
            <br/>
            <right>
              <Button type="submit" style={{width: '130px'}}>Submit drink</Button>
            </right>
      </View>
      </div>
      </center>
    </Form>
  );
  if(localStorage.getItem("currentUser") != null){
    return(
      <Formik 
      component={AddDrinkComponent} 
      initialValues={initialValues} 
      validationSchema={validationSchema}
      validateOnChange={false}
      validateOnBlur={false}
      onSubmit = {(values, formikActions) => {
        setTimeout(() => {      
            console.log("Transmitting drink data to database...");
            console.log(JSON.stringify(values));
            const requestOptions = {
                method: 'POST',
                credentials: 'include',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${JSON.parse(localStorage.getItem("currentUser")).token}`},
                body: JSON.stringify(values)
            };
            console.log(requestOptions);
            fetch(process.env.REACT_APP_API_URL+"/drinks/", requestOptions)
            .then(response => {
              if(response.ok){
                showCreatedMessage();
                formikActions.resetForm();
              }
              else{
                showErrorMessage()
              }
            })
            formikActions.setSubmitting(false)
          }, 500);
          }}
      />
    )
  }
  else{
    return(
      <div>
        You must log in to create a drink.
      </div>
    )
  }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      backgroundColor: '#F',
      width: '70%',
      padding: 8,
      borderStyle: 'line',
      borderWidth: '1px',
      borderColor: '#0',
    },
    title: {
      margin: 24,
      fontSize: 24,
      fontWeight: 'bold',
      textAlign: 'center',
    },
    error: {
      margin: 8,
      fontSize: 14,
      color: 'red',
      fontWeight: 'bold',
      textAlign: 'left',
    },
    input: {
      height: 50,
      paddingHorizontal: 8,
      width: '40%',
      borderColor: '#ddd',
      borderWidth: 1,
      backgroundColor: '#fff',
    },
    descriptionInput: {
      height: 200,
      paddingHorizontal: 8,
      width: '80%',
      borderColor: '#ddd',
      borderWidth: 1,
      backgroundColor: '#fff',
    },
  });
  export default AddDrink