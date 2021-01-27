package stop.one.ozo_seller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import holder.banner_holder;
import holder.product_holder;
import stop.one.ozo_seller.R;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class banner_status extends AppCompatActivity {

    TextView new_request;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter adapter;
    String shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_status);
        new_request=findViewById(R.id.new_request);
        recyclerView=findViewById(R.id.recycler_view);

        new_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        shop_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query= FirebaseFirestore.getInstance().collection("Add").whereEqualTo("shop_id",shop_id);

        FirestoreRecyclerOptions<banner_holder> options = new FirestoreRecyclerOptions.Builder<banner_holder>()
                .setQuery(query, banner_holder.class).build();

        adapter=new FirestoreRecyclerAdapter<banner_holder, RecyclerAdapter>(options){

            @NonNull
            @Override
            public RecyclerAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.banner_layout, parent, false);
                return new RecyclerAdapter(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull RecyclerAdapter holder, int i, @NonNull banner_holder model) {
                Glide.with(getApplicationContext()).load(model.getImage()).centerCrop().into(holder.image);
                holder.status.setText(model.getStatus());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public class RecyclerAdapter extends RecyclerView.ViewHolder {
        TextView status;
        ImageView image;

        public RecyclerAdapter(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.status);
            image = itemView.findViewById(R.id.image);
        }
    }
}