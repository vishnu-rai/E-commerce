import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ReferenceManyField, ImageField, DateField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput,
  Create, SingleFieldList, ArrayField, ChipField,
  DeleteButton,
  Show, TabbedShowLayout, Tab,
  CardActions,ListButton,RefreshButton,
} from 'react-admin'
import { Link } from 'react-router-dom';
import ChatBubbleIcon from "@material-ui/icons/ChatBubble";
import { withStyles } from '@material-ui/core/styles';
import { Button } from 'react-admin';

const styles = {
  button: {
    margin: '1em',
    backgroundColor:'rgb(33, 150, 243)',
    color:'white'
  }
};

const AddProductButton = withStyles(styles)(({ classes, record }) => (
  <Button
    className={classes.button}
    variant="contained"
    component={Link}
    to={`/Products/create?Shop_id=${record.id}`}
    label="Add a Product"
  >
  </Button>
));

const ShopShowActions = ({ basePath, data }) => (
  <CardActions>
    <ListButton basePath={basePath} />
    <RefreshButton />
  </CardActions>
);

export const ShopShow = props => (
	<Show {...props} actions={<ShopShowActions />}>
		<TabbedShowLayout>
			<Tab label="Summary">
				<TextField label="Shop Id" source="shop_id" />
        <TextField source="name" />
				<TextField source="category" />
        <ArrayField source="Items">
          <SingleFieldList linkType={false}>
            <ChipField source="name"/>
          </SingleFieldList>
        </ArrayField>
        <TextField source="Address" />
        <TextField source="Pincode" />
        <TextField source="Phone" />
        <TextField source="min" />
				<TextField source="timing" />
				<ImageField source="image" />
			</Tab>
			<Tab label="Products" path="Products">
				<ReferenceManyField reference="Products" target="Shop_id" addLabel={false}>
					<Datagrid rowClick="edit" isRowSelectable={record=>true} optimized>
				    <TextField source="id" />
				    <ImageField source="Image" title="random"/> 
				    <TextField source="Price" />
				    <TextField source="Description" />
				    <TextField source="Item" />
				    <TextField source="Category" />
				    <TextField source="Brand" />
				    <EditButton />
				    <DeleteButton />
				  </Datagrid>
				</ReferenceManyField>
				<AddProductButton />
			</Tab>
			<Tab label="Orders" path="Orders">
				<ReferenceManyField reference="Orders" target="Shop_id" addLabel={false}>
					<Datagrid rowClick="edit" optimized>
						<TextField source="id" />
            <TextField source="Payment_type" />
            <TextField source="Address_id" />
            <ReferenceField source="Product_id" reference="Products"><TextField source="id" /></ReferenceField>
            <DateField source="Date" />
            <ReferenceField source="Shop_id" reference="Shop"><TextField source="id" /></ReferenceField>
            <TextField source="Price" />
            <TextField source="Status" />
            <TextField source="Type" />
            <EditButton />
					</Datagrid>
				</ReferenceManyField>
			</Tab>
		</TabbedShowLayout>
	</Show>
);