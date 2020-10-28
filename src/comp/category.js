import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Show, SimpleShowLayout, ArrayField,
  Create,
  ArrayInput, SimpleFormIterator,
} from 'react-admin'

import CategoryItemCreateButton from './CategoryItemCreateButton'
import CategoryItemDeleteButton from './CategoryItemDeleteButton'

export const CategoryList = props => {
    return (
    <List {...props}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="name" />
            <EditButton />
        </Datagrid>
    </List>
)};

export const CategoryShow = props => {

    return <Show {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <CategoryItemCreateButton />
            <ArrayField source="Item">
                <Datagrid>
                    <TextField source="id" />
                    <TextField source="image" />
                    <TextField source="type" />
                    <CategoryItemDeleteButton parentId={props.id}/>
                </Datagrid>
            </ArrayField>
        </SimpleShowLayout>
    </Show>
};

export const CategoryEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="id" />
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