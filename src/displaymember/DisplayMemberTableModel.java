
package displaymember;

public class DisplayMemberTableModel {
    String name;
    String id;
    String mobile;
    String email;

    public DisplayMemberTableModel(String name, String id, String mobile, String email) {
        this.name = name;
        this.id = id;
        this.mobile = mobile;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
