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

function CategoryItemCreateButton(props) {
  const [showDialog, setShowDialog] = useState(false);
  const [update, { loading }] = useUpdate('Category');
  const notify = useNotify();
  const refresh = useRefresh();
  // const form = useForm();

  const handleClick = () => {
    setShowDialog(true);
  };

  const handleCloseClick = () => {
    setShowDialog(false);
  };

  const categoryId = props.record.id

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
      <Button onClick={handleClick} label="ra.action.create">
        <IconContentAdd />
      </Button>
      <Dialog
        fullWidth
        open={showDialog}
        onClose={handleCloseClick}
        aria-label="Add Item"
      >
        <DialogTitle>Create post</DialogTitle>
        <DialogContent>
          <SimpleForm
            resource="Category"
            // We override the redux-form onSubmit prop to handle the submission ourselves
            save={handleSubmit}
            // We want no toolbar at all as we have our modal actions
            toolbar={<CategoryItemCreateButtonToolbar onCancel={handleCloseClick} />}
          >
            <TextInput source="id" validate={required()} />
            <TextInput source="type" validate={required()} />
            <TextInput source="image" validate={required()} />
          </SimpleForm>
        </DialogContent>
      </Dialog>
    </>
  );
}

export default CategoryItemCreateButton;

function CategoryItemCreateButtonToolbar({ onCancel, ...props }) {
  return (
    <Toolbar {...props}>
      <SaveButton submitOnEnter={true} />
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
