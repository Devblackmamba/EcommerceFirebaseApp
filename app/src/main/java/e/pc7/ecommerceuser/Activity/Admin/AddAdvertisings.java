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

public class AddAdvertisings extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_advertise_template;
    Button btn_add_advertises;
    EditText et_advertise_name;


    private String productkey,downloadimgURL,savecurrentdate,savecurrenttime,advertisename;

    private static final int GALLERY_PICK=1;
    private Uri imageuri;
    private ProgressDialog progressDialog;

    private StorageReference advertiseStorage;
    private DatabaseReference advertiseref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisings);
        bindviews();

        advertiseStorage= FirebaseStorage.getInstance().getReference().child("AdvertiseTemplate");
        advertiseref= FirebaseDatabase.getInstance().getReference().child("Advertises");

        progressDialog=new ProgressDialog(this);

        //onclick listneing
        iv_advertise_template.setOnClickListener(this);
        btn_add_advertises.setOnClickListener(this);
    }

    private void bindviews() {

        btn_add_advertises=findViewById(R.id.btn_add_advertises);
        iv_advertise_template=findViewById(R.id.iv_advertise_template);
        et_advertise_name=findViewById(R.id.et_advertise_name);
    }

    //open gallery and select image
    private void opengallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"select image template"),GALLERY_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_PICK && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageuri= data.getData();
            try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),imageuri);
                iv_advertise_template.setImageBitmap(bm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    //store image to storage
    public void storeproductdata(){
        progressDialog.setMessage("please wait while we storing");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("MMM:dd:yyyy");
        savecurrentdate=currentdate.format(calendar.getTime());

        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime=currenttime.format(calendar.getTime());

        //childnodepath
        productkey= DateFormat.getDateTimeInstance().format(new Date());

        final StorageReference filepath = advertiseStorage.child(imageuri.getLastPathSegment()+productkey);
        final UploadTask uploadTask= filepath.putFile(imageuri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errormsg=e.toString();
                Toast.makeText(AddAdvertisings.this,"error"+errormsg,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            Toast.makeText(AddAdvertisings.this,"error occurred",Toast.LENGTH_LONG).show();
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
                            saveProductDatatoServer();
                        }
                    }
                });
            }
        });
    }

    //save data to chidnode
    private void saveProductDatatoServer() {

        advertisename=et_advertise_name.getText().toString();

        HashMap<String, Object> advertisemap = new HashMap<>();
        advertisemap.put("advertise_id", productkey);
        advertisemap.put("advertise_img_URL", downloadimgURL);
        advertisemap.put("advertise_name",advertisename);


        advertiseref.child(productkey).updateChildren(advertisemap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(AddAdvertisings.this, "advertise added sucessfullly", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            String errormsg = task.getException().toString();
                            Toast.makeText(AddAdvertisings.this, "Error :" + errormsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_advertise_template:
                opengallery();
                break;

            case R.id.btn_add_advertises:
                storeproductdata();
                break;
        }
    }
}
