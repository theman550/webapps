import React from 'react';
import { Text, View, StyleSheet, TextInput, Alert } from 'react-native';
import { Formik, Field, Form, ErrorMessage, yupToFormErrors, FieldArray, insert } from 'formik';
import * as Yup from 'yup';
import { Button } from 'primereact/button';
import { InputTextarea } from 'primereact/inputtextarea';
import { FileUpload } from 'primereact/fileupload';
import { Dropdown } from 'primereact/dropdown';
import { render } from '@testing-library/react';

const ingredientItem = {
      name: '',
      percentage: '',
      amount: '',
      unit: '',
};

const initialValues = {
      drinkName: '',
      description: '',
    ingredients: [
      {
        name: '',
        percentage: '',
        amount: '',
        unit: '',
      }
    ],
  };
  
  const validationSchema=Yup.object({
      drinkName: Yup.string()
      .required('A name is required'),
      description: Yup.string()
      .required('Please describe how to make your drink'),
      ingredients: Yup.array().of(
        Yup.object().shape({
          name: Yup.string()
          .required('A name is required'),
          percentage: Yup.number()
          .required('Required')
          .min(0, "Must be at least 0%")
          .max(100, "Must be less than 100%"),
          amount: Yup.number()
          .required('Required'),
          unit: Yup.string()
          .required('Required'),
        })
      )
    })

export default class AddDrink extends React.Component {
  render(){
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
                    headers: { 'Content-Type': 'application/json' },
                    body: {values}
                };
            fetch(process.env.REACT_APP_API_URL+"/drinks/", requestOptions)
            .then(response => response.json())
            .then(data => alert("drink created!"));
            formikActions.setSubmitting(false);
            }, 500);
            }}
        />
    )
  }
}
const AddDrinkComponent = ({
  handleChange,
  handleBlur,
  values,
  }) => (
  <Form>
    <center>
    <View style={styles.container}>
        <TextInput
            type="text"
            onChange={handleChange('drinkName')}
            onBlur={handleBlur('drinkName')}
            value={values.drinkName}
            name="drinkName"
            autoFocus
            placeholder="Name of the drink"
            style={styles.input}
        />
        <ErrorMessage name={`drinkName`} />
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
        <ErrorMessage name={`description`} />
        <FieldArray name="ingredients">
          {({ remove, push }) => (
            <div>
              {values.ingredients.length > 0 &&
                values.ingredients.map((ingredient, index) => (
                <div className="row" key={index}>
                  <div className="col">
                    <label htmlFor={`ingredients.${index}.name`}>Name:&ensp;</label>
                    <Field name={`ingredients[${index}].name`}  />
                    <ErrorMessage name={`ingredients.${index}.name`} />
                  <label htmlFor={`ingredients.${index}.percentage`}>&emsp;Percentage:&ensp;</label>
                    <Field name={`ingredients.${index}.percentage`} />                    
                    <ErrorMessage name={`ingredients.${index}.percentage`} />
                  <label htmlFor={`ingredients.${index}.amount`}>&emsp;Amount:&ensp;</label>
                    <Field name={`ingredients.${index}.amount`} />
                    <ErrorMessage name={`ingredients.${index}.amount`} />
                  <label htmlFor={`ingredients.${index}.unit`}>&emsp;Unit:&ensp;</label>
                    <Field name={`ingredients.${index}.unit`} />
                    <ErrorMessage name={`ingredients.${index}.unit`} />
                    &emsp;
                    <Button type="Button"
                    className="secondary" 
                    onClick={() => remove(index)}>
                       Remove ingredient
                    </Button>
                  </div>
                </div>
                ))}
                <br/>
               <Button
                  type="Button"
                  className="secondary"
                  onClick={() => push({
                    name: '',
                    percentage: '',
                    amount: '',
                    unit: '',
                    })}
                >
                Add another ingredient
                </Button>
            </div>
            )}
            </FieldArray>
            <br/>
            <Button type="submit">Submit drink</Button>
      </View>
      </center>
    </Form>
);

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