package e.pc7.ecommerceuser.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import e.pc7.ecommerceuser.R;
import io.paperdb.Paper;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout settings_btn_logout;
    TextView tv_btn_change_profile;
    RecyclerView test_product_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        bindview();


        //onclicklistning
        tv_btn_change_profile.setOnClickListener(this);
        settings_btn_logout.setOnClickListener(this);
    }

    private void bindview() {

        settings_btn_logout=findViewById(R.id.settings_btn_logout);
        tv_btn_change_profile=findViewById(R.id.tv_btn_change_profile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settings_btn_logout:
                Paper.book().destroy();
                break;

            case R.id.tv_btn_change_profile:
                Intent intent = new Intent(SettingsActivity.this,ChangePofile.class);
                startActivity(intent);
                break;
        }
    }
}
