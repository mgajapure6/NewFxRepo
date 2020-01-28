package app.customer;

import java.util.Date;

public class BillEntity {

	Integer id;
	Date billDate;
	String billNo;
	Integer isPayed;
	Integer customerId;

	public BillEntity(Integer id, Date billDate, String billNo, Integer isPayed, Integer customerId) {
		super();
		this.id = id;
		this.billDate = billDate;
		this.billNo = billNo;
		this.isPayed = isPayed;
		this.customerId = customerId;
	}

}
