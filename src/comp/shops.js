import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Create,
  ReferenceArrayInput, SelectArrayInput, FormDataConsumer, AutocompleteArrayInput, required,
} from 'react-admin'

import EffDropDown from './effDropDown'

export const ShopList = props => (
    <List {...props}>
        <Datagrid rowClick="show">
            <TextField source="Address" />
            <TextField source="delivery_charge" />
            <TextField source="type" />
            <MultiReferenceField source="category"/>
            <TextField source="name" />
            <ImageField source="image" />
            <TextField source="timing" />
            <EditButton />
        </Datagrid>
    </List>
);

const MultiReferenceField = ({record, ...rest})=>{
    if(record){
        return (
            <ReferenceField record={record} reference={record.category_type} {...rest}>
                <TextField source="name" />
            </ReferenceField>
        )
    }
    return <div/>
}

export const ShopEdit = props => {
    return (
    <Edit {...props}>
        <SimpleForm warnWhenUnsavedChanges>
            <TextInput source="Address" />
            <TextInput source="delivery_charge" />
            <TextInput source="type" />
            <TextInput disabled label="id" source="shop_id" />
            <SelectInput source="category_type" choices={[
                {id: 'Category', name: "Category"},
                {id: 'BCategory', name: "BCategory"}
            ]}/>
            <FormDataConsumer> 
                {({formData, ...rest})=>(
                    <ReferenceInput source="category" reference={formData.category_type || "Category"} {...rest}>
                        <SelectInput optionText="name" />
                    </ReferenceInput>
                )}
            </FormDataConsumer>
            <EffDropDown source="Items" />
            <TextInput source="name" />
            <TextInput source="email" />
            <ImageField source="image" label="Current Image" />
            <ImageInput label="New Image" source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
            <TextInput source="timing" />
        </SimpleForm>
    </Edit>
)};

export const ShopCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput label="id" source="shop" />
            <SelectInput source="category_type" choices={[
                {id: 'Category', name: "Category"},
                {id: 'BCategory', name: "BCategory"}
            ]}/>
            <FormDataConsumer> 
                {({formData, ...rest})=>(
                    <ReferenceInput source="category" reference={formData.category_type || "Category"} {...rest}>
                        <SelectInput optionText="name" />
                    </ReferenceInput>
                )}
            </FormDataConsumer>
            <TextInput source="name" />
            <TextInput source="email" />
            <ImageInput source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
            <TextInput source="timing" />
        </SimpleForm>
    </Create>
);

