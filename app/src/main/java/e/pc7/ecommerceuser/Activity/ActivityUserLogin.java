package e.pc7.ecommerceuser.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.User;

public class ActivityUserLogin extends AppCompatActivity implements View.OnClickListener {

    EditText et_username,et_userpassword;
    Button btn_userlogin;
    private CheckBox cb_rememberme;

    String username,password;

    private String parentnameDB="Users";

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        getSupportActionBar().hide();
        bindviews();

        btn_userlogin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
    }

    private void bindviews() {
        et_username=findViewById(R.id.et_username);
        et_userpassword=findViewById(R.id.et_userpassword);
        btn_userlogin=findViewById(R.id.btn_userlogin);
        cb_rememberme=findViewById(R.id.cb_rememberme);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_userlogin:
                checkanddologin();
                break;
        }
    }

    private void checkanddologin(){

        username=et_username.getText().toString();
        password=et_userpassword.getText().toString();

        if(TextUtils.isEmpty(username)){

            Toast.makeText(ActivityUserLogin.this,"please fill username",Toast.LENGTH_SHORT).show();

        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(ActivityUserLogin.this,"please fill password",Toast.LENGTH_SHORT).show();
        }
        else{

            AllowtoaccessAccount(username,password);
        }
    }

    //check credentials
    private void AllowtoaccessAccount(final String username, final String password) {

        CharSequence messgae="verifying the user...";

        progressDialog.setMessage(messgae);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final DatabaseReference rootref;
        rootref=FirebaseDatabase.getInstance().getReference();

        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(parentnameDB).child(username).exists()){

                    User userdata = dataSnapshot.child(parentnameDB).child(username).getValue(User.class);

                    if(userdata.getUsername().equals(username)){

                        if (userdata.getPassword().equals(password)) {
                            Intent intent = new Intent(ActivityUserLogin.this,ShoppingSreenActivity.class);
                            Observer.CurrentOnlineUser = userdata;
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                        }
                    }
                }
                else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
