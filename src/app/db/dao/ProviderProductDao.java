package app.db.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import app.db.domain.ProviderProduct;

public class ProviderProductDao extends BasicDAO<ProviderProduct> {

	private EntityManagerFactory factory;

	public ProviderProductDao(EntityManagerFactory factory) {
		super(ProviderProduct.class);
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
