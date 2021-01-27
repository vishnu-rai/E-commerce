package stop.one.ozo_seller.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import stop.one.ozo_seller.R;

public class add_other_products extends AppCompatActivity {

    String item, category, item_name="", item_brand="", item_price="", item_image="",shop_id,shop_name,shop_image;
    Uri image_url=null;
    TextView add;
    EditText name, brand, price;
    ImageView image;
    private static final int SELECT_IMAGE = 234;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_products);

        progressDialog = new ProgressDialog(add_other_products.this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        item = getIntent().getStringExtra("item");
        category = getIntent().getStringExtra("category");
        shop_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Shop").document(shop_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                shop_name=documentSnapshot.getString("name");
                shop_image=documentSnapshot.getString("image");
                progressDialog.dismiss();
            }
        });

        add = findViewById(R.id.add);
        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });
        
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()) {
                    progressDialog.show();
                    uploadFile();
                }
            }
        });
    }

    private Boolean check() {
        item_name=name.getText().toString();
        item_brand=brand.getText().toString();
        item_price=price.getText().toString();
        if(image_url==null) {
            Toast.makeText(getApplicationContext(), "Choose a image", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(item_name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter the product name", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(item_brand.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter the product brand", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(item_price.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter the product price", Toast.LENGTH_LONG).show();
            return false;
        }
        else 
            return true;
    }

    protected void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image_url = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_url);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        final ProgressDialog progressdialog = new ProgressDialog(this);
        progressdialog.setTitle("Uploading");
        progressdialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        String image_name_save=shop_id+currentDateandTime;

        StorageReference riversRef = storage.getReference().child(image_name_save);

        riversRef.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                        new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                item_image = (task.getResult().toString());
                                progressdialog.dismiss();
                                save_data();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressdialog.dismiss();
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NotNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressdialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });

    }

    private void save_data() {

        String document_id= FirebaseFirestore.getInstance().collection("Products").document().getId();
        List list=new ArrayList();

        Map<String,Object> detail=new HashMap<>();
        detail.put("Name",item_name);
        detail.put("Brand",item_brand);
        detail.put("Description","");
        detail.put("Price",item_price);
        detail.put("Mrp","");
        detail.put("Shop_id",shop_id);
        detail.put("Category",category);
        detail.put("Item",item);
        detail.put("product_id",document_id);
        detail.put("Image",item_image);
        detail.put("list_image",list);
        detail.put("Size",list);
        detail.put("Stock","1");
        detail.put("Shop_name",shop_name);
        detail.put("Shop_image",shop_image);

        FirebaseFirestore.getInstance().collection("Products").document(document_id)
                .set(detail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

}