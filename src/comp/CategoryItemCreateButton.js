import React, { useState } from 'react';
import { useForm } from 'react-final-form';
import {
  required,
  Button,
  SaveButton,
  SimpleForm,
  TextInput,
  useUpdate,
  useNotify,
  useRefresh,
  useTranslate,
  Toolbar
} from 'react-admin';
import IconContentAdd from '@material-ui/icons/Add';
import IconCancel from '@material-ui/icons/Cancel';

import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import CircularProgress from '@material-ui/core/CircularProgress';

function CategoryItemCreateButton(props) {
  const [showDialog, setShowDialog] = useState(false);
  const [update, { loading }] = useUpdate(props.customProps.resource);
  const notify = useNotify();
  const refresh = useRefresh();
  // const form = useForm();

  const handleClick = () => {
    setShowDialog(true);
  };

  const handleCloseClick = () => {
    setShowDialog(false);
  };

  const categoryId = props.customProps.categoryId

  const handleSubmit = async values => {
      update(
        { payload: {id: categoryId, data: {values, meta: {isSubCollection: true, type: 'CREATE'}}}},
        {
          onSuccess: ({ data }) => {
            setShowDialog(false);
            // Update the comment form to target the newly created post
            // Updating the ReferenceInput value will force it to reload the available posts
            // form.change('post_id', data.id);
          },
          onFailure: ({ error }) => {
            notify(error.message, 'error');
          }
        }
      );
  };

  return (
    <>
      <Button onClick={handleClick} label="Add Item">
        <IconContentAdd />
      </Button>
      <Dialog
        fullWidth
        open={showDialog}
        onClose={handleCloseClick}
        aria-label="Add Item"
      >
        <DialogTitle>Add Item to Category</DialogTitle>
        <DialogContent>
          <SimpleForm
            resource="Category"
            // We override the redux-form onSubmit prop to handle the submission ourselves
            save={handleSubmit}
            // We want no toolbar at all as we have our modal actions
            toolbar={<CategoryItemCreateButtonToolbar loading={loading} onCancel={handleCloseClick} />}
          >
            <TextInput source="type" validate={required()} />
          </SimpleForm>
        </DialogContent>
      </Dialog>
    </>
  );
}

export default CategoryItemCreateButton;

function CategoryItemCreateButtonToolbar({ onCancel, loading, ...props }) {
  return (
    <Toolbar {...props}>
      {
        loading ?
        <CircularProgress />
        :<SaveButton submitOnEnter={true} />
      }
      <CancelButton onClick={onCancel} />
    </Toolbar>
  );
}

function CancelButton(props) {
  const translate = useTranslate();
  return (
    <Button label={translate('ra.action.cancel')} {...props}>
      <IconCancel />
    </Button>
  );
}
