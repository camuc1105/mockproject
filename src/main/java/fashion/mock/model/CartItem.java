/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {
	
	@JsonProperty("productId")
	private Long productID;
	private String name;
	private double price;
	private int quantity = 1;

	public CartItem() {
		super();
	}

	public CartItem(Long productID, String name, double price, int quantity) {
		super();
		this.productID = productID;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	public Long getProductID() {
		return productID;
	}

	public void setProductID(Long productID) {
		this.productID = productID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "CartItem [productID=" + productID + ", name=" + name + ", price=" + price + ", quantity=" + quantity
				+ "]";
	}
	
	

}
