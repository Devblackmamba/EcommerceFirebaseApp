package e.pc7.ecommerceuser.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import e.pc7.ecommerceuser.R;

public class ActivityUserRegistration extends AppCompatActivity implements View.OnClickListener {

    Button btn_signup;
    EditText et_newuserphonenumber,et_newuserpassword,et_newusername,et_newpersonname;
    String name,username,password,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        getSupportActionBar().hide();
        bindview();
        //setonclick listning
        btn_signup.setOnClickListener(this);
    }

    private void bindview() {
        btn_signup=findViewById(R.id.btn_signup);
        et_newuserphonenumber=findViewById(R.id.et_newuserphonenumber);
        et_newuserpassword=findViewById(R.id.et_newuserpassword);
        et_newusername=findViewById(R.id.et_newusername);
        et_newpersonname=findViewById(R.id.et_newpersonname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:
                createnewuseraccount();
                break;
        }
    }

    private void createnewuseraccount() {
        name=et_newusername.getText().toString();
        username=et_newusername.getText().toString();
        password=et_newuserpassword.getText().toString();
        phonenumber=et_newuserphonenumber.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(ActivityUserRegistration.this,"fill all the detail",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(username)){
            Toast.makeText(ActivityUserRegistration.this,"fill all the detail",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(ActivityUserRegistration.this,"fill all the detail",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText(ActivityUserRegistration.this,"fill all the detail",Toast.LENGTH_SHORT).show();
        }
        else{
            validateusercredentials(name,username,password,phonenumber);
        }
    }

    private void validateusercredentials(final String name, final String username, final String password, final String phonenumber) {
        final DatabaseReference rootref;
        rootref=FirebaseDatabase.getInstance().getReference();
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("users").child(username).exists())){
                    HashMap<String,Object> usermap = new HashMap<>();
                    usermap.put("phonenumber",phonenumber);
                    usermap.put("username",username);
                    usermap.put("appusername",name);
                    usermap.put("password",password);
                    rootref.child("Users").child(username).updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(ActivityUserRegistration.this,ActivityUserLogin.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(ActivityUserRegistration.this,"network error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(ActivityUserRegistration.this,"this user is already exists choose any different username",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
