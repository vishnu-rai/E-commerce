import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Show, SimpleShowLayout, ArrayField,
  Create,
  ArrayInput, SimpleFormIterator,
  CardActions, ListButton, RefreshButton, TopToolbar,
} from 'react-admin'

import CategoryItemCreateButton from './CategoryItemCreateButton'
import CategoryItemDeleteButton from './CategoryItemDeleteButton'

export const CategoryList = props => {
    return (
    <List {...props}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <ImageField source="image" />
            <TextField source="name" />
            <TextField source="status" />
            <EditButton />
        </Datagrid>
    </List>
)};

export const CategoryShow = props => {

    return <Show {...props} actions={<CategoryShowActions />}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <ImageField source="image" />
            <TextField source="status" />
            <ArrayField source="Item">
                <Datagrid>
                    <TextField source="id" />
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
            <TextInput disabled source="id" />
            <TextInput source="name" />
            <TextInput source="status" />
            <ImageInput label="New Image" source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
        </SimpleForm>
        </Edit>
);

const defaultValues = {status: "1"}
export const CategoryCreate = props => (
    <Create {...props}>
        <SimpleForm initialValues={defaultValues}>
            <TextInput source="name" />
            <TextInput source="status" />
            <ImageInput label="New Image" source="image" accept="image/*">
                <ImageField source="src" />
            </ImageInput>
        </SimpleForm>
    </Create>
);

const CategoryShowActions = ({basePath, data, resource}) => {
    const categoryId = data ? data.id : null
    return(
        <TopToolbar>
          <CategoryItemCreateButton customProps={{categoryId, resource, data}} />
          <EditButton basePath={basePath} record={data} />
        </TopToolbar>
    )
};