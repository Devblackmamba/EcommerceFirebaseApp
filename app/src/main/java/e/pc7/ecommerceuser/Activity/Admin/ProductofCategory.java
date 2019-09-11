package e.pc7.ecommerceuser.Activity.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import e.pc7.ecommerceuser.R;

public class ProductofCategory extends AppCompatActivity {

    private String categoryname,productdiscription,productname,productprice,productdiscount,savecurrentdate,savecurrenttime,
            productkey,downloadimgURL,searchkey;

    private ImageView iv_product_image;
    private EditText et_product_name,et_product_prize,et_product_discription,et_product_discount,et_product_searchkey;
    private Button btn_add_product;

    private static final int GALLERY_PICK=1;
    private Uri imageuri;
    private ProgressDialog progressDialog;

    private StorageReference productimageref;
    private DatabaseReference  productref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productof_category);
        bindviews();

        categoryname = getIntent().getStringExtra("category");

        productimageref=FirebaseStorage.getInstance().getReference().child("productimage");
        productref=FirebaseDatabase.getInstance().getReference().child("Shop_products");

        //Productuploading progressing
        progressDialog = new ProgressDialog(this);

        btn_add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateproductdata();
            }
        });
        iv_product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });

    }

    private void bindviews() {

        iv_product_image=findViewById(R.id.iv_product_image);
        et_product_name=findViewById(R.id.et_product_name);
        et_product_prize=findViewById(R.id.et_product_prize);
        et_product_discription=findViewById(R.id.et_product_discription);
        et_product_discount=findViewById(R.id.et_product_discount);
        btn_add_product=findViewById(R.id.btn_add_product);
        et_product_searchkey=findViewById(R.id.et_product_searchkey);

    }


    //open gallery and select image
    private void opengallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"select shop image"),GALLERY_PICK);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri= data.getData();
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                iv_product_image.setImageBitmap(bm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //store the data to server
    public void storeproductdata(){

        progressDialog.setTitle("adding the product");
        progressDialog.setMessage("please wait while we are adding");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM:dd:yyyy");
        savecurrentdate=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calendar.getTime());



        //childnodepath
        productkey=DateFormat.getDateTimeInstance().format(new Date())+","+categoryname;

        final StorageReference filepath = productimageref.child(imageuri.getLastPathSegment()+productkey);
        final UploadTask uploadTask= filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errormsg=e.toString();
                Toast.makeText(ProductofCategory.this,"error"+errormsg,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            Toast.makeText(ProductofCategory.this,"error occurred",Toast.LENGTH_LONG).show();
                            throw task.getException();

                        }
                        downloadimgURL=filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            downloadimgURL=task.getResult().toString();
                            Log.e("fireimage","-->"+downloadimgURL);
                            Toast.makeText(ProductofCategory.this,"product set to the database",Toast.LENGTH_LONG).show();
                                saveProductDatatoServer();
                        }
                    }
                });
            }
        });
    }

    private void saveProductDatatoServer() {
        HashMap<String,Object> productmap = new HashMap<>();
        productmap.put("pid",productkey);
        productmap.put("pimage",downloadimgURL);
        productmap.put("pname",productname);
        productmap.put("pcategory",categoryname);
        productmap.put("pprice",productprice);
        productmap.put("pdiscount",productdiscount);
        productmap.put("pdiscription",productdiscription);
        productmap.put("pcategory",categoryname);
        productmap.put("pdate",savecurrentdate);
        productmap.put("ptime",savecurrenttime);
        productmap.put("psearchkey",searchkey);

        productref.child(productkey).updateChildren(productmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(ProductofCategory.this,"product added sucessfullly",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                        else {
                            progressDialog.dismiss();
                            String errormsg =task.getException().toString();
                            Toast.makeText(ProductofCategory.this,"Error :"+errormsg,Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    //validate product data
    private void validateproductdata(){

        productname=et_product_name.getText().toString();
        productdiscount=et_product_discount.getText().toString();
        productprice=et_product_prize.getText().toString();
        productdiscription=et_product_discription.getText().toString();
        searchkey=et_product_searchkey.getText().toString();

        if(TextUtils.isEmpty(productname)){
            Toast.makeText(ProductofCategory.this,"fill all the details",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(productdiscount)){
            Toast.makeText(ProductofCategory.this,"fill all the details",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(productprice)){
            Toast.makeText(ProductofCategory.this,"fill all the details",Toast.LENGTH_LONG).show();

        }else if(TextUtils.isEmpty(productdiscription)){
            Toast.makeText(ProductofCategory.this,"fill all the details",Toast.LENGTH_LONG).show();
        }
        else{
           storeproductdata();
        }
    }
}
