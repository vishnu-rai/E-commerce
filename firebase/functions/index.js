const functions = require('firebase-functions');
const admin = require('firebase-admin')
admin.initializeApp()

exports.createUserandShop = functions.https.onCall((data, context)=>{
  let {shopData} = data
  let shopRef
  if(!shopData.email){
    throw new functions.https.HttpsError('invalid-argument', 'No email specified')
  }

  return(
    admin.auth().createUser({
      email: shopData.email
    })
    .then(userRecord=>{
      console.log("Created user: ", userRecord.uid)
      shopRef = admin.firestore().collection('Shop').doc(userRecord.uid)
      return shopRef.create({
        shop_id: userRecord.uid,
        ...shopData
      })
    })
    .then(res=>{
      return shopRef.get()
    })
    .then(doc=>{
      if(doc.exists){
        return ({shop: {id: doc.id, ...doc.data()}})
      }
      throw new Error('Created document not found')
    })
    .catch(error=>{
      console.error(error)
      throw new functions.https.HttpsError('internal', error.message)
    })
  )
})