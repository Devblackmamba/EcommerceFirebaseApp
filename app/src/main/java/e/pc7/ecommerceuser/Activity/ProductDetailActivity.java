package e.pc7.ecommerceuser.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.ProductsModels;
import e.pc7.ecommerceuser.model.RatingModel;
import e.pc7.ecommerceuser.viewholders.Review_Viewholder;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ElegantNumberButton elegantnumberbutton;
    Button add_viewedproduct;
    ImageView product_detailimage;
    TextView productdetail_discription,productdetail_discount,productdetail_price,productdetail_name,tv_add_review_btn;
    RecyclerView recycler_reviews;
    private String productID;
    LinearLayoutManager layoutManager;
    String comment,checkstars,savecurrentdate;
    DatabaseReference reviewref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        bindview();
        productID=getIntent().getStringExtra("SELECTED_PRODUCT");
        getProductDetail(productID);

        //setup reviews
        recycler_reviews.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recycler_reviews.setLayoutManager(layoutManager);

        //onclick listning
        add_viewedproduct.setOnClickListener(this);
        tv_add_review_btn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //FETCH DATA FROM FIREBASE SERVER
        reviewref = FirebaseDatabase.getInstance().getReference().child("Shop_products").child(productID);
        FirebaseRecyclerOptions<RatingModel> options = new FirebaseRecyclerOptions.Builder<RatingModel>().setQuery(reviewref.child("Product_Reviews"), RatingModel.class).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<RatingModel, Review_Viewholder>(options) {
            @Override
            public Review_Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_show_reviews, parent, false);
                return new Review_Viewholder(view);
            }
            @Override
            protected void onBindViewHolder(@NonNull Review_Viewholder holder, int position, @NonNull RatingModel model) {
                holder.row_reviwername.setText(model.getRatingusername());
                holder.row_review.setText(model.getUsercomment());
                holder.review_date.setText(model.getReviewdate());
                holder.row_ratingbar.setRating(Float.valueOf(model.getRating()));
                Toast.makeText(ProductDetailActivity.this, ""+model.getRating(), Toast.LENGTH_SHORT).show();
            }
        };
        adapter.startListening();
        recycler_reviews.setAdapter(adapter);
    }

    //get product details from product id
    private void getProductDetail(String productID) {
        DatabaseReference productref=FirebaseDatabase.getInstance().getReference().child("Shop_products");
        productref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ProductsModels models = dataSnapshot.getValue(ProductsModels.class);
                    productdetail_name.setText(models.getPname());
                    productdetail_discription.setText(models.getPdiscription());
                    productdetail_price.setText(models.getPprice());
                    productdetail_discount.setText(models.getPdiscount()+"% off on this product");
                    Picasso.get().load(models.getPimage()).into(product_detailimage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    //ADD TO FIREBASE CARTLIST
    private void addToCartList(){
        Toast.makeText(this, ""+Observer.CurrentOnlineUser.appusername, Toast.LENGTH_SHORT).show();
        if(Integer.valueOf(elegantnumberbutton.getNumber())>0)
        {
            String savecurrentdate,savecurrenttime;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentdate=new SimpleDateFormat("MMM:dd:yyyy");
            savecurrentdate=currentdate.format(calendar.getTime());
            SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
            savecurrenttime=currenttime.format(calendar.getTime());
            DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Cart_List").child("UsersCart");
            final HashMap<String,Object> cartmap= new HashMap<>();
            cartmap.put("cart_pid",productID);
            cartmap.put("cart_pname",productdetail_name.getText().toString());
            cartmap.put("cart_productprice",productdetail_price.getText().toString());
            cartmap.put("cart_quantiity",elegantnumberbutton.getNumber());
            cartmap.put("cart_date",savecurrentdate);
            cartmap.put("cart_time",savecurrenttime);
            cartmap.put("cart_pdiscount",productdetail_discount.getText().toString());
            productref.child(Observer.CurrentOnlineUser.appusername).child("Products").child(productID).updateChildren(cartmap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ProductDetailActivity.this, "product added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "please add atleat one product", Toast.LENGTH_SHORT).show();
        }
    }
    private void bindview() {
        productdetail_price=findViewById(R.id.productdetail_price);
        productdetail_discount=findViewById(R.id.productdetail_discount);
        productdetail_discription=findViewById(R.id.productdetail_discription);
        product_detailimage=findViewById(R.id.product_detailimage);
        elegantnumberbutton=findViewById(R.id.elegantnumberbutton);
        productdetail_name=findViewById(R.id.productdetail_name);
        add_viewedproduct=findViewById(R.id.add_viewedproduct);
        tv_add_review_btn=findViewById(R.id.tv_add_review_btn);
        recycler_reviews=findViewById(R.id.recycler_reviews);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_viewedproduct:
                addToCartList();
                break;

            case R.id.tv_add_review_btn:
                //open giving reviewsheet
                opengivingReview();
                break;
        }
    }

    //open giving reviews sheet
    private void opengivingReview(){

        Button btn_bs_addreview;
        final EditText et_bs_writereview;
        final RatingBar ratingbar_product;

        final BottomSheetDialog commentSheet = new BottomSheetDialog(ProductDetailActivity.this);
        commentSheet.setContentView(R.layout.bottom_sheet_giverating);

        et_bs_writereview=commentSheet.findViewById(R.id.et_bs_writereview);
        btn_bs_addreview=commentSheet.findViewById(R.id.btn_bs_addreview);
        ratingbar_product=commentSheet.findViewById(R.id.ratingbar_product);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM:dd:yyyy");
        savecurrentdate=currentdate.format(calendar.getTime());


        btn_bs_addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = et_bs_writereview.getText().toString();
                checkstars=String.valueOf(ratingbar_product.getRating());
                DatabaseReference productref = FirebaseDatabase.getInstance().getReference().child("Shop_products").child(productID);
                HashMap<String, Object> addressmap = new HashMap<>();
                addressmap.put("ratingusername", Observer.CurrentOnlineUser.appusername);
                addressmap.put("usercomment", comment);
                addressmap.put("rating",checkstars );
                addressmap.put("reviewdate",savecurrentdate);
                Toast.makeText(ProductDetailActivity.this, ""+comment, Toast.LENGTH_SHORT).show();

                productref.child("Product_Reviews").child(Observer.CurrentOnlineUser.username).updateChildren(addressmap).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                commentSheet.dismiss();
                            }
                        });
                }
        });
        commentSheet.show();
    }
}
