package stop.one.ozo_seller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import holder.select_item_holder;
import stop.one.ozo_seller.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class choose_item extends AppCompatActivity {

    public String shop_id,category,activity;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_item);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        recyclerView=findViewById(R.id.recycler_view);
        shop_id=FirebaseAuth.getInstance().getCurrentUser().getUid();

        category=getIntent().getStringExtra("category");
        activity=getIntent().getStringExtra("activity");

        Query query= FirebaseFirestore.getInstance().collection("Shop")
                .document(shop_id).collection("Items");

        FirestoreRecyclerOptions<select_item_holder> options=new FirestoreRecyclerOptions.Builder<select_item_holder>()
                .setQuery(query, select_item_holder.class).build();

        adapter= new FirestoreRecyclerAdapter<select_item_holder, recyclerAdapter>(options) {
            @NonNull
            @Override
            public recyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.select_item_card, parent, false);
                return new recyclerAdapter(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull recyclerAdapter recyclerAdapter, final int i, @NonNull final select_item_holder select_item_holder) {

                recyclerAdapter.item.setText(select_item_holder.getName());
                recyclerAdapter.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(activity.equals("show")) {
                            Intent intent = new Intent(getApplicationContext(), product_list.class);
                            intent.putExtra("item", select_item_holder.getName());
                            intent.putExtra("category", category);
                            startActivity(intent);
                        }
                        else
                        {
                            if(category.equalsIgnoreCase("Electronic")) {
                                Intent intent = new Intent(getApplicationContext(), add_electronic_product.class);
                                intent.putExtra("item", select_item_holder.getName());
                                intent.putExtra("category", category);
                                startActivity(intent);
                            }
                            else if(category.equalsIgnoreCase("Clothing")||category.equalsIgnoreCase("Footwear"))
                            {
                                Intent intent = new Intent(getApplicationContext(), add_clothing_product.class);
                                intent.putExtra("item", select_item_holder.getName());
                                intent.putExtra("category", category);
                                startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(getApplicationContext(), add_other_products.class);
                                intent.putExtra("item", select_item_holder.getName());
                                intent.putExtra("category", category);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class recyclerAdapter extends RecyclerView.ViewHolder {
        TextView item;
        public recyclerAdapter(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.item);
        }
    }
}