package e.pc7.ecommerceuser.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import e.pc7.ecommerceuser.Interface.ItemClickListner;
import e.pc7.ecommerceuser.R;

/**
 * Created by rohit on 2/4/19.
 */

public class Review_Viewholder extends RecyclerView.ViewHolder {


    public TextView row_reviwername,row_review,review_date;
    public AppCompatRatingBar row_ratingbar;

    public ItemClickListner listner;

    public Review_Viewholder(@NonNull View itemView) {
        super(itemView);

        row_ratingbar=itemView.findViewById(R.id.row_rating);
        row_reviwername=itemView.findViewById(R.id.row_reviwername);
        row_review=itemView.findViewById(R.id.row_comment);
        review_date=itemView.findViewById(R.id.review_date);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner=listner;
    }
}
