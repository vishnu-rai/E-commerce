import React, {useState, useEffect} from 'react'
import {useGetList, useDataProvider, Loading, useInput} from 'react-admin'
import Autocomplete from '@material-ui/lab/Autocomplete'
import TextField from '@material-ui/core/TextField'
import {useFormState} from 'react-final-form'

const EffDropDown = props=>{
  const [choices, setChoices] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const [isInit, setIsInit] = useState(true)
  const {values} = useFormState()
  const {input: {onChange, value}} = useInput(props)
  const dataProvider = useDataProvider()

  const selCategory = values.category
  const [currCategory, setCurrentCategory] = useState("")
  console.log({props})

  const label = props.source

  useEffect(()=>{
    if(selCategory !== currCategory && !isLoading){
      setIsLoading(true)
      dataProvider.getList('Category/Item',
            {filter: {category: selCategory}}
          )
      .then(({data})=>{
        if(!isInit){
          console.log("OHH")
          onChange([])
        }
        else setIsInit(false)
        setCurrentCategory(selCategory)
        setChoices(data)
        setIsLoading(false)
      })
      .catch(error=>{
        setIsLoading(false)
        setCurrentCategory(selCategory)
        console.log("ERororoor")
      })
    }
  }, [selCategory, isInit, isLoading, dataProvider, onChange])

  const inputValues = value ? value : []

  if(isLoading) return <Loading />
  return(
    <Autocomplete
      multiple
      value={inputValues}
      options={choices}
      getOptionLabel={option=>option.type}
      getOptionSelected={(option, value)=>{
        return option.id === value.id
      }}
      onChange={(event, newValues)=>{
        onChange(newValues)
      }}
      renderInput={(params) => (
        <TextField
          {...params}
          variant="standard"
          label={label}
        />
      )}
    />  
  )
}

export default EffDropDown