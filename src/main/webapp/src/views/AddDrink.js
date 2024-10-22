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
    image: Yup.string().url('Invalid URL.'),
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
          <ErrorMessage name={`name`}>
            {mess => <div style={{fontSize: 'small', color: 'red', textAlign: 'left' }}>{mess}</div> }
          </ErrorMessage>
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
          <ErrorMessage name={`description`}>
            {mess => <div style={{fontSize: 'small', color: 'red', textAlign: 'left' }}>{mess}</div> }
          </ErrorMessage>
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
        <ErrorMessage name={`image`}>
          {mess => <div style={{fontSize: 'small', color: 'red', textAlign: 'left' }}>{mess}</div> }
        </ErrorMessage>
        <FieldArray name="ingredients">
          {({ remove, push }) => (
            <div>
              <h2>Ingredients</h2>
              {values.ingredients.length > 0 &&
                values.ingredients.map((ingredient, index) => (
                  <div className="p-grid p-justify-between" style={{width: '60%', height:'70px', borderTop:'1px dotted black', paddingBottom: '3px', paddingTop: '10px'}} key={index}>
                    <div className="p-col-flex  p-as-start">
                      <Field name={`ingredients[${index}].name`} placeholder="Name" />
                      <ErrorMessage name={`ingredients.${index}.name`}>
                        {mess => <div style={{fontSize: 'small', color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                    </div>                    
                    <div className="p-col-flex">
                      <Field name={`ingredients.${index}.abv`}  style={{width: '80px'}} placeholder="Alcohol" />%   
                      <ErrorMessage name={`ingredients.${index}.abv`}>
                        {mess => <div style={{fontSize: 'small', color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                    </div>
                    <div className="p-col-flex">
                      <Field name={`ingredients.${index}.amount`} style={{width: '80px'}} placeholder="Amount"  />
                      <ErrorMessage name={`ingredients.${index}.amount`}>
                        {mess => <div style={{fontSize: 'small', color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                    </div>
                    <div className="p-col-flex">
                      <Field as="select" name={`ingredients.${index}.unit`}>
                        <option value="">Unit</option>
                        <option value="MILLILITRE">Ml</option>
                        <option value="CENTILITRE">Cl</option>
                        <option value="DECILITRE">Dl</option>
                        <option value="LITRE">L</option>
                        <option value="GRAMS">Grams</option>
                        <option value="PIECES">Pieces</option>
                      </Field>
                      <ErrorMessage name={`ingredients.${index}.unit`}>
                        {mess => <div style={{fontSize: 'small', color: 'red' }}>{mess}</div> }
                      </ErrorMessage>
                    </div>

                    <div className="p-col-flex p-as-end">
                      &emsp;
                      <Button type="Button"
                      icon="pi pi-trash"
                      className="p-button-sm"
                      style={{height: '20px'}} 
                      onClick={() => remove(index)}/>
                    <br/>
                    <br/>
                    <br/>
                    </div>
                    <br/>
                </div>
              ))}
                
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
            <div style={{paddingHorizontal: 'auto'}}>
              <Button type="submit" style={{width: '130px'}}>Submit drink</Button>
            </div>
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
      backgroundColor: '#f8f9fa',
      width: '70%',
      padding: 8,
      borderStyle: 'line',
      borderWidth: '1px',
      borderColor: '#0',
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