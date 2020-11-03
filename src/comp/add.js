import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField, SelectField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput, required,
  Create, DeleteButton,
} from 'react-admin'

export const AddList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <TextField source="status" />
            <ImageField source="image" />
            <ReferenceField label="Shop" source="shop_id" reference="Shop">
                <TextField source="name" />
            </ReferenceField>
            <DeleteButton />
        </Datagrid>
    </List>
);

export const AddEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <SelectInput source="status" choices={[
                {id: "Active", name: "Active"},
                {id: "Inactive", name: "Inactive"},
                {id: "Rejected", name: "Rejected"},
            ]} />
            <TextField source="id" />
            <ImageField source="image" />
            <ReferenceField label="Shop" source="shop_id" reference="Shop">
                <TextField source="name"/>
            </ReferenceField>
        </SimpleForm>
    </Edit>
);

export const AddCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <ImageInput source="image" accept="image/*">
            	<ImageField source="src" />
            </ImageInput>
            <SelectInput validate={[required()]} source="status" choices={[
                {id: "Active", name: "Active"},
                {id: "Inactive", name: "Inactive"},
            ]} />
            <ReferenceInput label="Shop" source="shop_id" reference="Shop">
                <SelectInput optionText="name" optionValue="shop_id" />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

