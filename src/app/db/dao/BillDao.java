package app.db.dao;

import org.hibernate.Session;

import app.db.domain.Bill;
import app.db.domain.Customer;
import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;
import app.db.util.HibernateUtil;

public class BillDao {

	public Boolean saveBill(Customer customer, Bill bill) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		
		return true;

	}

}
