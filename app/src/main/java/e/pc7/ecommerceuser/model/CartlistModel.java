package e.pc7.ecommerceuser.model;

public class CartlistModel{

    public CartlistModel() {

    }

    public CartlistModel(String cart_date, String cart_pdiscount, String cart_pid, String cart_pname, String cart_productprice, String cart_quantiity, String cart_time) {
        this.cart_date = cart_date;
        this.cart_pdiscount = cart_pdiscount;
        this.cart_pid = cart_pid;
        this.cart_pname = cart_pname;
        this.cart_productprice = cart_productprice;
        this.cart_quantiity = cart_quantiity;
        this.cart_time = cart_time;
    }

    String cart_date,cart_pdiscount,cart_pid,cart_pname,cart_productprice,cart_quantiity,cart_time;

    public String getCart_date() {
        return cart_date;
    }

    public String getCart_pdiscount() {
        return cart_pdiscount;
    }

    public String getCart_pid() {
        return cart_pid;
    }

    public String getCart_pname() {
        return cart_pname;
    }

    public String getCart_productprice() {
        return cart_productprice;
    }

    public String getCart_quantiity() {
        return cart_quantiity;
    }

    public String getCart_time() {
        return cart_time;
    }
}
