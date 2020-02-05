package app.db.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.dao.ProviderDao;
import app.db.dto.ProviderBillDto;

public class ProviderService {

	private ProviderDao providerDao;

	public ProviderService() {
		try {
			providerDao = new ProviderDao(Persistence.createEntityManagerFactory("MMCStore"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public List<ProviderBillDto> getProviderBillsById(Integer providerId) {
		String sql = "select a.billId,a.billDate,case when a.isPaid > 0 then 'Paid' else 'Unpaid' end as status, e.customerName,e.address,d.productName,d.description,b.qtyRequested,c.qtyAvailable,d.price, (b.qtyRequested * d.price) as billAmount from bill a join billproviderproduct b on(a.billId=b.billId) join providerproduct c on (b.providerProductId = c.providerProductId) join product d on (c.productId = d.productId) join customer e on (a.customerId = e.customerId) where c.providerId = "+providerId+" order by a.billId,a.billDate,a.customerId";
		List<ProviderBillDto> pdt = null;
		try {
			pdt = getProviderBillsList(providerDao.executeQuery(sql));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdt;
	}

	private List<ProviderBillDto> getProviderBillsList(List list) {
		List<ProviderBillDto> pds = new ArrayList<>();
		Gson gson = new Gson();
		System.out.println("jsonElement list:"+list);
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			System.out.println("jsonElement:"+jsonElement);
			ProviderBillDto b = gson.fromJson(jsonElement, ProviderBillDto.class);
			pds.add(b);
		}
		return pds;
	}

}
