import React from 'react'
import {Admin, Resource, ListGuesser, EditGuesser} from 'react-admin'
import {dataProvider, cachedDataProvider} from './dataProvider'
import {ProductList, ProductEdit, ProductCreate} from './comp/products'
import {ShopList, ShopEdit, ShopCreate} from './comp/shops'
import {ServiceList, ServiceEdit, ServiceCreate} from './comp/services'
import {CategoryList, CategoryEdit, CategoryCreate} from './comp/category'
import {AddList, AddEdit, AddCreate} from './comp/add'

const App = () => (
	<Admin dataProvider={cachedDataProvider}>
		<Resource name="Products" list={ProductList} edit={ProductEdit} create={ProductCreate} />
		<Resource name="Shop" list={ShopList} edit={ShopEdit} create={ShopCreate}/>
		<Resource name="Services" list={ServiceList} edit={ServiceEdit} create={ServiceCreate}/>
		<Resource name="Category" list={CategoryList} edit={CategoryEdit} create={CategoryCreate} />
		<Resource name="Add" list={AddList} edit={AddEdit} create={AddCreate} />
	</Admin>
);

export default App;