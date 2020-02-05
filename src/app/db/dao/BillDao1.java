package app.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import app.db.domain.Bill;

public class BillDao1 extends BasicDAO<Bill> {

	private EntityManagerFactory factory;

	public BillDao1(EntityManagerFactory factory) {
		super(Bill.class);
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
