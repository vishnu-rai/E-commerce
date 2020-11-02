import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField, DateField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput, DateInput,
  Create, ChipField, Show, SimpleShowLayout, ReferenceArrayField, SingleFieldList,
} from 'react-admin'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';

import {parse} from 'query-string'
export const OrderList = props => (
    <List {...props}>
        <Datagrid rowClick="edit" expand={expandView}>
            <TextField source="id" />
            <TextField source="Address_id" />
            <DateField source="Date" />
            <TextField source="Number" />
            <TextField source="Payment_type" />
            <ReferenceField source="Shop_id" reference="Shop"><TextField source="name" /></ReferenceField>
            <TextField source="Total_price" />
            <TextField source="Status" />
            <TextField source="Type" />
            <TextField source="User_id" />
        </Datagrid>
    </List>
);

export const OrderEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="id" disabled />
            <DateInput source="Price" />
            <TextInput source="Status" />
            <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Type" />
            <TextInput source="Payment_type" />
            <ReferenceInput source="Product_id" reference="Products"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Address_id"></TextInput>
            <TextInput source="Date" />
        </SimpleForm>
    </Edit>
);

export const OrderCreate = props => {
	let {Shop_id}=parse(props.location.search)
  if(!Shop_id){Shop_id=""}
  const redirect= Shop_id?`/Shop/${Shop_id}/show/Products`:"/Products"
	return(
    <Create {...props}>
        <SimpleForm>
            <TextInput source="id" />
            <DateInput source="Price" />
            <TextInput source="Status" />
            <ReferenceInput source="Shop_id" reference="Shop"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Type" />
            <TextInput source="Payment_type" />
            <ReferenceInput source="Product_id" reference="Products"><SelectInput optionText="id" /></ReferenceInput>
            <TextInput source="Address_id"></TextInput>
            <DateInput source="Date" />
        </SimpleForm>
    </Create>
	)
};

const expandView = props=>(
    <Show {...props} component="div" title=" ">
        <SimpleShowLayout>
            <TextField source="Address_id" />
            <ReferenceArrayField label="Ordered items" source="Product_id" reference="Products">
                <ProdQuan quan={props.record.Quan} />
            </ReferenceArrayField>
        </SimpleShowLayout>
    </Show>
)

const ProdQuan = props=>{
    console.log({props})
    const {quan, data, ids} = props

    return (
          <Table size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell align="right">Quantity</TableCell>
                <TableCell align="right">Price</TableCell>
                <TableCell align="right">Id</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {ids.map((id, index) => (
                data[id] ?
                <TableRow key={id}>
                  <TableCell component="th" scope="row">
                    {data[id].Name}
                  </TableCell>
                  <TableCell align="right">{quan[index]}</TableCell>
                  <TableCell align="right">{data[id].Price}</TableCell>
                  <TableCell align="right">{id}</TableCell>
                </TableRow>
                : null
              ))}
            </TableBody>
          </Table>
      );
}