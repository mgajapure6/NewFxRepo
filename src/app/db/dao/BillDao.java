package app.db.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.domain.Bill;
import app.db.domain.BillProviderProduct;
import app.db.domain.Customer;
import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;
import app.db.dto.ProductDto;
import app.db.util.HibernateUtil;

public class BillDao {

	public Boolean saveBill(Customer customer, Set<BillProviderProduct> billProviderProductSet, Double balAmt) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		Bill bill = new Bill();
		bill.setCustomer(customer);

		if (balAmt == 0) {
			bill.setIsPaid(true);
		} else {
			bill.setIsPaid(false);
		}
		bill.setBillDate(new Date());

		try {
			session.saveOrUpdate(bill);

			for (BillProviderProduct billProviderProduct : billProviderProductSet) {
				billProviderProduct.setBill(bill);
				session.saveOrUpdate(billProviderProduct);
			}
			session.getTransaction().commit();
			session.close();
			return true;
		} catch (Exception e) {
			session.getTransaction().commit();
			session.close();
			e.printStackTrace();
			return false;
		}
	}

	public List<Bill> getBillsByCustomerId(Integer customerId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		String query = "select billId, billDate," + "isPaid,sum(amount) as billAmount "
				+ "from(SELECT a.billId, a.billDate, a.isPaid, b.qtyRequested * e.price as amount "
				+ "from bill a join billproviderproduct b on (a.billId = b.billId) "
				+ "join providerproduct c on (b.providerProductId = c.providerProductId) "
				+ "join product e on (c.productId = e.productId) where a.customerId = " + customerId
				+ ")t group by billId";

		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Bill> providerProducts = getBillTypedList(sqlQuery.list());
		// System.out.println("providerProducts::" + providerProducts);
		session.getTransaction().commit();
		session.close();
		return providerProducts;
	}

	public List<ProductDto> getBillDetailById(Integer billId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		String query = "SELECT a.billId, a.billDate, c.productId, e.productName, "
				+ "b.qtyRequested as qty, e.price as rate, e.description, "
				+ "b.qtyRequested * e.price as amount from bill a join billproviderproduct b "
				+ "on (a.billId = b.billId) join providerproduct c " + "on (b.providerProductId = c.providerProductId) "
				+ "join provider d on (c.providerId = d.providerId) "
				+ "join product e on (c.productId = e.productId) where a.billId =" + billId;

		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<ProductDto> pdt = getBillProductsList(sqlQuery.list());
		session.getTransaction().commit();
		session.close();
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
	/*
	 * select billId,date_format(billDate,'%d/%m/%y') as
	 * bill_date,isPaid,sum(amount) as bill_amount from(SELECT a.billId, a.billDate,
	 * a.isPaid, b.qtyRequested * e.price as amount from bill a join
	 * billproviderproduct b on (a.billId = b.billId) join providerproduct c on
	 * (b.providerProductId = c.providerProductId) join product e on (c.productId =
	 * e.productId) where a.customerId = 1)t group by billId;
	 * 
	 * SELECT a.billId, a.billDate, c.productId, e.productName, b.qtyRequested as
	 * qty, e.price as rate, b.qtyRequested * e.price as amount from bill a join
	 * billproviderproduct b on (a.billId = b.billId) join providerproduct c on
	 * (b.providerProductId = c.providerProductId) join provider d on (c.providerId
	 * = d.providerId) join product e on (c.productId = e.productId) where a.billId
	 * = 2;
	 * 
	 * 
	 * 
	 */
}
