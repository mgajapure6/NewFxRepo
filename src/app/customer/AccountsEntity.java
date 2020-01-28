package app.customer;

public class AccountsEntity {
	Integer id;
	Double sum;
	Integer customerId;

	public AccountsEntity(Integer id, Double sum, Integer customerId) {
		super();
		this.id = id;
		this.sum = sum;
		this.customerId = customerId;
	}

}
