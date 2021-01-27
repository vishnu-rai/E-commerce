import React from 'react'
import {
  List, Datagrid, TextField, ReferenceField, ImageField, SelectField,
  EditButton, Edit, SimpleForm, TextInput, ReferenceInput, SelectInput, ImageInput, required,
  Create
} from 'react-admin'

export const CouponList = props => (
    <List {...props}>
        <Datagrid rowClick="edit">
            <TextField source="id" />
            <TextField label="Coupon Name" source="name" />
            <ReferenceField label="Shop" source="shop_id" reference="Shop">
                <TextField source="name" />
            </ReferenceField>
            <TextField source="value" />
            <EditButton />
        </Datagrid>
    </List>
);

export const CouponEdit = props => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput disabled source="id" />
            <ReferenceInput label="Shop" source="shop_id" reference="Shop">
                <SelectInput optionText="name" optionValue="shop_id" />
            </ReferenceInput>
            <TextInput label="Coupon Name" source="name" />
            <TextInput source="value" />
        </SimpleForm>
    </Edit>
);

export const CouponCreate = props => (
    <Create {...props}>
        <SimpleForm>
            <TextInput label="Coupon Name" source="name" />
            <ReferenceInput label="Shop" source="shop_id" reference="Shop">
                <SelectInput optionText="name" optionValue="shop_id" />
            </ReferenceInput>
            <TextInput source="value" />
        </SimpleForm>
    </Create>
);
