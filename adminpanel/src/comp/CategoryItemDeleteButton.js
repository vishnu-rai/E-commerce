import React, {useState} from 'react'
// import { Link } from 'react-router-dom';
// import ChatBubbleIcon from "@material-ui/icons/ChatBubble";
import { withStyles } from '@material-ui/core/styles';
import IconItemDelete from '@material-ui/icons/Delete';
import { Button, useUpdate, useRefresh, useNotify } from 'react-admin';
import CircularProgress from '@material-ui/core/CircularProgress';


const styles = {
  button: {
    margin: '1em',
    backgroundColor:'rgb(33, 150, 243)',
    color:'white'
  }
};

const CategoryItemDeleteButton = props=>{
  const [showDialog, setShowDialog] = useState(false);
  const [update, { loading }] = useUpdate(props.resource)
  const notify = useNotify();
  const refresh = useRefresh();
  // const form = useForm();

  // const handleClick = () => {
  //   setShowDialog(true);
  // };

  // const handleCloseClick = () => {
  //   setShowDialog(false);
  // };

  const categoryId = props.parentId
  const itemId = props.record.id

  const handleClick = async values => {
      update(
        { payload: {id: categoryId, data: {itemId, meta: {isSubCollection: true, type: 'DELETE'}}}},
        {
          onSuccess: ({ data }) => {
            refresh()
            setShowDialog(false);
          },
          onFailure: ({ error }) => {
            notify(error.message, 'error');
          }
        }
      );
  };

  if(loading){
    return <CircularProgress />
  }

  return (
      <Button onClick={handleClick} label="ra.action.delete">
        <IconItemDelete />
      </Button>
    )
}

export default CategoryItemDeleteButton