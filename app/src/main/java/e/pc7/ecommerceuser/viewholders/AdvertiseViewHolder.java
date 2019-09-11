package e.pc7.ecommerceuser.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import e.pc7.ecommerceuser.Interface.ItemClickListner;
import e.pc7.ecommerceuser.R;

/**
 * Created by rohit on 3/4/19.
 */

public class AdvertiseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public ImageView advertiseimage;
    public ItemClickListner listner;

    public AdvertiseViewHolder(@NonNull View itemView) {
        super(itemView);

        advertiseimage=itemView.findViewById(R.id.iv_advertises);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner=listner;
    }

    @Override
    public void onClick(View v) {
        listner.onclick(v,getAdapterPosition(),false);
    }
}
