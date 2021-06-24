package bank.mangement.service.model.bankForm;

public class BankForm {
    private String AccountHolder;
    private Long user_id;
    private String contact_no;
    private String bank_name;
    private String account_type;
	public String getAccountHolder() {
		return AccountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		AccountHolder = accountHolder;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getContact_no() {
		return contact_no;
	}
	public void setContact_no(String contact_no) {
		this.contact_no = contact_no;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
   
}