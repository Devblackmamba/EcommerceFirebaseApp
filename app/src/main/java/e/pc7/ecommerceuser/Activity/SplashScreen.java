package e.pc7.ecommerceuser.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.User;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener {

    TextView tv_register,tv_login;
    ProgressDialog dialog;

    SharedPreferences preferences;

    private String parentnameDB="Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        bindview();

        dialog= new ProgressDialog(this);

        tv_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    private void bindview() {

        tv_register=findViewById(R.id.tv_register);
        tv_login=findViewById(R.id.tv_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                Intent intent = new Intent(SplashScreen.this,ActivityUserLogin.class);
                startActivity(intent);
                finish();
            break;

            case R.id.tv_register:
                Intent intent1 = new Intent(SplashScreen.this,ActivityUserRegistration.class);
                startActivity(intent1);
                finish();
            break;
        }
    }


    //check credentials
    private void AllowtoaccessAccount(final String username, final String password) {

        CharSequence messgae="you are logged in please wait...";

        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final DatabaseReference rootref;
        rootref=FirebaseDatabase.getInstance().getReference();

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentnameDB).child(username).exists()){

                    User userdata = dataSnapshot.child(parentnameDB).child(username).getValue(User.class);

                    if(userdata.getUsername().equals(username)){

                        if (userdata.getPassword().equals(password)) {
                            Intent intent = new Intent(SplashScreen.this,ShoppingSreenActivity.class);
                            Observer.CurrentOnlineUser = userdata;
                            startActivity(intent);
                            dialog.dismiss();
                            finish();
                        }else{
                            dialog.dismiss();
                        }
                    }
                }
                else{
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
