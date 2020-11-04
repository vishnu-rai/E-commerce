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

  const selCategoryType = values["category_type"]
  const selCategory = values.category
  const [currCategory, setCurrentCategory] = useState("")
  const [currCategoryType, setCurrCategoryType] = useState("")

  const label = props.source

  useEffect(()=>{
    if(((selCategory !== currCategory) || (selCategoryType !== currCategoryType)) && !isLoading){
      setIsLoading(true)
      dataProvider.getList('Category/Item',
            {filter: {category_type: selCategoryType, category: selCategory}}
          )
      .then(({data})=>{
        if(!isInit){
          console.log("OHH")
          onChange([])
        }
        else setIsInit(false)
        setCurrentCategory(selCategory)
        setCurrCategoryType(selCategoryType)
        setChoices(data)
        setIsLoading(false)
      })
      .catch(error=>{
        onChange([])
        setIsLoading(false)
        console.log("ERororoor")
      })
    }
  }, [selCategory, selCategoryType, isInit, isLoading, dataProvider, onChange])

  const inputValues = value ? value : []

  if(isLoading) return <Loading />
  return(
    <Autocomplete
      multiple
      value={inputValues}
      options={choices}
      getOptionLabel={option=>(option.type || option.name || option.id)}
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