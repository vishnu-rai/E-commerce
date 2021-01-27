package stop.one.ozo_seller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import holder.product_frag_order_list_holder;
import stop.one.ozo_seller.R;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class order_list extends AppCompatActivity {

    RecyclerView order_recycler;
    FirestoreRecyclerAdapter adapter;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    String shop_id;
    String order_id;


    @Override
    public void onBackPressed() {
        if(isTaskRoot()) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        order_recycler = findViewById(R.id.order_recycler);

        shop_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = rootRef.collection("Orders").whereEqualTo("Shop_id", shop_id).orderBy("Number", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<product_frag_order_list_holder> options = new FirestoreRecyclerOptions.Builder<product_frag_order_list_holder>()
                .setQuery(query, product_frag_order_list_holder.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<product_frag_order_list_holder, RecyclerAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RecyclerAdapter holder, final int position, @NonNull final product_frag_order_list_holder model) {

                holder.order_date.setText("Order date: "+model.getDate());
                holder.order_id.setText(getSnapshots().getSnapshot(position).getId());
                holder.price.setText("Price : " + model.getTotal_price());
                holder.status.setText(model.getStatus());
                if(model.getStatus()=="Pending")
                    holder.product_image.setBackgroundColor(Color.BLACK);

                holder.product_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        order_id= getSnapshots().getSnapshot(position).getId();
                        FirebaseFirestore.getInstance().collection("Orders").document(order_id).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        List<String> pro=new ArrayList<>();
                                        List<String> quan=new ArrayList<>();
                                        List<String> size=new ArrayList<>();
                                        pro= (List<String>) documentSnapshot.get("Product_id");
                                        quan= (List<String>) documentSnapshot.get("Quan");
                                        size= (List<String>) documentSnapshot.get("Size");
                                        Intent intent = new Intent(getApplicationContext(), order_detail.class);
                                        intent.putExtra("Order_id", order_id);
                                        intent.putExtra("Product_id", (Serializable) pro);
                                        intent.putExtra("Quan", (Serializable) quan);
                                        intent.putExtra("Size", (Serializable) size);
                                        intent.putExtra("Address_id",model.getAddress_id());
                                        intent.putExtra("User_id",model.getUser_id());
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to load",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
            @NonNull
            @Override
            public RecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.frag_product_order_list_card, parent, false);
                return new RecyclerAdapter(view);
            }
        };

        order_recycler.setLayoutManager(new LinearLayoutManager(this));
        order_recycler.setAdapter(adapter);
        adapter.startListening();
    }

    public class RecyclerAdapter extends RecyclerView.ViewHolder {
        TextView order_id, order_date, price, status;
        LinearLayout product_layout;
        ImageView product_image;


        public RecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            order_id = itemView.findViewById(R.id.order_id);
            order_date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.status);
            product_layout = itemView.findViewById(R.id.product_layout);

        }
    }
}