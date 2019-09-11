package e.pc7.ecommerceuser.model;

public class User {

    public String appusername,password,phonenumber,username;

    public User(String appusername, String password, String phonenumber, String username) {
        this.appusername = appusername;
        this.password = password;
        this.phonenumber = phonenumber;
        this.username = username;
    }

    public String getAppusername() {
        return appusername;
    }

    public void setAppusername(String appusername) {
        this.appusername = appusername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }
}
