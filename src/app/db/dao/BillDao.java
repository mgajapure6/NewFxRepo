package app.db.dao;

import java.util.Date;
import java.util.Set;

import org.hibernate.Session;

import app.db.domain.Bill;
import app.db.domain.BillProviderProduct;
import app.db.domain.Customer;
import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;
import app.db.util.HibernateUtil;

public class BillDao {

	public Boolean saveBill(Customer customer, Set<BillProviderProduct> billProviderProductSet, Double balAmt) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		Bill bill = new Bill();
		bill.setCustomer(customer);
		
		if(balAmt==0) {
			bill.setIsPaid(true);
		}else {
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

}
