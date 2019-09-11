package e.pc7.ecommerceuser.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.AdvertiseModel;
import e.pc7.ecommerceuser.model.ProductsModels;
import e.pc7.ecommerceuser.model.UserAddress;
import e.pc7.ecommerceuser.utils.Consts;
import e.pc7.ecommerceuser.viewholders.AdvertiseViewHolder;
import e.pc7.ecommerceuser.viewholders.ProductviewHolder;

public class ShoppingSreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference productref,advertiseref;

    RecyclerView recyclerview_productlist,recyclerview_advertiselist;
    RecyclerView.LayoutManager layoutManager;
    TextView tv_set_delivery_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_sreen);
        bindview();

        //firebase
        productref=FirebaseDatabase.getInstance().getReference().child("Shop_products");
        productref.keepSynced(true);
        advertiseref=FirebaseDatabase.getInstance().getReference().child("Advertises");
        advertiseref.keepSynced(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //product list
        recyclerview_productlist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerview_productlist.setLayoutManager(layoutManager);

        //advertiselist
        recyclerview_advertiselist.setHasFixedSize(true);
        recyclerview_advertiselist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        View navheader = navigationView.getHeaderView(0);
        TextView navuser= navheader.findViewById(R.id.nav_currentusername);
        navuser.setText(Observer.CurrentOnlineUser.appusername+" ;");

        //set on click listning
        tv_set_delivery_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShoppingSreenActivity.this,SetDeliveryLocation.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //FETCH DATA FROM FIREBASE SERVER
        FirebaseRecyclerOptions<ProductsModels> options = new FirebaseRecyclerOptions.Builder<ProductsModels>()
                .setQuery(productref, ProductsModels.class).build();
        FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<ProductsModels, ProductviewHolder>(options) {
            @Override
            public ProductviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
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
                        Intent intent = new Intent(ShoppingSreenActivity.this,ProductDetailActivity.class);
                        intent.putExtra("SELECTED_PRODUCT",model.getPid());
                        startActivity(intent);
                    }
                });
            }
        };

        //FIREBASE ADVERVISE FETCH DATA FROM SERVER
        FirebaseRecyclerOptions<AdvertiseModel> options1 = new FirebaseRecyclerOptions.Builder<AdvertiseModel>().setQuery(advertiseref,AdvertiseModel.class).build();
        FirebaseRecyclerAdapter adapter1= new FirebaseRecyclerAdapter<AdvertiseModel, AdvertiseViewHolder>(options1) {
            @NonNull
            @Override
            public AdvertiseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_view_advertise, viewGroup, false);
                return new AdvertiseViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AdvertiseViewHolder holder, int position, @NonNull AdvertiseModel model) {
                Picasso.get().load(model.getAdvertise_img_URL()).into(holder.advertiseimage);
            }
        };
        adapter1.startListening();
        recyclerview_advertiselist.setAdapter(adapter1);
        adapter.startListening();
        recyclerview_productlist.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        final DatabaseReference rootref;
        rootref=FirebaseDatabase.getInstance().getReference();
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Users").child(Observer.CurrentOnlineUser.username).exists()){

                    UserAddress userAddress = dataSnapshot.child("Users").child(Observer.CurrentOnlineUser.username).child("address").getValue(UserAddress.class);
                    Observer.CurrentOnlineUserAddress = userAddress;
                    Consts.locationisavailable=1;
                }
                   else{
                    Toast.makeText(ShoppingSreenActivity.this,"data not found in the record",Toast.LENGTH_SHORT).show();
                  }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_sreen, menu);
        return true;
    }

    //dotted menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(ShoppingSreenActivity.this,CartActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_searchproduct) {
            Intent intent = new Intent(ShoppingSreenActivity.this,SearchProduct.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_exit) {
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void bindview(){
        recyclerview_productlist=findViewById(R.id.recyclerview_productlist);
        tv_set_delivery_location=findViewById(R.id.tv_set_delivery_location);
        recyclerview_advertiselist=findViewById(R.id.recyclerview_advertiselist);
    }
}
