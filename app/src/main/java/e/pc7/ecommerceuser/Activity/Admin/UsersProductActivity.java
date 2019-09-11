package e.pc7.ecommerceuser.Activity.Admin;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.CartlistModel;
import e.pc7.ecommerceuser.viewholders.CartViewHolder;

public class UsersProductActivity extends AppCompatActivity {

    RecyclerView recycler_usersproduct;
    private DatabaseReference carrproductref;
    private String Uid="";
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_product);
        bindviews();

        recycler_usersproduct.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_usersproduct.setLayoutManager(layoutManager);

        Uid=getIntent().getStringExtra("USERCART_KEY");

        Toast.makeText(this, ""+Uid, Toast.LENGTH_SHORT).show();


            }

    @Override
    protected void onStart() {
        super.onStart();

//        final DatabaseReference carrproductref = FirebaseDatabase.getInstance().getReference().child("Cart_List");
//        FirebaseRecyclerOptions<CartlistModel> options = new FirebaseRecyclerOptions.Builder<CartlistModel>()
//                .setQuery(carrproductref.child("UsersCart")).build();

        final DatabaseReference CartRef = FirebaseDatabase.getInstance().getReference().child("Cart_List");
        FirebaseRecyclerOptions<CartlistModel> options = new FirebaseRecyclerOptions.Builder<CartlistModel>()
                .setQuery(CartRef.child("UsersCart")
                        .child(Uid)
                        .child("Products"),CartlistModel.class)
                        .build();

        FirebaseRecyclerAdapter<CartlistModel,CartViewHolder> adapter = new FirebaseRecyclerAdapter<CartlistModel, CartViewHolder>(options) {
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cartlist, viewGroup, false);
                return new CartViewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull CartlistModel model) {

                holder.cart_added_product_name.setText(model.getCart_pname());
                holder.cart_added_product_price.setText(model.getCart_productprice());
                holder.cart_added_product_quantitiy.setText(model.getCart_quantiity());
            }
        };

        adapter.startListening();
        recycler_usersproduct.setAdapter(adapter);
    }

    private void bindviews() {

        recycler_usersproduct=findViewById(R.id.recycler_usersproduct);
    }
}
