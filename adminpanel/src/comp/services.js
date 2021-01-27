import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
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

export const ServiceEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput disabled source="id" />
            <TextInput source="name" />
            <ImageInput source="icon" accept="image/*">
              <ImageField source="src" />
            </ImageInput>
        </SimpleForm>
    </Edit>
);

export const ServiceCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <TextInput source="name" />
            <ImageInput source="icon" accept="image/*">
              <ImageField source="src" />
            </ImageInput>
        </SimpleForm>
    </Create>
);