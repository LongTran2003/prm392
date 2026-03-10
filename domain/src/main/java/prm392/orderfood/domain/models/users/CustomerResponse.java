package prm392.orderfood.domain.models.users;

public class CustomerResponse {
    private String userId;
    private String fullName;
    private String phone;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        if (phone == null || phone.isEmpty()) {
            return "Chưa cập nhật";
        }
        return phone;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone = phoneNumber;
    }

    public String getName() {
        if (fullName == null || fullName.isEmpty()) {
            return "Unknown Customer";
        }
        return fullName;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }
}
