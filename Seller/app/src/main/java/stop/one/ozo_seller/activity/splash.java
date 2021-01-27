package stop.one.ozo_seller.activity;

import androidx.appcompat.app.AppCompatActivity;
import stop.one.ozo_seller.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splash extends AppCompatActivity {

    FirebaseAuth auth=FirebaseAuth.getInstance();
    String user_id;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int secondsDelayed = 2;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                s();
            }
        }, secondsDelayed * 2000);
    }

    public void s()
    {
        if(auth.getCurrentUser()==null)
        {
            Intent intent=new Intent(getApplicationContext(),login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
        {
            user_id=auth.getCurrentUser().getUid();
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}