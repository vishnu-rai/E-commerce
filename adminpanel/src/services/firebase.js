import firebase from 'firebase'

const firebaseConfig = {
  apiKey: process.env.REACT_APP_APIKEY,
  authDomain: process.env.REACT_APP_AUTHDOMAIN,
  databaseURL: process.env.DATABASEURL,
  projectId: process.env.REACT_APP_PROJECTID,
  storageBucket: process.env.REACT_APP_STORAGEBUCKET,
  messagingSenderId: process.env.REACT_APP_MESSAGINGSENDERID,
  appId: process.env.REACT_APP_APPID,
  // measurementId: process.env.REACT_APP_MEASUREMENTID
};
firebase.initializeApp(firebaseConfig)
const db=firebase.firestore()
const storage = firebase.storage()
export const firebaseApp=firebase
export {db, storage, firebaseConfig}


