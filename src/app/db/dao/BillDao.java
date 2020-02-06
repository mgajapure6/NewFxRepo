package app.db.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import app.db.domain.Bill;
import app.db.dto.ProductDto;

public class BillDao extends BasicDAO<Bill> {

	private EntityManagerFactory factory;

	public BillDao(EntityManagerFactory factory) {
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
