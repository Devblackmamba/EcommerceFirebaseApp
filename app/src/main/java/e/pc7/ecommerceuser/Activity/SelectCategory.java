package e.pc7.ecommerceuser.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import e.pc7.ecommerceuser.Activity.Admin.ProductofCategory;
import e.pc7.ecommerceuser.R;

public class SelectCategory extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout btn_chair,btn_sofa,btn_bed,btn_table,btn_closet;
    LinearLayout l_btn_logout;
    TextView datetime;
    String savecurrenttime,savecurrentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectcategory);
        bindviews();


        //onclick listning
        btn_chair.setOnClickListener(this);
        btn_bed.setOnClickListener(this);
        btn_closet.setOnClickListener(this);
        btn_sofa.setOnClickListener(this);
        btn_table.setOnClickListener(this);

    }

    private void bindviews() {

        btn_chair=findViewById(R.id.btn_chair);
        btn_sofa=findViewById(R.id.btn_sofa);
        btn_bed=findViewById(R.id.btn_bed);
        btn_table=findViewById(R.id.btn_table);
        btn_closet=findViewById(R.id.btn_closet);
        l_btn_logout=findViewById(R.id.l_btn_logout);
        datetime=findViewById(R.id.datetime);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chair:
                Intent intent = new Intent(SelectCategory.this,ProductofCategory.class);
                intent.putExtra("category","chairs");
                startActivity(intent);
                break;

            case R.id.btn_sofa:
                Intent intent1= new Intent(SelectCategory.this,ProductofCategory.class);
                intent1.putExtra("category","sofa");
                startActivity(intent1);
                break;

            case R.id.btn_bed:
                Intent intent2 = new Intent(SelectCategory.this,ProductofCategory.class);
                intent2.putExtra("category","beds");
                startActivity(intent2);
                break;

            case R.id.btn_table:
                Intent intent3 = new Intent(SelectCategory.this,ProductofCategory.class);
                intent3.putExtra("category","tables");
                startActivity(intent3);
                break;

            case R.id.btn_closet:
                Intent intent4 = new Intent(SelectCategory.this,ProductofCategory.class);
                intent4.putExtra("category","closets");
                startActivity(intent4);
                break;

            case R.id.l_btn_logout:
                break;
        }
    }
}
