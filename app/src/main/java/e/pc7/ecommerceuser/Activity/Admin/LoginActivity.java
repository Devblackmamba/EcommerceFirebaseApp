package e.pc7.ecommerceuser.Activity.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import e.pc7.ecommerceuser.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_username,et_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindviews();

        //onclick listning
        btn_login.setOnClickListener(this);
    }

    private void bindviews() {

        btn_login=findViewById(R.id.btn_login);
        et_username=findViewById(R.id.et_username);
        et_password=findViewById(R.id.et_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                loginuser();
                break;
        }
    }

    private void loginuser(){

    }
}
