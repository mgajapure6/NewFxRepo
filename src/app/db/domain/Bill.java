package app.db.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Bill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer billId;

	@Column(length = 100)
	private Date billDate;

	@Column(length = 100)
	private Boolean isPaid;

	@ManyToOne
	@JoinColumn(name = "customerId", nullable = false)
	private Customer customer;

	@OneToMany(mappedBy = "bill")
	private Set<BillProviderProduct> billProviderProducts;

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<BillProviderProduct> getBillProviderProducts() {
		return billProviderProducts;
	}

	public void setBillProviderProducts(Set<BillProviderProduct> billProviderProducts) {
		this.billProviderProducts = billProviderProducts;
	}

	public Bill(Integer billId, Date billDate, Boolean isPaid, Customer customer,
			Set<BillProviderProduct> billProviderProducts) {
		super();
		this.billId = billId;
		this.billDate = billDate;
		this.isPaid = isPaid;
		this.customer = customer;
		this.billProviderProducts = billProviderProducts;
	}

	public Bill() {
		super();
	}

}
