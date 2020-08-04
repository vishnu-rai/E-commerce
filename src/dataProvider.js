import {firebaseApp, firebaseConfig} from './services/firebase'
import {FirebaseDataProvider} from 'react-admin-firebase'
import {cacheDataProviderProxy} from 'react-admin'

const options={
	app:firebaseApp
}

const dataProvider=FirebaseDataProvider(firebaseConfig,options)
const cachedDataProvider=cacheDataProviderProxy(dataProvider,15*1000)
export {dataProvider, cachedDataProvider}