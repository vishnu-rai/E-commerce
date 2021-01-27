package stop.one.ozo_seller.activity;

import androidx.appcompat.app.AppCompatActivity;
import stop.one.ozo_seller.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView add_new_product,order,products,banner;
    String category,shop_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        products=findViewById(R.id.products);
        order=findViewById(R.id.order);
//        banner=findViewById(R.id.banner);
        add_new_product=findViewById(R.id.add_new_product);

        shop_id= FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Shop").document(shop_id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        category=documentSnapshot.getString("category");
                    }
                });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,order_list.class));
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, choose_item.class);
                intent.putExtra("activity","show");
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

        add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, choose_item.class);
                intent.putExtra("activity","add");
                intent.putExtra("category",category);
                startActivity(intent);
            }
        });

//        banner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),banner_status.class));
//            }
//        });

    }
}