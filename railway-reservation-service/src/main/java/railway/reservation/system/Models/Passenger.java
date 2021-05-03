package railway.reservation.system.Models;

public class Passenger {
    private Long passenger_id;
    private String passenger_name;
    private String age;
    private String contact_no;
    private String sex;
    private String address;
    private String quota;
    private String credit_card_no;
    private String bank_name;
    private String class_name;

    public Passenger() {
    }

    public Passenger(Long passenger_id, String passenger_name, String age, String contact_no, String sex, String address, String quota, String credit_card_no, String bank_name, String class_name) {
        this.passenger_id = passenger_id;
        this.passenger_name = passenger_name;
        this.age = age;
        this.contact_no = contact_no;
        this.sex = sex;
        this.address = address;
        this.quota = quota;
        this.credit_card_no = credit_card_no;
        this.bank_name = bank_name;
        this.class_name = class_name;
    }
}
