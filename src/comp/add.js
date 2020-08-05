import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Create
} from 'react-admin'

export const AddList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <ImageField source="image" />
            <TextField source="name" />
            <EditButton />
        </Datagrid>
    </List>
);

export const AddEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput disabled source="id" />
            <TextInput source="name" />
            <ImageInput source="image" accept="image/*">
            	<ImageField source="src" />
            </ImageInput>
        </SimpleForm>
    </Edit>
);

export const AddCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <TextInput source="name" />
            <ImageInput source="image" accept="image/*">
            	<ImageField source="src" />
            </ImageInput>
        </SimpleForm>
    </Create>
);

