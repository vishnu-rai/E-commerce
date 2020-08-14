import {firebaseApp, firebaseConfig, db} from './services/firebase'
import {FirebaseDataProvider} from 'react-admin-firebase'
import {cacheDataProviderProxy} from 'react-admin'

const options={
	app:firebaseApp
}
const dataProvider=FirebaseDataProvider(firebaseConfig,options)
const ex_dataProvider=(type,resource,params)=>{
	if(type==='DELETE' && resource==='Shop'){
		//deletes the products of the shop from 'Products' collection as well
		return(
			db.collection('Products').where('Shop_id','==',params.id).get()
			.then((querySnapshot)=>{
				let batch=db.batch()
				querySnapshot.forEach(doc=>{
					batch.delete(doc.ref)
				})
				const delShopRef=db.collection('Shop').doc(params.id)
				batch.delete(delShopRef)
				return batch.commit()
			})
			.then(()=>{
				return {data:params.previousData}
			})
		)
	}
	return dataProvider(type,resource,params)
}
const cachedDataProvider=cacheDataProviderProxy(ex_dataProvider,15*1000)
export {dataProvider, ex_dataProvider, cachedDataProvider}