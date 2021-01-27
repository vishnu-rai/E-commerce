
package stop.one.ozo_seller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import stop.one.ozo_seller.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class login extends AppCompatActivity {

    private static final String TAG = "tag";
    EditText email, password;
    TextView login;

    String em, ps;

    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pd=new ProgressDialog(this);
        pd.setMessage("Loading");

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent = new Intent(login.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        mAuth = FirebaseAuth.getInstance();





        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                em = email.getText().toString();
                ps = password.getText().toString();

                if (!em.isEmpty() && !ps.isEmpty()) {
                    pd.show();
                    mAuth.signInWithEmailAndPassword(em, ps)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                                        String token= FirebaseInstanceId.getInstance().getToken();
                                        HashMap<String,Object> map=new HashMap<>();
                                        map.put("token",token);
                                        firebaseFirestore.collection("Shop")
                                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map);
                                        pd.dismiss();
                                        Intent intent = new Intent(login.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else
                    Toast.makeText(getApplicationContext(), "Empty field", Toast.LENGTH_LONG).show();
            }
        });

    }
}