import {firebaseApp, firebaseConfig, db} from './services/firebase'
import {FirebaseDataProvider, FirebaseAuthProvider} from 'react-admin-firebase'
import {cacheDataProviderProxy} from 'react-admin'

const options={
	app:firebaseApp,
	disableMeta: true
}
const dataProvider=FirebaseDataProvider(firebaseConfig,options)
const authProvider=FirebaseAuthProvider(firebaseConfig,options)
const ex_dataProvider=(type,resource,params)=>{
	console.log(type, resource, params);
	if(type === 'UPDATE' && resource === 'Category'){
		const {data} = params
		if(data.meta && data.meta.isSubCollection){
			let itemRef = db.collection('Category')
				.doc(params.id)
				.collection('Item')
			if(data.meta.type === 'CREATE'){
					return (
					itemRef.doc(data.values.id)
					.set(data.values)
					.then(()=>{
						return itemRef.get()
					})
					.then(querySnapshot=>{
						const itemArray = querySnapshot.docs.map(doc=>({id: doc.id, ...doc.data()}))
						return {data: {id: params.id, name: params.id, Item: itemArray}}
					})
				)
			}
			else if(data.meta.type === 'DELETE'){
				return(
					itemRef.doc(data.itemId).delete()
					.then(()=>{
						return itemRef.get()
					})
					.then(querySnapshot=>{
						const itemArray = querySnapshot.docs.map(doc=>({id: doc.id, ...doc.data()}))
						return {data: {id: params.id, name: params.id, Item: itemArray}}
					})
				)
			}
		}
	}

	if(type==='CREATE' && resource==='Shop'){
		console.log("shop waala", params)
		const shopData = params.data
		let createUserandShop = firebaseApp.functions().httpsCallable('createUserandShop');
		return (
			createUserandShop({shopData})
			.then(result=>{
				console.log({resp: result.data.shop})
				return {data: result.data.shop}
			})
		)
	}

	if(type==='GET_ONE' && resource==='Category'){
		return(
			db.collection('Category')
				.doc(params.id)
				.collection('Item')
				.get()
				.then(querySnapshot=>{
					const itemArray = querySnapshot.docs.map(doc=>({id:doc.id, ...doc.data()}))
					console.log({itemArray})
					return {data: {id: params.id, name: params.id, Item: itemArray}}
				})
		)
	}
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
export {dataProvider, ex_dataProvider, cachedDataProvider, authProvider}