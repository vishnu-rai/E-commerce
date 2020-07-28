import React from 'react'
import {Admin,Resource,ListGuesser} from 'react-admin'
import {dataProvider} from './dataProvider'
import {ProductList} from './comp/products'
const App = () => (
	<Admin dataProvider={dataProvider}>
		<Resource name="Products" list={ProductList} />
	</Admin>
);

export default App;