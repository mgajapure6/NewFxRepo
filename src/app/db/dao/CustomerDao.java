package app.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import app.db.domain.Customer;

public class CustomerDao extends BasicDAO<Customer> {

	private EntityManagerFactory factory;

	public CustomerDao(EntityManagerFactory factory) {
		super(Customer.class);
		this.factory = factory;
	}

	@Override
	public EntityManager getEntityManager() {
		try {
			return factory.createEntityManager();
		} catch (Exception ex) {
			System.out.println("The entity manager cannot be created!");
			return null;

		}
	}

}
