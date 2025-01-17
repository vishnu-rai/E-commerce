import {firebaseApp, firebaseConfig, db, storage} from './services/firebase'
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
	if(type === 'UPDATE' && (resource==='Category' || resource==='BCategory')){
		const {data} = params
		if(data.meta && data.meta.isSubCollection){
			let itemRef = db.collection(resource)
				.doc(params.id)
				.collection('Item')

			if(data.meta.type === 'CREATE'){
					return (
					itemRef.doc()
					.set(data.values)
					.then(()=>{
						return itemRef.get()
					})
					.then(querySnapshot=>{
						const {Item, ...prevData} = params.previousData
						const itemArray = querySnapshot.docs.map(doc=>({id: doc.id, ...doc.data()}))
						return {data: {...prevData, Item: itemArray}}
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
		const {id: exId, Item: exItem,image: newImg="", ...newData} = params.data
		const {image: prevImg=""} = params.previousData
		const docRef = db.collection(resource).doc(params.id)

		return docRef.update(newData).then(async ()=>{
			if((newImg === prevImg) || !newImg){
				return {data: params.data}
			}
			const categoryId = params.id
			const imageRef = storage.ref().child("Category").child(categoryId+"_CategImage")
			await imageRef.put(newImg.rawFile)
			const url = await imageRef.getDownloadURL()
			await docRef.update({
				image: url
			})
			return {data: {...params.data, image: url}}
		})
	}

	if(type==='CREATE' && (resource==='Category' || resource==='BCategory')){
		const {image, ...categoryData} = params.data
		const docRef = db.collection(resource).doc()
		return (
			docRef.set(categoryData)
			.then(()=>{
				return docRef.get()
			})
			.then(async (doc)=>{
				const categoryId = doc.id
				if(!image){
					return {data: {id: categoryId, ...doc.data()}}
				} 
				const imageRef = storage.ref().child(resource).child(categoryId+"_CategImage")
				await imageRef.put(image.rawFile)
				const url = await imageRef.getDownloadURL()
				await docRef.update({
					image: url
				})
				return {data: {id: categoryId, ...doc.data(), image: url}}
			})
		)
	}

	if(type==='CREATE' && resource==='Shop'){
		console.log("shop waala", params)
		const {image, ...shopData} = params.data
		let createUserandShop = firebaseApp.functions().httpsCallable('createUserandShop');
		return (
			createUserandShop({shopData})
			.then(async (result)=>{
				const shopId = result.data.shop.id
				if(!image){
					return {data: result.data.shop}
				} 
				const imageRef = storage.ref().child(shopId+"_ShopImage")
				await imageRef.put(image.rawFile)
				const url = await imageRef.getDownloadURL()
				const docRef = db.collection("Shop").doc(shopId)
				await docRef.update({
					image: url
				})
				return {data: {...result.data.shop, image: url}}
			})
		)
	}

	if(type==='DELETE' && resource==='Shop'){ 
		//deletes the products of the shop from 'Products' collection as well
		const shopId = params.id
		if(!shopId){
		  return Promise.reject(new Error("Invalid Shop Id"))
		}
		return(
		  db.collection('Products').where('Shop_id', '==', shopId)
		  .get().then(async (querySnapshot) =>{
		    let batch = db.batch()
		    querySnapshot.docs.forEach(doc=>{
		      // let {list_image: imgURLs=[], Image: imgURL=""} = doc.data()
		      // imgURLs.push(imgURL)

		      // imgURLs.forEach(url=>{
		      //   if(url){
		      //     storage.refFromURL(url).delete()
		      //     .catch(error=>{
		      //       console.error("Error in deleting image: ", url, error.message)
		      //     })
		      //   }
		      //   batch.delete(doc.ref)
		      batch.update(doc.ref, {Stock: "0"})
		      })
		    const shopRef = db.collection('Shop').doc(shopId)
		    // const shopDoc = await shopRef.get()
		    // if(!shopDoc){
		    //   throw new Error("No shop found")
		    // }
		    // let url = shopDoc.data().image
		    // if(url){
		    //   storage.refFromURL(url).delete()
		    //   .catch(error=>{
		    //     console.error("Error in deleting image: ", url, error.message)
		    //   })
		    // }
		    // batch.delete(shopRef)
		    batch.update(shopRef, {status: "0"})
		    return batch.commit()
		  })
		  .then(()=>{
		    return {data: params.previousData}
		  })
		)
	}

	if(type==='UPDATE' && resource==='Shop'){
		console.log("Caught ya")
		let {Items: currItems=[], image: newImg, ...newData} = params.data
		let {Items: prevItems=[], image: prevImg, ...prevData} = params.previousData
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
				let {id, type: name} = item
				batch.set(docRef, {name})
			}
		})

		return (
			batch.commit()
			.then(async ()=>{
				if((newImg === prevImg) || !newImg){
					return {data: params.data}
				}
				const shopId = params.data.id
				const imageRef = storage.ref().child(shopId+"_ShopImage")
				await imageRef.put(newImg.rawFile)
				const url = await imageRef.getDownloadURL()
				await shopRef.update({
					image: url
				})
				return {data: {...params.data, image: url}}
			})
		)
	}

	if(type==='GET_ONE' && (resource==='Category' || resource==='BCategory')){
		const docRef = db.collection(resource).doc(params.id)
		return (
			Promise.all([docRef.get(), docRef.collection('Item').get()])
			.then(([mainDoc, itemSnap])=>{
				const itemArray = itemSnap.docs.map(doc=>({id:doc.id, ...doc.data()}))
				return {data: {id: params.id, ...mainDoc.data(), Item: itemArray}}
			})
		)
	}

	if(type==='GET_ONE' && resource==='Shop'){
		let shopRef = db.collection('Shop').doc(params.id)
		let itemRef = shopRef.collection('Items')
		return Promise.all([shopRef.get(), itemRef.get()])
		.then(([shopDoc, itemSnap])=>{
			let itemsArray = itemSnap.docs.map(doc=>({id: doc.id, ...doc.data()}))
			return {data: {id: params.id, ...shopDoc.data(), Items: itemsArray}}
		})
	}

	if(type==='GET_LIST' && resource==='Category/Item'){
		console.log("HHIIIT")
		const {shop_id, category, category_type} = params.filter
		return db.collection(category_type).where("name", '==', category)
			.get()
			.then(querySnapshot=>{
				const docRef = querySnapshot.docs[0].ref
				return docRef.collection('Item').get()
			})
			.then(querySnapshot=>{
				const itemArray = querySnapshot.docs.map(doc=>({id:doc.id, ...doc.data()}))
				console.log({itemArray})
				return {data: itemArray, total: itemArray.length}
			})
	}

	if(type==='CREATE' && (resource==='Add')){
		const {image, ...categoryData} = params.data
		const docRef = db.collection(resource).doc()
		return (
			db.collection('Shop').doc(categoryData.shop_id)
			.get()
			.then(doc=>{
				categoryData["category"] = doc.data().category
				return docRef.set(categoryData)
			})
			.then(()=>{
				return docRef.get()
			})
			.then(async (doc)=>{
				const docId = doc.id
				if(!image){
					return {data: {id: docId, ...doc.data()}}
				} 
				const imageRef = storage.ref().child(resource).child(docId+"_AddImage")
				await imageRef.put(image.rawFile)
				const url = await imageRef.getDownloadURL()
				await docRef.update({
					image: url
				})
				return {data: {id: docId, ...doc.data(), image: url}}
			})
		)
	}

	if(type==='DELETE' && resource==='Add'){
		const docRef = db.collection(resource).doc(params.id)
		let url = params.previousData.image || ""
		if(url){
			return (
				storage.refFromURL(url).delete()
				.then(()=>{
					return docRef.delete()
				})
				.then(()=>{
					return {data: params.previousData}
				})
			)
		}
		return docRef.delete().then(()=>{
			return {data: params.previousData}
		})
	}
	return dataProvider(type,resource,params)
}
const cachedDataProvider=cacheDataProviderProxy(ex_dataProvider,15*1000)
export {dataProvider, ex_dataProvider, cachedDataProvider, authProvider}