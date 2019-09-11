package e.pc7.ecommerceuser.Activity.Admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import e.pc7.ecommerceuser.R;

public class AdminDashBoard extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout btn_chair, btn_sofa, btn_bed, btn_table, btn_closet;
    Button btn_check_new_orders,btn_addadvertises,btn_viewadvertises;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        getSupportActionBar().hide();
        bindviews();

        //onclick listning
        btn_chair.setOnClickListener(this);
        btn_bed.setOnClickListener(this);
        btn_closet.setOnClickListener(this);
        btn_sofa.setOnClickListener(this);
        btn_table.setOnClickListener(this);
        btn_check_new_orders.setOnClickListener(this);
        btn_addadvertises.setOnClickListener(this);
        btn_viewadvertises.setOnClickListener(this);
    }

    private void bindviews() {
        btn_chair = findViewById(R.id.btn_chair);
        btn_sofa = findViewById(R.id.btn_sofa);
        btn_bed = findViewById(R.id.btn_bed);
        btn_table = findViewById(R.id.btn_table);
        btn_closet = findViewById(R.id.btn_closet);
        btn_check_new_orders=findViewById(R.id.btn_check_new_orders);
        btn_viewadvertises=findViewById(R.id.btn_viewadvertises);
        btn_addadvertises=findViewById(R.id.btn_addadvertises);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_chair:
                Intent intent = new Intent(AdminDashBoard.this, ProductofCategory.class);
                intent.putExtra("category", "chairs");
                startActivity(intent);
                break;

            case R.id.btn_sofa:
                Intent intent1 = new Intent(AdminDashBoard.this, ProductofCategory.class);
                intent1.putExtra("category", "sofa");
                startActivity(intent1);
                break;

            case R.id.btn_bed:
                Intent intent2 = new Intent(AdminDashBoard.this, ProductofCategory.class);
                intent2.putExtra("category", "beds");
                startActivity(intent2);
                break;

            case R.id.btn_table:
                Intent intent3 = new Intent(AdminDashBoard.this, ProductofCategory.class);
                intent3.putExtra("category", "tables");
                startActivity(intent3);
                break;

            case R.id.btn_closet:
                Intent intent4 = new Intent(AdminDashBoard.this, ProductofCategory.class);
                intent4.putExtra("category", "closets");
                startActivity(intent4);
                break;

            case R.id.btn_check_new_orders:
                Intent intent5 = new Intent(AdminDashBoard.this,CheckNewOrder.class);
                startActivity(intent5);
                break;

            case R.id.btn_addadvertises:
                Intent intent6 = new Intent(AdminDashBoard.this,AddAdvertisings.class);
                startActivity(intent6);
                break;

            case R.id.btn_viewadvertises:
                Intent intent7 = new Intent(AdminDashBoard.this,ViewAdvertises.class);
                startActivity(intent7);
                break;
        }
    }
}
