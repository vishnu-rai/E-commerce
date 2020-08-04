import React from 'react'
import {Admin, Resource, ListGuesser, EditGuesser} from 'react-admin'
import {dataProvider, cachedDataProvider} from './dataProvider'
import {ProductList, ProductEdit, ProductCreate} from './comp/products'
import {ShopList, ShopEdit, ShopCreate} from './comp/shops'
import {ServiceList} from './comp/services'
import {CategoryList} from './comp/category'

const App = () => (
	<Admin dataProvider={cachedDataProvider}>
		<Resource name="Products" list={ProductList} edit={ProductEdit} create={ProductCreate} />
		<Resource name="Shop" list={ShopList} edit={ShopEdit} create={ShopCreate}/>
		<Resource name="Services" list={ServiceList} />
		<Resource name="Category" list={CategoryList} />
	</Admin>
);

export default App;