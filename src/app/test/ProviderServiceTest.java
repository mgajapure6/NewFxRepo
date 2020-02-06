package app.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.Before;
import org.junit.Test;

import app.db.domain.Provider;
import app.db.dto.ProviderBillDto;
import app.db.services.ProviderService;
import junit.framework.Assert;

public class ProviderServiceTest {
	
	ProviderService providerService;
	
	@Before
	public void setup(){
		System.out.println("setup");
		providerService = new ProviderService();
	}
	
	@Test
	public void saveProvider() {
		Boolean isSaved = providerService.saveProvider(new Provider(1, "Alexa John", null, null));
		assertTrue("is saved", isSaved);
	}
	
	@Test
	public void getProviderBillsById() {
		List<ProviderBillDto> pbds = providerService.getProviderBillsById(1);
		assertTrue("is greater than 0", pbds.size()>0);
       // assertThat(pbds, hasSize(0));
        assertTrue("not empty", !pbds.isEmpty());
	}

}
