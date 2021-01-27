import React, {useEffect} from 'react'
import {db} from '../services/firebase'
import {useNotify, useRefresh} from 'react-admin'
import {useLocation} from 'react-router-dom'

export const Notifier=()=>{
	const notify=useNotify()
	const location=useLocation()
	const refresh=useRefresh()
	useEffect(()=>{
		let isFirstCall=true
		db.collection('Orders').onSnapshot((snapshot)=>{
			if(isFirstCall){
				// as per onSnapshot documentation, the initial call returns the whole current result of the query
				// gotta skip these, and only notify on the subsequent ones
				isFirstCall=false
				return;
			}
			snapshot.docChanges().forEach((change)=>{
				console.log("yeueu")
				let parts=location.pathname.split('/')
				if((parts[1]==="Shop" && parts[2]===change.doc.data().Shop_id) || (parts[1]==="Orders")){
					refresh()
				}
				notify(`An order ${change.type}`)
			})
		})
	},[])
	return null
}