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

	if(type==='UPDATE' && resource==='Shop'){
		console.log("Caught ya")
		let {Item: currItems=[], ...newData} = params.data
		let {Item: prevItems=[], ...prevData} = params.previousData
		let shopRef = db.collection('Shop').doc(params.id)
		let itemRef = shopRef.collection('Items')

		let currHash = currItems.reduce((acc, val)=>{
			acc[val.id] = val
			return acc
		}, {})
		let prevHash = prevItems.reduce((acc, val)=>{
			acc[val.id] = val
			return acc
		}, {})

		let batch = db.batch()
		batch.update(shopRef, newData)
		prevItems.forEach(item=>{
			if(!currHash[item.id]){
				let docRef = itemRef.doc(item.id)
				batch.delete(docRef)
			}
		})
		currItems.forEach(item=>{
			if(!prevHash[item.id]){
				let docRef = itemRef.doc(item.id)
				let {id, ...itemData} = item
				batch.set(docRef, itemData)
			}
		})

		return (
			batch.commit()
			.then(()=>{
				return {data: params.data}
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

	if(type==='GET_ONE' && resource==='Shop'){
		let shopRef = db.collection('Shop').doc(params.id)
		let itemRef = shopRef.collection('Items')
		return Promise.all([shopRef.get(), itemRef.get()])
		.then(([shopDoc, itemSnap])=>{
			let itemsArray = itemSnap.docs.map(doc=>({id: doc.id, ...doc.data()}))
			return {data: {id: params.id, ...shopDoc.data(), Item: itemsArray}}
		})
	}

	if(type==='GET_LIST' && resource==='Category/Item'){
		console.log("HHIIIT")
		const {shop_id, category} = params.filter
		return db.collection('Category')
			.doc(category)
			.collection('Item')
			.get()
			.then(querySnapshot=>{
				const itemArray = querySnapshot.docs.map(doc=>({id:doc.id, ...doc.data()}))
				console.log({itemArray})
				return {data: itemArray, total: itemArray.length}
			})
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