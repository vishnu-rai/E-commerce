package stop.one.ozo_seller.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import holder.product_holder;
import stop.one.ozo_seller.R;

public class product_list extends AppCompatActivity {

    String item_name, category, shop_id;
    RecyclerView product_list_recycler;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    FirestoreRecyclerAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        shop_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent startingIntent = getIntent();
        item_name = startingIntent.getStringExtra("item");
        category = startingIntent.getStringExtra("category");

        product_list_recycler = findViewById(R.id.product_list_recycler);

        Query query = rootRef.collection("Products").whereEqualTo("Shop_id", shop_id).whereEqualTo("Item", item_name);

        FirestoreRecyclerOptions<product_holder> options = new FirestoreRecyclerOptions.Builder<product_holder>()
                .setQuery(query, product_holder.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<product_holder, product_list_RecyclerAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final product_list_RecyclerAdapter holder, final int position, @NonNull final product_holder model) {

                holder.item_name.setText(model.getName().toUpperCase());
                Glide.with(getApplicationContext()).load(model.getImage()).into(holder.item_image);
                holder.brand_name.setText(model.getBrand());
                holder.price.setText(model.getPrice() + " Rs.");
                if (model.getStock().equals("0"))
                    holder.stock.setText("Not in stock");
                else
                    holder.stock.setText("In Stock");
                if (model.getSize().size() > 0) {
                    String s = "Size ";
                    for (int i = 0; i < model.getSize().size(); i++) {
                        s = s + " " + model.getSize().get(i);
                    }
                    holder.size.setText(s);
                } else
                    holder.size.setText("null");


                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                        final String docId = snapshot.getId();
                        Log.d("list_image_check", String.valueOf(model.getList_image()));
                        if (category.equalsIgnoreCase("Electronic") || category.equalsIgnoreCase("Footwear") || category.equalsIgnoreCase("Clothing")) {
                            for (int i = 0; i < model.getList_image().size(); i++) {
                                final int g = i;
                                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getList_image().get(i));
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (g == 3) {
                                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage());
                                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    FirebaseFirestore.getInstance().collection("Products").document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            progressDialog.dismiss();
                                                            finish();
                                                            startActivity(getIntent());
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(getApplicationContext(), "Not deleted", Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(getApplicationContext(), "Not deleted", Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        } else {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImage());
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseFirestore.getInstance().collection("Products").document(docId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Not deleted1", Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    }
                });

                holder.change_stock.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                    @Override
                    public void onClick(View v) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                        final String docId = snapshot.getId();
                        new Stockdialog(product_list.this, docId);
                    }
                });

                holder.change_price.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
                    @Override
                    public void onClick(View v) {
                        DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                        final String docId = snapshot.getId();
                        new Pricedialog(product_list.this, docId);
                    }
                });

            }

            @NonNull
            @Override
            public product_list_RecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.product_list_card_layout, parent, false);
                return new product_list_RecyclerAdapter(view);
            }
        };
        product_list_recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                progressDialog.dismiss();
            }
        });
        product_list_recycler.setAdapter(adapter);
        adapter.startListening();

    }

    public class product_list_RecyclerAdapter extends RecyclerView.ViewHolder {
        TextView item_name, brand_name, size, price, stock, change_price, change_stock, delete;
        ImageView item_image;
        LinearLayout product_layout;


        public product_list_RecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_image = itemView.findViewById(R.id.item_image);
            product_layout = itemView.findViewById(R.id.product_layout);
            brand_name = itemView.findViewById(R.id.brand_name);
            price = itemView.findViewById(R.id.price);
            size = itemView.findViewById(R.id.size);
            stock = itemView.findViewById(R.id.stock);
            change_price = itemView.findViewById(R.id.change_price);
            change_stock = itemView.findViewById(R.id.change_stock);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public class Pricedialog {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        public Pricedialog(Context context, String product) {

            ProgressDialog prog;
            final String product_id = product;

            final Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setElevation(5);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

            prog = new ProgressDialog(dialog.getContext());
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.setMessage("Loading. Please wait...");
            prog.setIndeterminate(true);
            prog.setCanceledOnTouchOutside(false);

            final EditText value;
            TextView title, update;

            value = dialog.findViewById(R.id.value);
            title = dialog.findViewById(R.id.title);
            update = dialog.findViewById(R.id.update);
            title.setText("Enter the new price");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val;
                    val = value.getText().toString();
                    if (val.isEmpty()) {
                        value.setError("Empty");
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("Price", val);
                        FirebaseFirestore.getInstance().collection("Products").document(product_id)
                                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            });
        }
    }

    public class Stockdialog {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        public Stockdialog(Context context, String product) {

            ProgressDialog prog;
            final String product_id = product;

            final Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_layout);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setElevation(5);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();

            prog = new ProgressDialog(dialog.getContext());
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.setMessage("Loading. Please wait...");
            prog.setIndeterminate(true);
            prog.setCanceledOnTouchOutside(false);

            final EditText value;
            TextView title, update;

            value = dialog.findViewById(R.id.value);
            title = dialog.findViewById(R.id.title);
            update = dialog.findViewById(R.id.update);
            title.setText("Update the stock \n 0- For out of stock \n 1- For in stock");
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String val;
                    val = value.getText().toString();
                    if (val.isEmpty()) {
                        value.setError("Empty");
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        if (val.equals("0"))
                            map.put("Stock", val);
                        else
                            map.put("Stock", "1");
                        FirebaseFirestore.getInstance().collection("Products").document(product_id)
                                .update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            });
        }
    }

}