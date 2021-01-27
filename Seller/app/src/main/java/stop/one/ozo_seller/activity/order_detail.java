package stop.one.ozo_seller.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.razorpay.Checkout;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import holder.cart_holder;
import stop.one.ozo_seller.R;

public class order_detail extends AppCompatActivity {

    TextView name, city, state, phone, area, order_date, delivery_status, payment_type,
            cancel_order, price, pincode, house_no, accept_order;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    String order_id, address_id, userid, payment_id, total_price, shop_id;
    ProgressDialog progressDialog;
    FirestoreRecyclerAdapter adapter;
    RecyclerView order_recycler;

    List<String> product_id = new ArrayList<>();
    List<String> quan = new ArrayList<>();
    List<String> size = new ArrayList<>();
    Map<String, String> sze = new HashMap<>();
    Map<String, String> quantity = new HashMap<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        Checkout.preload(getApplicationContext());

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        order_id = getIntent().getStringExtra("Order_id");
        product_id = getIntent().getStringArrayListExtra("Product_id");
        size = getIntent().getStringArrayListExtra("Size");
        quan = getIntent().getStringArrayListExtra("Quan");
        address_id = getIntent().getStringExtra("Address_id");
        userid = getIntent().getStringExtra("User_id");
        shop_id = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Log.d("product_list", String.valueOf(product_id));
        for (int i = 0; i < product_id.size(); i++) {
            sze.put(product_id.get(i), size.get(i));
            quantity.put(product_id.get(i), quan.get(i));
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        order_recycler = findViewById(R.id.order_recycler);

        name = findViewById(R.id.name);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        phone = findViewById(R.id.phone);
        area = findViewById(R.id.area);
        order_date = findViewById(R.id.order_date);
        delivery_status = findViewById(R.id.delivery_status);
        payment_type = findViewById(R.id.payment_type);
        cancel_order = findViewById(R.id.cancel_order);
        price = findViewById(R.id.price);
        pincode = findViewById(R.id.pincode);
        house_no = findViewById(R.id.house_no);
        accept_order = findViewById(R.id.accept_order);

        load_recycler();


        firebaseFirestore.collection("Users").document(userid).collection("Address")
                .document(address_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getString("Name"));
                phone.setText(documentSnapshot.getString("Phone"));
                area.setText(documentSnapshot.getString("Area"));
                state.setText(documentSnapshot.getString("State"));
                house_no.setText(documentSnapshot.getString("House_no"));
                city.setText(documentSnapshot.getString("City"));
                pincode.setText(documentSnapshot.getString("Pincode"));
            }
        });

        firebaseFirestore.collection("Orders").document(order_id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                price.setText(documentSnapshot.getString("Total_price"));
                String status = documentSnapshot.getString("Status");
                payment_id = documentSnapshot.getString("Payment_id");
                total_price = documentSnapshot.getString("Total_price");
                payment_type.setText(documentSnapshot.getString("Payment_type"));
                if (status.equalsIgnoreCase("Pending")) {
                    delivery_status.setText(documentSnapshot.getString("Status"));
                    cancel_order.setVisibility(View.VISIBLE);
                    accept_order.setVisibility(View.VISIBLE);
                } else if (status.equalsIgnoreCase("Accepted")) {
                    cancel_order.setVisibility(View.GONE);
                    accept_order.setVisibility(View.GONE);
                    delivery_status.setText(documentSnapshot.getString("Status"));
                } else if (status.equalsIgnoreCase("Cancelled")) {
                    cancel_order.setVisibility(View.GONE);
                    accept_order.setVisibility(View.GONE);
                    delivery_status.setText(documentSnapshot.getString("Status"));
                }
            }
        });

        firebaseFirestore.collection("Orders").document(order_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    return;
                }
                if (value != null && value.exists()) {
                    String str=value.getString("Status");
                    delivery_status.setText(str);

                    if (str.equalsIgnoreCase("Pending")) {
                        cancel_order.setVisibility(View.VISIBLE);
                        accept_order.setVisibility(View.VISIBLE);
                    }
                    else if (str.equalsIgnoreCase("Cancelled"))
                    {
                        cancel_order.setVisibility(View.GONE);
                        accept_order.setVisibility(View.GONE);
                    }
                    else if (str.equalsIgnoreCase("Rejected"))
                    {
                        cancel_order.setVisibility(View.GONE);
                        accept_order.setVisibility(View.GONE);
                    }
                } else {
                }
            }
        });

        cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                on_cancel_order();
            }
        });

        accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Map<String, Object> map = new HashMap<>();
                map.put("Status", "Accepted");
                firebaseFirestore.collection("Orders").document(order_id).update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not accepted", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void on_cancel_order() {
        Map<String, Object> map = new HashMap<>();
        map.put("Status", "Cancelled");
        if (payment_id.equals("")) {
            firebaseFirestore.collection("Orders").document(order_id).update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Not accepted", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            RazorpayClient razorpay = null;
            try {
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_x2o8sn1xPYpLuw");
                razorpay = new RazorpayClient("rzp_live_BfmrdWjSLMac8G", "oyoJ6JTEwCZLmRRZtm4qk4mj");
            } catch (RazorpayException e) {
                e.printStackTrace();
            }
            try {
                JSONObject refundRequest = new JSONObject();
                refundRequest.put("amount", Integer.parseInt(total_price) * 100); // Amount should be in paise
                Refund refund = razorpay.Payments.refund(payment_id, refundRequest);
                firebaseFirestore.collection("Orders").document(order_id).update(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Not accepted", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (RazorpayException e) {
                e.printStackTrace();
                Log.d("refunderror", e.getMessage());
                Toast.makeText(getApplicationContext(), "Not canceled", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Not canceled", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }
    }

    public void load_recycler() {

        Query query = FirebaseFirestore.getInstance().collection("Products").whereIn("product_id", product_id);

        FirestoreRecyclerOptions<cart_holder> options = new FirestoreRecyclerOptions.Builder<cart_holder>()
                .setQuery(query, cart_holder.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<cart_holder, RecyclerAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RecyclerAdapter holder, final int position, @NonNull final cart_holder model) {

                holder.prod_brand.setText(model.getBrand());
                holder.prod_name.setText(model.getName());
                holder.prod_price.setText(model.getPrice() + " Rs");
                Glide.with(getApplicationContext()).load(model.getImage()).into(holder.prod_image);
                holder.size.setText("Size " + sze.get(model.getProduct_id()));
                holder.quan.setText("Quantity " + quantity.get(model.getProduct_id()));

            }

            @NonNull
            @Override
            public RecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.card_cart_layout, parent, false);
                return new RecyclerAdapter(view);
            }
        };

        order_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public void onLayoutCompleted(RecyclerView.State state) {
                super.onLayoutCompleted(state);
                progressDialog.dismiss();
            }
        });
        order_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        order_recycler.setAdapter(adapter);
        adapter.startListening();
        progressDialog.dismiss();

    }

    public class RecyclerAdapter extends RecyclerView.ViewHolder {
        TextView prod_name, prod_brand, prod_price, quan, size;

        ImageView prod_image;

        public RecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            prod_name = itemView.findViewById(R.id.prod_name);
            prod_brand = itemView.findViewById(R.id.prod_brand);
            prod_price = itemView.findViewById(R.id.prod_price);
            prod_image = itemView.findViewById(R.id.prod_image);
            size = itemView.findViewById(R.id.size);
            quan = itemView.findViewById(R.id.quan);

        }
    }
}