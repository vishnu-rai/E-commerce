import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField, DateField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput, DateInput,
  Create,
} from 'react-admin'

import {parse} from 'query-string'
export const OrderList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <TextField source="Payment_type" />
            <TextField source="Address_id" />
            <ReferenceField source="Product_id" reference="Products"><TextField source="id" /></ReferenceField>
            <DateField source="Date" />
            <ReferenceField source="Shop_id" reference="Shop"><TextField source="id" /></ReferenceField>
            <TextField source="Price" />
            <TextField source="Status" />
            <TextField source="Type" />
        </Datagrid>
    </List>
);

export const OrderEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="id" disabled />
            <DateInput source="Price" />
            <TextInput source="Status" />
            <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Type" />
            <TextInput source="Payment_type" />
            <ReferenceInput source="Product_id" reference="Products"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Address_id"></TextInput>
            <TextInput source="Date" />
        </SimpleForm>
    </Edit>
);

export const OrderCreate = props => {
	let {Shop_id}=parse(props.location.search)
  if(!Shop_id){Shop_id=""}
  const redirect= Shop_id?`/Shop/${Shop_id}/show/Products`:"/Products"
	return(
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <DateInput source="Price" />
            <TextInput source="Status" />
            <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Type" />
            <TextInput source="Payment_type" />
            <ReferenceInput source="Product_id" reference="Products"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Address_id"></TextInput>
            <DateInput source="Date" />
        </SimpleForm>
    </Create>
	)
};