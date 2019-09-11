package e.pc7.ecommerceuser.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.CartlistModel;
import e.pc7.ecommerceuser.viewholders.CartViewHolder;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_cartlist;
    Button btn_proceed_order;
    LinearLayoutManager layoutManager;

    TextView tv_carttotalamount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        bindView();

        recycler_cartlist.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_cartlist.setLayoutManager(layoutManager);

        //setonclick listning
        btn_proceed_order.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //FETCH DATA FROM FIREBASE SERVER
        final DatabaseReference CartRef = FirebaseDatabase.getInstance().getReference().child("Cart_List");
        FirebaseRecyclerOptions<CartlistModel> options = new FirebaseRecyclerOptions.Builder<CartlistModel>()
                .setQuery(CartRef.child("UsersCart")
                        .child(Observer.CurrentOnlineUser.getUsername())
                        .child("Products"),CartlistModel.class)
                        .build();

        FirebaseRecyclerAdapter<CartlistModel,CartViewHolder> adapter= new FirebaseRecyclerAdapter<CartlistModel, CartViewHolder>(options) {

            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cartlist, viewGroup, false);
                return new CartViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final CartlistModel model) {
                holder.cart_added_product_name.setText(model.getCart_pname());
                holder.cart_added_product_price.setText(model.getCart_productprice());
                holder.cart_added_product_quantitiy.setText(model.getCart_quantiity());

                Toast.makeText(CartActivity.this, "", Toast.LENGTH_SHORT).show();

                long oneProductprice= (long) ((Double.valueOf(model.getCart_productprice()))*(Double.valueOf(model.getCart_quantiity())));
                tv_carttotalamount.setText(String.valueOf(oneProductprice));

                //perticular cart item
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CharSequence options[] = new CharSequence[]{
                                "remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("cart oprions");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(which==0){
                                    CartRef.child("UsersCart")
                                            .child(Observer.CurrentOnlineUser.username)
                                            .child("Products")
                                            .child(model.getCart_pid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(CartActivity.this,"item removed",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }
                            }
                        });

                        builder.show();
                        return true;
                    }
                });
            }

        };

        adapter.startListening();
        recycler_cartlist.setAdapter(adapter);
    }

    private void bindView() {
        btn_proceed_order=findViewById(R.id.btn_proceed_order);
        recycler_cartlist=findViewById(R.id.recyclercart);
        tv_carttotalamount=findViewById(R.id.tv_carttotalamount);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_proceed_order:
                confirmOderByUser();

                break;
        }
    }

    private void confirmOderByUser(){
        Intent intent= new Intent(CartActivity.this,ConfirmFinalorderactivity.class);

        if(TextUtils.isEmpty(tv_carttotalamount.getText().toString()))
        {

        }else{
            intent.putExtra("TOTAL_AMOUNT",tv_carttotalamount.getText().toString());
            startActivity(intent);
            finish();
        }
    }

}
