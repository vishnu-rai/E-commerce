import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Create,
} from 'react-admin'

export const ServiceList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <ImageField source="icon" title="plcholder" />
            <TextField source="name" />
            <EditButton />
        </Datagrid>
    </List>
);