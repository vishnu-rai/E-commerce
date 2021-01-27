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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import stop.one.ozo_seller.R;

public class add_clothing_product extends AppCompatActivity {

    private static final int SELECT_IMAGE = 234;
    int click_count;
    String item, category, document_id, shop_id, shop_name = "", shop_image = "";
    EditText name, brand, price, mrp, description, size1, size2, size3, size4, size5, size6, size7;
    TextView submit;
    ImageView image0, image1, image2, image3, image4;
    String names, brands, prices, mrps, descriptions;
    List<ImageView> image_list = new ArrayList<>();
    List<Uri> image_url = new ArrayList<>();
    List<String> image_link = new ArrayList<>();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    String s1, s2, s3, s4, s5, s6, s7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothing_product);

        item = getIntent().getStringExtra("item");
        category = getIntent().getStringExtra("category");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        submit = findViewById(R.id.submit);
        name = findViewById(R.id.name);
        brand = findViewById(R.id.brand);
        price = findViewById(R.id.price);
        mrp = findViewById(R.id.mrp);
        description = findViewById(R.id.description);
        image0 = findViewById(R.id.image0);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        size1 = findViewById(R.id.size1);
        size2 = findViewById(R.id.size2);
        size3 = findViewById(R.id.size3);
        size4 = findViewById(R.id.size4);
        size5 = findViewById(R.id.size5);
        size6 = findViewById(R.id.size6);
        size7 = findViewById(R.id.size7);

        image_list.add(image0);
        image_list.add(image1);
        image_list.add(image2);
        image_list.add(image3);
        image_list.add(image4);

        shop_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("Shop").document(shop_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                shop_image = documentSnapshot.getString("image");
                shop_name = documentSnapshot.getString("name");
                progressDialog.dismiss();
            }
        });

        image0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_count = 0;
                selectImageFromGallery();
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_count = 1;
                if (image_url.size() > 0)
                    selectImageFromGallery();
                else
                    Toast.makeText(getApplicationContext(), "Choose main image first", Toast.LENGTH_SHORT).show();
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_count = 2;
                if (image_url.size() > 0)
                    selectImageFromGallery();
                else
                    Toast.makeText(getApplicationContext(), "Choose main image first", Toast.LENGTH_SHORT).show();
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_count = 3;
                if (image_url.size() > 0)
                    selectImageFromGallery();
                else
                    Toast.makeText(getApplicationContext(), "Choose main image first", Toast.LENGTH_SHORT).show();
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_count = 4;
                if (image_url.size() > 0)
                    selectImageFromGallery();
                else
                    Toast.makeText(getApplicationContext(), "Choose main image first", Toast.LENGTH_SHORT).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
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
            image_url.add(click_count, data.getData());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_url.get(click_count));
                image_list.get(click_count).setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadData() {
        names = name.getText().toString();
        brands = brand.getText().toString();
        descriptions = description.getText().toString();
        prices = price.getText().toString();
        mrps = mrp.getText().toString();
        s1 = size1.getText().toString();
        s2 = size2.getText().toString();
        s3 = size3.getText().toString();
        s4 = size4.getText().toString();
        s5 = size5.getText().toString();
        s6 = size6.getText().toString();
        s7 = size7.getText().toString();

        if (s1.isEmpty() && s2.isEmpty() && s3.isEmpty() && s4.isEmpty() && s5.isEmpty() && s6.isEmpty() && s7.isEmpty())
            size1.setError("Empty");
        else if (names.isEmpty())
            name.setError("Empty");
        else if (brands.isEmpty())
            brand.setError("Empty");
        else if (prices.isEmpty())
            price.setError("Empty");
        else if (mrps.isEmpty())
            mrp.setError("Empty");
        else if (descriptions.isEmpty())
            description.setError("Empty");
        else if (image_url.size() < 5)
            Toast.makeText(getApplicationContext(), "Empty image", Toast.LENGTH_SHORT).show();
        else {
            progressDialog.show();
            uploadFile();
        }

    }

    private void uploadFile() {
        if (image_url.size() == 5) {
            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading");
            progressdialog.show();

            for (int i = 0; i < image_url.size(); i++) {
                final int finalI = i;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                String image_name_save = shop_id + currentDateandTime + String.valueOf(i + 1523);

                StorageReference riversRef = storage.getReference().child(image_name_save);

                riversRef.putFile(image_url.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        image_link.add(task.getResult().toString());
                                        if (finalI == 4) {
                                            progressdialog.dismiss();
                                            setData();
                                        }
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressdialog.dismiss();
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
        } else {
            Toast.makeText(getApplicationContext(), "Choose image", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    private void setData() {
        document_id = firebaseFirestore.collection("Products").document().getId();

        List<String> lst = new ArrayList<>();
        lst.add(image_link.get(1));
        lst.add(image_link.get(2));
        lst.add(image_link.get(3));
        lst.add(image_link.get(4));

        List<String> list = new ArrayList<>();
        if (!s1.isEmpty())
            list.add(s1.toUpperCase());
        if (!s2.isEmpty())
            list.add(s2.toUpperCase());
        if (!s3.isEmpty())
            list.add(s3.toUpperCase());
        if (!s4.isEmpty())
            list.add(s4.toUpperCase());
        if (!s5.isEmpty())
            list.add(s5.toUpperCase());
        if (!s6.isEmpty())
            list.add(s6.toUpperCase());
        if (!s7.isEmpty())
            list.add(s7.toUpperCase());

        Map<String, Object> detail = new HashMap<>();
        detail.put("Name", names);
        detail.put("Brand", brands);
        detail.put("Description", descriptions);
        detail.put("Price", prices);
        detail.put("Mrp", mrps);
        detail.put("Shop_id", shop_id);
        detail.put("Category", category);
        detail.put("Item", item);
        detail.put("product_id", document_id);
        detail.put("Size", list);
        detail.put("Image", image_link.get(0));
        detail.put("list_image", lst);
        detail.put("Stock", "1");
        detail.put("Shop_name", shop_name);
        detail.put("Shop_image", shop_image);

        firebaseFirestore.collection("Products").document(document_id)
                .set(detail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}