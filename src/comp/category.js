import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Create,
} from 'react-admin'

export const CategoryList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <TextField source="name" />
            <EditButton />
        </Datagrid>
    </List>
);

export const CategoryEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput disabled source="id" />
            <TextInput source="name" />
        </SimpleForm>
    </Edit>
);

export const CategoryCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <TextInput source="name" />
        </SimpleForm>
    </Create>
);