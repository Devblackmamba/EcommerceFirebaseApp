package e.pc7.ecommerceuser.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import e.pc7.ecommerceuser.Interface.ItemClickListner;
import e.pc7.ecommerceuser.R;

/**
 * Created by rohit on 31/3/19.
 */

public class NewOrderviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView tv_no_customername,tv_no_customernumber;
    Button btn_view_ordered_product,btn_view_customer_address;
    public ItemClickListner listner;

    public NewOrderviewHolder(@NonNull View itemView) {

            super(itemView);
            btn_view_ordered_product=itemView.findViewById(R.id.btn_view_ordered_product);
            tv_no_customername=itemView.findViewById(R.id.tv_no_customername );
            tv_no_customernumber=itemView.findViewById(R.id.tv_no_customernumber);
            btn_view_customer_address=itemView.findViewById(R.id.btn_view_customer_address);
        }

    public void setItemClickListner(ItemClickListner listner){
        this.listner=listner;
    }

    @Override
    public void onClick(View v) {
        listner.onclick(v,getAdapterPosition(),false);
    }

}
