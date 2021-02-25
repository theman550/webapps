import React from 'react';
import { Text, View, StyleSheet, TextInput, Alert } from 'react-native';
import { Formik, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import { Button } from 'primereact/button';
import { InputTextarea } from 'primereact/inputtextarea';
import { FileUpload } from 'primereact/fileupload';
import { Dropdown } from 'primereact/dropdown';
 
export default class AddDrink extends React.Component {
  descriptionInput = null;
  unit = "null";

  setUnit(e){
    console.log("yo");
    this.unit = e;
  }
  
  render() {
    return (
      <center>
      <View style={styles.container}>
        <Text style={styles.title}>Add a drink </Text>
        <Text style={styles.title}></Text>
        <Formik
          initialValues={{ drinkName: '', description: '', ingredientItem: ''}}
          validationSchema={Yup.object({
            drinkName: Yup.string()              
              .required('Required'),
            description: Yup.string()
              .required('Required'),
            ingredientItem: Yup.string()
              .required('Required'),
          })}
          onSubmit={(values, formikActions) => {
            console.log("yo");
            setTimeout(() => {
              Alert.alert(JSON.stringify(values));
              console.log(JSON.stringify(values))
              const requestOptions = {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: {values}
            };
            fetch(process.env.REACT_APP_API_URL+"/drinks/", requestOptions)
                .then(response => response.json())
                .then(data => this.setState({ postId: data.id }));

              // Important: Make sure to setSubmitting to false so our loading indicator
              // goes away.
              formikActions.setSubmitting(false);
            }, 500);
          }}>
          {props => (
            <View>
             <TextInput
                onChangeText={props.handleChange('drinkName')}
                onBlur={props.handleBlur('drinkName')}
                value={props.values.drinkName}
                autoFocus
                placeholder="Name of the drink"
                style={styles.input}
                onSubmitEditing={() => {
                  // on certain forms, it is nice to move the user's focus
                  // to the next input when they press enter.
                  this.descriptionInput.focus()
                }}
              />
              {props.touched.drinkName && props.errors.drinkName ? (
                <Text style={styles.error}>{props.errors.drinkName}</Text>
              ) : null}
               <TextInput
                multiline={true}
                numberOfLines={4}
                blurOnSubmit="true"
                onChangeText={props.handleChange('description')}
                onBlur={props.handleBlur('description')}
                value={props.values.description}
                placeholder="Description"
                style={styles.descriptionInput}
                ref={el => this.descriptionInput = el}
              />  
              {props.touched.description && props.errors.description ? (
                <Text style={styles.error}>{props.errors.description}</Text>
              ) : null}
              <TextInput
                blurOnSubmit="true"
                onChangeText={props.handleChange('ingredientItem')}
                onBlur={props.handleBlur('ingredientItem')}
                value={props.values.ingredientItem}
                placeholder="Ingredients"
                style={styles.input}
                ref={el => this.ingredientItemInput = el} 
              />
              {props.touched.ingredientItem && props.errors.ingredientItem ? (
                <Text style={styles.error}>{props.errors.ingredientItem}</Text>
              ) : null}
              <Dropdown value={this.unit} optionLabel="label" optionValue="value" options={unitSelectItems} onChange={(e) => this.setUnit(e.value)} placeholder="Select unit"/>
              <Button
                onClick={props.handleSubmit}
                color="black"
                mode="contained"
                loading={props.isSubmitting}
                disabled={props.isSubmitting}
                style={{marginTop: 16}}>
                Submit
              </Button>
              <Button
                onClick={props.handleReset}
                color="black"
                mode="outlined"
                disabled={props.isSubmitting}
                style={{ marginTop: 16 }}>
                Reset
              </Button>
            </View>
          )}
        </Formik>
{/*           <FileUpload name="Upload butt" url="./upload" accept="image/*"></FileUpload> */}
      </View>
      </center>
    );
  }



/*     renderIngredientItem(){
      return(
        <div>
          <TextInput
            className="ingredientInput"
            blurOnSubmit="true"
            onChangeText={this.props.handleChange('ingredientList')}
            onBlur={this.props.handleBlur('ingredientList')}
            value={this.props.values.ingredientList}
            placeholder="ingredientList"
            style={styles.ingredientList}
            ref={el => this.ingredientItem = el} 
          />
        </div>
      ); 
    }*/
}
const unitSelectItems = [
    {label: 'CL', value: 'CL'},
    {label: 'DL', value: 'DL'},
    {label: 'Grams', value: 'g'},
];

const selectAmountItems = [
    {label: '1', value: 1},
    {label: '2', value: 2},
    {label: '3', value: 3},
];


const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F',
    width: '50%',
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
