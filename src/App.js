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
import {CouponList, CouponEdit, CouponCreate} from './comp/coupon'
import {Notifier} from './comp/notifier'

const App = () => (
	<Admin dataProvider={ex_dataProvider} authProvider={authProvider}>
		
		<Resource name="Shop" list={ShopList} edit={ShopEdit} create={ShopCreate} show={ShopShow}/>
		<Resource name="Orders" list={OrderList} edit={OrderEdit} create={OrderCreate} />
		<Resource name="Category" list={CategoryList} edit={CategoryEdit} create={CategoryCreate} show={CategoryShow} />
		<Resource name="BCategory" list={CategoryList} edit={CategoryEdit} create={CategoryCreate} show={CategoryShow} />
		<Resource name="Add" list={AddList} edit={AddEdit} create={AddCreate} />
		<Resource name="Coupon" list={CouponList} edit={CouponEdit} create={CouponCreate} />
		<Resource name="Products"/>
    <Resource name="Category/Item" />
		<Notifier />
	</Admin>
);

export default App;