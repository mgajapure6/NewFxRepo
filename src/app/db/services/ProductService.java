package app.db.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.dao.ProductDao1;
import app.db.dao.ProviderProductDao;
import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;

public class ProductService {

	private ProviderProductDao providerProductDao;
	private ProductDao1 productDao1;

	public ProductService() {
		try {
			providerProductDao = new ProviderProductDao(Persistence.createEntityManagerFactory("MMCStore"));
			productDao1 = new ProductDao1(Persistence.createEntityManagerFactory("MMCStore"));
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public Boolean saveProduct(Product product, Provider provider, Integer qty) {

		if (product != null) {
			try {
				productDao1.create(product);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		ProviderProduct providerProduct = new ProviderProduct();
		providerProduct.setProduct(product);
		providerProduct.setProvider(provider);
		providerProduct.setQtyAvailable(qty);

		try {
			providerProductDao.create(providerProduct);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public List<ProviderProduct> getAllProviderProducts() {
		List<ProviderProduct> providerProducts = null;
		try {
			providerProducts = providerProductDao.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		return providerProducts;
	}

	public List<ProviderProduct> getAllProviderProductsByProviderId(Integer providerId, Provider provider) {
		List<ProviderProduct> providerProducts = new ArrayList<>();
		List l = null;
		try {
			l = providerProductDao.executeQuery(
					"Select pp.providerProductId, pp.productId, p.productName, p.description, p.price, pp.qtyAvailable from providerproduct pp, product p where p.productId=pp.productId and providerId ="
							+ providerId);
			providerProducts = getTypedList(l, provider);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return providerProducts;
	}

	private List<ProviderProduct> getTypedList(List list, Provider provider) {
		List<ProviderProduct> providerProducts = new ArrayList<>();
		Gson gson = new Gson();
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			ProviderProduct pp = gson.fromJson(jsonElement, ProviderProduct.class);
			Product p = gson.fromJson(jsonElement, Product.class);
			pp.setProduct(p);
			pp.setProvider(provider);
			providerProducts.add(pp);
		}
		return providerProducts;
	}

}
