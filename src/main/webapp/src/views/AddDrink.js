import React, { useRef } from 'react';
import { Text, View, StyleSheet, TextInput, Alert } from 'react-native';
import { Formik, Field, Form, ErrorMessage, yupToFormErrors, FieldArray } from 'formik';
import * as Yup from 'yup';
import { Button } from 'primereact/button';
import { FileUpload } from 'primereact/fileupload';
import { Messages } from 'primereact/messages';


const ingredientItem = {
      name: '',
      percentage: '',
      amount: '',
      unit: '',
};

const initialValues = {
      name: '',
      description: '',
    ingredients: [
      {ingredientItem}
    ],
  };
  
const validationSchema=Yup.object({
    name: Yup.string()
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

function AddDrink () {
  const msg = useRef(null);

  function showMessage() {
    msg.current.show({severity: 'success', summary: '', detail: 'Drink created!'})
  }

  const AddDrinkComponent = ({
    handleChange,
    handleBlur,
    values,
    }) => (
    <Form>
      <center>
      <View style={styles.container}>
      <Messages ref={msg}></Messages>
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
        <ErrorMessage name={`name`} />
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
                    <Field as="select" name={`ingredients.${index}.unit`}>
                      <option value="">Select</option>
                      <option value="MILLILITRE">Ml</option>
                      <option value="CENTILITRE">Cl</option>
                      <option value="DECILITRE">Dl</option>
                      <option value="LITRE">L</option>
                      <option value="GRAMS">Grams</option>
                      <option value="PIECES">Pieces</option>
                    <ErrorMessage name={`ingredients.${index}.unit`} />
                  </Field>

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
                  onClick={() => push(ingredientItem)}
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
                  body: JSON.stringify(values)
              };
          fetch(process.env.REACT_APP_API_URL+"/drinks/", requestOptions)
          .then(showMessage())
          .then(formikActions.resetForm);
          formikActions.setSubmitting(false);
          }, 500);
          }}
      />
)
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