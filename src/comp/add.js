import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField, SelectField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Create
} from 'react-admin'

export const AddList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <ImageField source="image" />
            <ReferenceField label="Shop" source="shop_id" reference="Shop">
                <TextField source="name" />
            </ReferenceField>
            <TextField source="shop_id" />
            <TextField source="status" />
            <EditButton />
        </Datagrid>
    </List>
);

export const AddEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput disabled source="id" />
            <ImageInput source="image" accept="image/*">
            	<ImageField source="src" />
            </ImageInput>
            <TextInput source="shop_id" />
            <SelectInput source="status" choices={[
                {id: "Accepted", name: "Accepted"},
                {id: "Pending", name: "Pending"},
                {id: "Rejected", name: "Rejected"},
            ]} />
        </SimpleForm>
    </Edit>
);

export const AddCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <ImageInput source="image" accept="image/*">
            	<ImageField source="src" />
            </ImageInput>
            <SelectInput source="status" choices={[
                {id: "Accepted", name: "Accepted"},
                {id: "Pending", name: "Pending"},
                {id: "Rejected", name: "Rejected"},
            ]} />
        </SimpleForm>
    </Create>
);

