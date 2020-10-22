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
	console.log(type, resource);
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
	if(type==='UPDATE' && resource==='Category'){
		console.log(type, resource, params)
		const {data, previousData} = params
		const dataHash = data.Item.reduce((acc, curr)=>{
			acc[curr.id] = curr;
			return acc;
		}, {})
		const prevDataHash = previousData.Item.reduce((acc, curr)=>{
			acc[curr.id] = curr;
			return acc;
		}, {})
		const addedIds = data.Item.reduce((acc, item)=>{
			if(!prevDataHash[item.id]){
				acc.push(item.id)
			}
			return acc
		}, [])
		const deletedIds = previousData.Item.reduce((acc, item)=>{
			if(!dataHash[item.id]){
				acc.push(item.id)
			}
			return acc
		}, [])

		console.log({addedIds, deletedIds})
		const itemRef = db.collection('Category')
			.doc(params.id)
			.collection('Item')

		let batch = db.batch()
		addedIds.forEach(id=>{
			let newDocRef = itemRef.doc(id)
			delete dataHash[id].id
			batch.set(newDocRef, {...dataHash[id]})
		})
		deletedIds.forEach(id=>{
			let delDocRef = itemRef.doc(id)
			batch.delete(delDocRef)
		})

		return(
			batch.commit().then(()=>{
				return itemRef.get()
			})
			.then(querySnapshot=>{
				console.log("Inside")
				const itemArray = querySnapshot.docs.map(doc=>({id: doc.id, ...doc.data()}))
				console.log({data: {id: params.id, name: params.id, Item: itemArray}})
				return {data: {id: params.id, name: params.id, Item: itemArray}}
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