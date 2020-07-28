import {firebaseApp, firebaseConfig} from './services/firebase'
import {FirebaseDataProvider} from 'react-admin-firebase'

const options={
	app:firebaseApp
}

export const dataProvider=FirebaseDataProvider(firebaseConfig,options)

