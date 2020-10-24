import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Show, SimpleShowLayout, ArrayField,
  Create,
  ArrayInput, SimpleFormIterator,
} from 'react-admin'

import CategoryItemCreateButton from './CategoryItemCreateButton'

export const CategoryList = props => {
    return (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <TextField source="name" />
            <EditButton />
        </Datagrid>
    </List>
)};

export const CategoryShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <CategoryItemCreateButton />
            <ArrayField source="Item">
                <Datagrid>
                    <TextField source="id" />
                    <TextField source="image" />
                    <TextField source="type" />
                </Datagrid>
            </ArrayField>
        </SimpleShowLayout>
    </Show>
);

export const CategoryEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <TextInput source="name" />
            <ArrayInput source="Item">
                <SimpleFormIterator>
                    <TextInput source="id" />
                    <TextInput source="type" />
                    <TextInput source="image" />
                </SimpleFormIterator>
            </ArrayInput>
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