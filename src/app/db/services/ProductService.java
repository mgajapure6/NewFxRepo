package app.db.services;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import app.db.dao.ProductDao;
import app.db.dao.ProviderProductDao;
import app.db.domain.Product;
import app.db.domain.Provider;
import app.db.domain.ProviderProduct;
import app.db.dto.ProviderProductDto;

public class ProductService {

	private ProviderProductDao providerProductDao;
	private ProductDao productDao1;

	public ProductService() {
		try {
			providerProductDao = new ProviderProductDao(Persistence.createEntityManagerFactory("MMCStore"));
			productDao1 = new ProductDao(Persistence.createEntityManagerFactory("MMCStore"));
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

	public List<ProviderProductDto> getAllProviderProductsByProviderId(Integer providerId, Provider provider) {
		List<ProviderProductDto> providerProducts = new ArrayList<>();
		try {
			String query = "Select pp.providerProductId, pp.productId, p.productName, p.description, p.price, pp.qtyAvailable from providerproduct pp, product p where p.productId=pp.productId and pp.providerId ="+ providerId;
			List l = providerProductDao.executeQuery(query,"ProviderProductDtoMapping");
			//List l = providerProductDao.executeQuery(query,ProviderProduct.class);
			providerProducts = getProviderProductDtoList(l);
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
	
	private List<ProviderProductDto> getProviderProductDtoList(List list) {
		List<ProviderProductDto> providerProductDto = new ArrayList<>();
		Gson gson = new Gson();
		for (Object object : list) {
			JsonElement jsonElement = gson.toJsonTree(object);
			ProviderProductDto pp = gson.fromJson(jsonElement, ProviderProductDto.class);
			providerProductDto.add(pp);
		}
		return providerProductDto;
	}

	public Boolean deleteProviderProduct(ProviderProductDto ppd) {
		
		return null;
	}

}
