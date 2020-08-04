import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Create,
} from 'react-admin'

export const ShopList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="shop" />
            <ReferenceField source="category" reference="Category"><TextField source="name" /></ReferenceField>
            <TextField source="name" />
            <ImageField source="image" />
            <TextField source="timing" />
            <EditButton />
        </Datagrid>
    </List>
);

export const ShopEdit = props => (
    <Edit {...props}>
        <SimpleForm warnWhenUnsavedChanges>
            <TextInput label="id" source="shop" />
            <ReferenceInput source="category" reference="Category"><SelectInput optionText="name" /></ReferenceInput>
            <TextInput source="name" />
            <TextInput source="image" />
            <TextInput source="timing" />
        </SimpleForm>
    </Edit>
);

export const ShopCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <ReferenceInput source="category" reference="Category"><SelectInput optionText="name" /></ReferenceInput>
            <TextInput source="name" />
            <TextInput source="image" />
            <TextInput source="timing" />
        </SimpleForm>
    </Create>
);
