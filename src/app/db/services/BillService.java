package app.db.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Persistence;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.dao.BillDao1;
import app.db.dao.BillProviderProductDao;
import app.db.domain.Bill;
import app.db.domain.BillProviderProduct;
import app.db.domain.Customer;
import app.db.dto.ProductDto;
import app.db.util.HibernateUtil;

public class BillService {

	private BillDao1 billDao;
	private BillProviderProductDao billProviderProductDao;

	public BillService() {
		try {
			billDao = new BillDao1(Persistence.createEntityManagerFactory("MMCStore"));
			billProviderProductDao = new BillProviderProductDao(Persistence.createEntityManagerFactory("MMCStore"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public Boolean saveBill(Customer customer, Set<BillProviderProduct> billProviderProductSet, boolean isPaid) {
		Bill bill = new Bill();
		bill.setCustomer(customer);
		bill.setBillDate(new Date());
		bill.setIsPaid(isPaid);
		try {
			billDao.create(bill);
			for (BillProviderProduct billProviderProduct : billProviderProductSet) {
				billProviderProduct.setBill(bill);
				billProviderProductDao.create(billProviderProduct);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<Bill> getBillsByCustomerId(Integer customerId) {
		String query = "select billId, billDate," + "isPaid,sum(amount) as billAmount "
				+ "from(SELECT a.billId, a.billDate, a.isPaid, b.qtyRequested * e.price as amount "
				+ "from bill a join billproviderproduct b on (a.billId = b.billId) "
				+ "join providerproduct c on (b.providerProductId = c.providerProductId) "
				+ "join product e on (c.productId = e.productId) where a.customerId = " + customerId
				+ ")t group by billId";

		List<Bill> providerProducts = null;
		try {
			providerProducts = getBillTypedList(billDao.executeQuery(query));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return providerProducts;
	}

	public List<ProductDto> getBillDetailById(Integer billId) {
		String query = "SELECT a.billId, a.billDate, c.productId, e.productName, "
				+ "b.qtyRequested as qty, e.price as rate, e.description, "
				+ "b.qtyRequested * e.price as amount from bill a join billproviderproduct b "
				+ "on (a.billId = b.billId) join providerproduct c " + "on (b.providerProductId = c.providerProductId) "
				+ "join provider d on (c.providerId = d.providerId) "
				+ "join product e on (c.productId = e.productId) where a.billId =" + billId;

		List<ProductDto> pdt = null;
		try {
			pdt = getBillProductsList(billDao.executeQuery(query));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdt;
	}

	private List<Bill> getBillTypedList(List list) {
		List<Bill> bills = new ArrayList<>();
		Gson gson = new Gson();
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			Bill b = gson.fromJson(jsonElement, Bill.class);
			bills.add(b);
		}
		return bills;
	}

	private List<ProductDto> getBillProductsList(List list) {
		List<ProductDto> pds = new ArrayList<>();
		Gson gson = new Gson();
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			ProductDto b = gson.fromJson(jsonElement, ProductDto.class);
			pds.add(b);
		}
		return pds;
	}

	public boolean updateOnlyBill(Bill selectedBill) {

		try {
			System.out.println("selectedBill::" + selectedBill);
			billDao.update(selectedBill);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
