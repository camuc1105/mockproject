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
	private String imgLink;
	private String color;
	private String size;
	private int quantity = 1;

	public CartItem() {
		super();
	}

	public CartItem(Long productID, String name, double price, String imgLink, String color, String size,
			int quantity) {
		super();
		this.productID = productID;
		this.name = name;
		this.price = price;
		this.imgLink = imgLink;
		this.color = color;
		this.size = size;
		this.quantity = quantity;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getImgLink() {
		return imgLink;
	}

	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
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
