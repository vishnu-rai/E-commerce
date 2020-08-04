import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput,
  Create, Filter
} from 'react-admin'

const ProductFilter=(props)=>(
  <Filter {...props}>
    <ReferenceInput source="Shop_id" reference="Shop">
      <SelectInput optionText="name" optionValue="shop" />
    </ReferenceInput>
  </Filter>
);

export const ProductList = props => (
<List {...props} filters={<ProductFilter />}>
  <Datagrid rowClick="edit">
    <TextField source="id" />
    <ImageField source="Image" title="random"/> 
    <TextField source="Price" />
    <TextField source="Description" />
    <ReferenceField source="Shop_id" reference="Shop"><TextField source="name" /></ReferenceField>
    <TextField source="Item" />
    <TextField source="Category" />
    <TextField source="Brand" />
    <EditButton />
  </Datagrid>
</List>
);

export const ProductEdit = props => (
  <Edit {...props}>
    <SimpleForm>
      <TextInput disabled source="id" />
      <TextInput source="Image" />
      <TextInput source="Category" />
      <TextInput source="Price" />
      <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="shop" /></ReferenceInput>
      <TextInput source="Item" />
      <TextInput source="Description" />
      <TextInput source="Brand" />
    </SimpleForm>
  </Edit>
);

export const ProductCreate = props => (
  <Create {...props}>
    <SimpleForm>
      <TextInput source="Image" />
      <TextInput source="Category" />
      <TextInput source="Price" />
      <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="shop" /></ReferenceInput>
      <TextInput source="Item" />
      <TextInput source="Description" />
      <TextInput source="Brand" />
    </SimpleForm>
  </Create>
);