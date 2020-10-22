import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Create,
} from 'react-admin'

export const ShopList = props => (
    <List {...props}>
        <Datagrid rowClick="show">
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
            <TextInput source="email" />
            <ImageInput source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
            <TextInput source="timing" />
        </SimpleForm>
    </Edit>
);

export const ShopCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput label="id" source="shop" />
            <ReferenceInput source="category" reference="Category"><SelectInput optionText="name" /></ReferenceInput>
            <TextInput source="name" />
            <TextInput source="email" />
            <ImageInput source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
            <TextInput source="timing" />
        </SimpleForm>
    </Create>
);
