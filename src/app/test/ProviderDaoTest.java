package app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.Persistence;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;

import app.db.dao.ProviderDao;
import app.db.domain.Provider;
import app.db.dto.ProviderBillDto;
import app.db.services.ProviderService;
import junit.framework.Assert;

public class ProviderDaoTest {
	
	ProviderDao providerDao;
	
	@Before
	public void setup(){
		System.out.println("setup");
		providerDao = new ProviderDao(Persistence.createEntityManagerFactory("MMCStore"));
	}
	
	@Test
	public void createProvider() throws Exception {
		Boolean isSaved = providerDao.create(new Provider(1, "Alexa John", null, null));
		assertTrue("is provider saved", isSaved);
	}
	
	@Test
	public void updateProvider() throws Exception {
		Provider provider = providerDao.find(1);
	}

}
