package e.pc7.ecommerceuser.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import e.pc7.ecommerceuser.Prevenlent.Observer;
import e.pc7.ecommerceuser.R;
import e.pc7.ecommerceuser.model.UserAddress;
import e.pc7.ecommerceuser.utils.Consts;

public class ConfirmFinalorderactivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm_order;
    String no_totalamoun;
    UserAddress address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_finalorderactivity);

        //setonclick listning
        btn_confirm_order=findViewById(R.id.btn_confirm_order);
        btn_confirm_order.setOnClickListener(this);

        no_totalamoun=getIntent().getStringExtra("TOTAL_AMOUNT");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm_order:
                conformorder();
                break;
        }
    }

    private void conformorder(){

        if(Consts.locationisavailable==0)
        {
            Intent intent = new Intent(ConfirmFinalorderactivity.this,SetDeliveryLocation.class);
            intent.putExtra("EMPTY_ADDRESS",1);
            startActivity(intent);
            finish();
        }
        else{
            String no_oredrdate,no_oredrtime;

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentdate=new SimpleDateFormat("MMM:dd:yyyy");
            no_oredrdate=currentdate.format(calendar.getTime());

            SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm:ss a");
            no_oredrtime=currenttime.format(calendar.getTime());


            final DatabaseReference orderref= FirebaseDatabase.getInstance().getReference().child("New_Orders").child(Observer.CurrentOnlineUser.appusername);


            HashMap<String,Object> ordermap = new HashMap<>();
            ordermap.put("no_appuser", Observer.CurrentOnlineUser.username);
            ordermap.put("no_appusername", Observer.CurrentOnlineUser.appusername);
            ordermap.put("no_phonenumber", Observer.CurrentOnlineUser.phonenumber);
            ordermap.put("no_lat", Observer.CurrentOnlineUserAddress.getLat());
            ordermap.put("no_lng", Observer.CurrentOnlineUserAddress.getLng());
            ordermap.put("no_userarea", Observer.CurrentOnlineUserAddress.getUserarea());
            ordermap.put("no_userhouseorflat", Observer.CurrentOnlineUserAddress.getUserhouseorflat());
            ordermap.put("no_userlandmark", Observer.CurrentOnlineUserAddress.getUserlandmark());
            ordermap.put("no_userlocation", Observer.CurrentOnlineUserAddress.getUserlocation());
            ordermap.put("no_oredrdate",no_oredrdate);
            ordermap.put("no_oredrtime", no_oredrtime);
            ordermap.put("no_totalamount",no_totalamoun);
            ordermap.put("no_order_status","not shipped");


            orderref.updateChildren(ordermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(ConfirmFinalorderactivity.this, "order is placed", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });

        }


    }
}
