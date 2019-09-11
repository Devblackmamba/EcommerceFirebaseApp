package e.pc7.ecommerceuser.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import e.pc7.ecommerceuser.Interface.ItemClickListner;
import e.pc7.ecommerceuser.R;

public class ProductviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView iv_productimage;
    public TextView tv_productname,tv_productprice,tv_productdiscount;

    public ItemClickListner listner;

    public ProductviewHolder(@NonNull View itemView) {
        super(itemView);

        iv_productimage=itemView.findViewById(R.id.iv_productimage);
        tv_productname=itemView.findViewById(R.id.tv_productname);
        tv_productprice=itemView.findViewById(R.id.tv_productprice);
        tv_productdiscount=itemView.findViewById(R.id.tv_productdiscount);
    }

    public void setItemClickListner(ItemClickListner listner){
       this.listner=listner;
    }

    @Override
    public void onClick(View v) {
      listner.onclick(v,getAdapterPosition(),false);
    }
}
