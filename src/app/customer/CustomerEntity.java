package app.customer;

import java.util.List;

public class CustomerEntity {

	Integer id;
	String customerName;
	String customerAddress;
	List<AccountsEntity> accounts;
	List<BillEntity> bills;

	public CustomerEntity(Integer id, String customerName, String customerAddress,
			List<AccountsEntity> accounts, List<BillEntity> bills) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.accounts = accounts;
		this.bills = bills;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}


	public List<AccountsEntity> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountsEntity> accounts) {
		this.accounts = accounts;
	}

	public List<BillEntity> getBills() {
		return bills;
	}

	public void setBills(List<BillEntity> bills) {
		this.bills = bills;
	}

	public CustomerEntity() {
		super();
	}

}
