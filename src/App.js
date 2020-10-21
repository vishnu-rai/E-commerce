import React, {useEffect} from 'react'
import {Admin, Resource, ListGuesser, EditGuesser, ShowGuesser} from 'react-admin'
import {cachedDataProvider, ex_dataProvider, dataProvider, authProvider} from './providers'
import {ProductList, ProductEdit, ProductCreate} from './comp/products'
import {ShopList, ShopEdit, ShopCreate} from './comp/shops'
import {ShopShow} from './comp/shopShow'
import {ServiceList, ServiceEdit, ServiceCreate} from './comp/services'
import {OrderList, OrderEdit, OrderCreate} from './comp/orders'
import {CategoryList, CategoryEdit, CategoryCreate, CategoryShow} from './comp/category'
import {AddList, AddEdit, AddCreate} from './comp/add'
import {Notifier} from './comp/notifier'

const App = () => (
	<Admin dataProvider={ex_dataProvider}>
		<Resource name="Products" list={ProductList} edit={ProductEdit} create={ProductCreate} />
		<Resource name="Shop" list={ShopList} edit={ShopEdit} create={ShopCreate} show={ShopShow}/>
		<Resource name="Orders" list={OrderList} edit={OrderEdit} create={OrderCreate} />
		<Resource name="Services" list={ServiceList} edit={ServiceEdit} create={ServiceCreate}/>
		<Resource name="Category" list={CategoryList} edit={CategoryEdit} create={CategoryCreate} show={CategoryShow} />
		<Resource name="Add" list={AddList} edit={AddEdit} create={AddCreate} />
		<Notifier />
	</Admin>
);

export default App;