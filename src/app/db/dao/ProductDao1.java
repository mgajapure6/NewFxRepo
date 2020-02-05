package app.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import app.db.domain.Product;

public class ProductDao1 extends BasicDAO<Product> {

	private EntityManagerFactory factory;

	public ProductDao1(EntityManagerFactory factory) {
		super(Product.class);
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
