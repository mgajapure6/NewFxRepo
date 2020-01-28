package app.product;

public class ProductEntity {

	Integer id;
	String productName;
	String productDesc;
	Double qty;
	Double price;

	public ProductEntity(Integer id, String productName, String productDesc, Double qty, Double price) {
		super();
		this.id = id;
		this.productName = productName;
		this.productDesc = productDesc;
		this.qty = qty;
		this.price = price;
	}

	public Double getQty() {
		return qty;
	}

	public void setQty(Double qty) {
		this.qty = qty;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
