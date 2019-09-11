package e.pc7.ecommerceuser.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.ProductsModels;
import e.pc7.ecommerceuser.viewholders.ProductviewHolder;

public class SearchProduct extends AppCompatActivity {

    RecyclerView searchedlist;
    Button btn_searchproduct;
    EditText et_search;
    LinearLayoutManager layoutManager;

    String searchedInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        bindviews();

        //product list
        searchedlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        searchedlist.setLayoutManager(layoutManager);


        btn_searchproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(et_search.getText().toString()))
                {
                    Toast.makeText(SearchProduct.this, "please write some keywords ", Toast.LENGTH_SHORT).show();
                }else
                {
                    searchedInput = et_search.getText().toString();
                    searchProduct();
                }
            }
        });
    }

    private void searchProduct() {
        //PREPARE FIREBASE SERVER FOR SEARCHING
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Shop_products");
        FirebaseRecyclerOptions<ProductsModels> options = new FirebaseRecyclerOptions.Builder<ProductsModels>().setQuery(reference.orderByChild("psearchkey").startAt(searchedInput),ProductsModels.class).build();
        FirebaseRecyclerAdapter<ProductsModels,ProductviewHolder> adapter = new FirebaseRecyclerAdapter<ProductsModels, ProductviewHolder>(options) {
            @NonNull
            @Override
            public ProductviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_product, viewGroup, false);
                return new ProductviewHolder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull ProductviewHolder holder, int position, @NonNull final ProductsModels model) {
                holder.tv_productname.setText(model.getPname());
                holder.tv_productprice.setText(model.getPprice());
                holder.tv_productdiscount.setText(model.getPdiscount());
                Picasso.get().load(model.getPimage()).into(holder.iv_productimage);

                //selected product
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(SearchProduct.this,ProductDetailActivity.class);
                        intent.putExtra("SELECTED_PRODUCT",model.getPid());
                        startActivity(intent);
                    }
                });

            }
        };
        searchedlist.setAdapter(adapter);
        adapter.startListening();
    }


    private void bindviews() {
        et_search=findViewById(R.id.et_search);
        btn_searchproduct=findViewById(R.id.btn_searchproduct);
        searchedlist=findViewById(R.id.searched_recyclerview);
    }
}
