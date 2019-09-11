package e.pc7.ecommerceuser.model;

public class UserAddress {

    String lat,lng,userarea,userhouseorflat,userlandmark,userlocation;

    public UserAddress() {

    }

    public UserAddress(String lat, String lng, String userarea, String userhouseorflat, String userlandmark, String userlocation) {
        this.lat = lat;
        this.lng = lng;
        this.userarea = userarea;
        this.userhouseorflat = userhouseorflat;
        this.userlandmark = userlandmark;
        this.userlocation = userlocation;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserarea() {
        return userarea;
    }

    public void setUserarea(String userarea) {
        this.userarea = userarea;
    }

    public String getUserhouseorflat() {
        return userhouseorflat;
    }

    public void setUserhouseorflat(String userhouseorflat) {
        this.userhouseorflat = userhouseorflat;
    }

    public String getUserlandmark() {
        return userlandmark;
    }

    public void setUserlandmark(String userlandmark) {
        this.userlandmark = userlandmark;
    }

    public String getUserlocation() {
        return userlocation;
    }

    public void setUserlocation(String userlocation) {
        this.userlocation = userlocation;
    }
}
