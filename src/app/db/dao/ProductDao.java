package app.db.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;
import app.db.domain.User;
import app.db.util.HibernateUtil;

public class ProductDao {

	public Boolean saveProduct(Product product, Provider provider, Integer qty) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		if (product != null) {
			session.save(product);
		}

		ProviderProduct providerProduct = new ProviderProduct();
		providerProduct.setProduct(product);
		providerProduct.setProvider(provider);
		providerProduct.setQtyAvailable(qty);

		session.saveOrUpdate(providerProduct);
		session.getTransaction().commit();
		session.close();
		return true;

	}
/* For providers 
 * SELECT * from bill a join billproviderproduct b on (a.billId = b.billId)
					 join providerproduct c on (b.providerProductId = c.providerProductId)
                     join provider d on (c.providerId = d.providerId)
                     join product e on (c.productId = e.productId)
 * 
 * 
 * */
	public List<ProviderProduct> getAllProviderProducts() {
		//select * from product p join providerproduct R ON (p.productId = r.providerProductId)
		//join provider pr on (r.providerId = pr.providerId);
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();	
		List<ProviderProduct> providerProducts = session.createQuery("From app.db.domain.ProviderProduct").list();	
		session.getTransaction().commit();
		session.close();
		return providerProducts;
	}
	
	public List<ProviderProduct> getAllProviderProductsByProviderId(Integer providerId, Provider provider) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();	
		System.out.println("llllllll");
		//Criteria criteria = session.createCriteria(ProviderProduct.class);
        //criteria.add(Restrictions.eq("provider", provider));
        SQLQuery sqlQuery = session.createSQLQuery("Select pp.providerProductId, pp.productId, p.productName, p.description, p.price, pp.qtyAvailable from providerproduct pp, product p where p.productId=pp.productId and providerId ="+providerId);
        sqlQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		//Query query = session.createQuery("From app.db.domain.Product where providerId = :providerId");
		//query.setParameter("providerId", providerId);
		//query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        List<ProviderProduct> providerProducts = getTypedList(sqlQuery.list(), provider);	
		//List simpleProviderProducts = 
		
		
		System.out.println("providerProducts::"+providerProducts);
		session.getTransaction().commit();
		session.close();
		return providerProducts;
	}
//	
//	SELECT * from bill a join billproviderproduct b on (a.billId = b.billId)
//	 join providerproduct c on (b.providerProductId = c.providerProductId)
//    join provider d on (c.providerId = d.providerId)
//    join product e on (c.productId = e.productId)
//    join customer f on (a.customerId = f.customerId)
//where d.providerId = ?
//or f.customerId = ?
	private List<ProviderProduct> getTypedList(List list,Provider provider) {
		List<ProviderProduct> providerProducts = new ArrayList<>();
		Gson gson = new Gson();
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			ProviderProduct pp = gson.fromJson(jsonElement, ProviderProduct.class);
			Product p = gson.fromJson(jsonElement, Product.class);
			pp.setProduct(p);
			pp.setProvider(provider);
			System.out.println("p::"+p);
			providerProducts.add(pp);
		}
		return providerProducts;
	}

	
}
