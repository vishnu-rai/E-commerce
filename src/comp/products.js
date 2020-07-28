import React from 'react'
import {List, Datagrid, TextField, ReferenceField} from 'react-admin'

export const ProductList = props => (
<List {...props}>
  <Datagrid rowClick="edit">
    <TextField source="id" />
    <TextField source="Price" />
    <TextField source="Description" />
    <ReferenceField source="Shop_id" reference="Shops"><TextField source="id" /></ReferenceField>
    <TextField source="Item" />
    <TextField source="Category" />
    <TextField source="Image" />
    <TextField source="Brand" />
  </Datagrid>
</List>
);