package e.pc7.ecommerceuser.model;

/**
 * Created by rohit on 3/4/19.
 */

public class AdvertiseModel {

    String advertise_id,advertise_img_URL,advertise_name;

    public AdvertiseModel() {
    }

    public AdvertiseModel(String advertise_id, String advertise_img_URL, String advertise_name) {
        this.advertise_id = advertise_id;
        this.advertise_img_URL = advertise_img_URL;
        this.advertise_name = advertise_name;
    }

    public String getAdvertise_id() {
        return advertise_id;
    }

    public void setAdvertise_id(String advertise_id) {
        this.advertise_id = advertise_id;
    }

    public String getAdvertise_img_URL() {
        return advertise_img_URL;
    }

    public void setAdvertise_img_URL(String advertise_img_URL) {
        this.advertise_img_URL = advertise_img_URL;
    }

    public String getAdvertise_name() {
        return advertise_name;
    }

    public void setAdvertise_name(String advertise_name) {
        this.advertise_name = advertise_name;
    }
}
