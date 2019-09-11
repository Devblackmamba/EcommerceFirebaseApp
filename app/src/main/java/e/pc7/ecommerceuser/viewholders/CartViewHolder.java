package e.pc7.ecommerceuser.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import e.pc7.ecommerceuser.Interface.ItemClickListner;
import e.pc7.ecommerceuser.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView cart_added_product_name,cart_added_product_price,cart_added_product_quantitiy;

    public ItemClickListner listner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cart_added_product_quantitiy=itemView.findViewById(R.id.cart_added_product_quantitiy);
        cart_added_product_name=itemView.findViewById(R.id.cart_added_product_name);
        cart_added_product_price=itemView.findViewById(R.id.cart_added_product_price);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner=listner;
    }

    @Override
    public void onClick(View v) {
        listner.onclick(v,getAdapterPosition(),false);
    }
}
