package e.pc7.ecommerceuser.Activity.Admin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.NewOrderModel;


public class CheckNewOrder extends AppCompatActivity {

    RecyclerView recycler_neworders;
    DatabaseReference orderRef;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_new_order);

        getSupportActionBar().hide();
        orderRef = FirebaseDatabase.getInstance().getReference().child("New_Orders");

        bindviews();

        recycler_neworders.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_neworders.setLayoutManager(layoutManager);
    }

    private void bindviews() {

        recycler_neworders=findViewById(R.id.recycler_neworders);
    }


    @Override
    protected void onStart() {
        super.onStart();

        //FETCH DATA FROM FIREBASE SERVER
        FirebaseRecyclerOptions<NewOrderModel> options= new FirebaseRecyclerOptions.Builder<NewOrderModel>()
                .setQuery(orderRef,NewOrderModel.class).build();

        FirebaseRecyclerAdapter<NewOrderModel,AdminOrderviewHolder> adapter = new FirebaseRecyclerAdapter<NewOrderModel, AdminOrderviewHolder>(options) {

            @NonNull
            @Override
            public AdminOrderviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_neworder, viewGroup, false);
                return new AdminOrderviewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull AdminOrderviewHolder holder, int position, @NonNull final NewOrderModel model) {

                holder.customername.setText(model.getNo_appusername());
                holder.customerphonenumber.setText(model.getNo_phonenumber());

                //check address
                holder.btn_viewaddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String lat = model.getNo_lat();
                        String lng = model.getNo_lng();
                        String customername=model.getNo_appuser();
                        String fulladdress = model.getNo_userlocation()+" "+model.getNo_userarea()+" "+model.getNo_userhouseorflat()+" "+model.getNo_userlandmark();

                        Intent intent = new Intent(CheckNewOrder.this,AdminViewMap.class);
                        intent.putExtra("LAT",lat);
                        intent.putExtra("LNG",lng);
                        intent.putExtra("CUSTOMER_NAME",customername);
                        intent.putExtra("FULL_ADDRESS",fulladdress);
                        startActivity(intent);
                    }
                });

                //send userkey to view products
                holder.btn_viewproduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String usercartkey=model.getNo_appuser();

                        Intent intent= new Intent(CheckNewOrder.this, UsersProductActivity.class);
                        intent.putExtra("USERCART_KEY",usercartkey);
                        startActivity(intent);

                    }
                });
            }

        };
        recycler_neworders.setAdapter(adapter);
        adapter.startListening();
    }


    public static class AdminOrderviewHolder extends RecyclerView.ViewHolder{

        public TextView customername,customerphonenumber;
        public Button btn_viewproduct,btn_viewaddress;

        public AdminOrderviewHolder(@NonNull View itemView) {
            super(itemView);

            customername=itemView.findViewById(R.id.tv_no_customername);
            customerphonenumber=itemView.findViewById(R.id.tv_no_customernumber);
            btn_viewproduct=itemView.findViewById(R.id.btn_view_ordered_product);
            btn_viewaddress=itemView.findViewById(R.id.btn_view_customer_address);
        }
    }

}
