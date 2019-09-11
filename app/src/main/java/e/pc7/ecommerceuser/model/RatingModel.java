package e.pc7.ecommerceuser.model;

/**
 * Created by rohit on 2/4/19.
 */

public class RatingModel {

    String rating,ratingusername,reviewdate,usercomment;

    public RatingModel(String rating, String ratingusername, String reviewdate, String usercomment) {
        this.rating = rating;
        this.ratingusername = ratingusername;
        this.reviewdate = reviewdate;
        this.usercomment = usercomment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingusername() {
        return ratingusername;
    }

    public void setRatingusername(String ratingusername) {
        this.ratingusername = ratingusername;
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }

    public String getUsercomment() {
        return usercomment;
    }

    public void setUsercomment(String usercomment) {
        this.usercomment = usercomment;
    }

    public RatingModel() {
    }
}
