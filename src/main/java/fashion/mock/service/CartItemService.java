/**
 * Author: Nguyen Cong Huan. 06/03/1999
 */
package fashion.mock.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import fashion.mock.model.CartItem;

@Service
public class CartItemService {
	Map<Long, CartItem> maps = new HashMap<>();

	public void add(CartItem item) {
		CartItem cartItem = maps.get(item.getProductID());
		if (cartItem == null) {
			maps.put(item.getProductID(), item);
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		}
	}

	public void remove(long id) {
		maps.remove(id);
	}

	public CartItem update(long productID, int qty) {
		CartItem cartItem = maps.get(productID);
		cartItem.setQuantity(qty);
		return cartItem;
	}

	public Collection<CartItem> getAllItems() {
		return maps.values();
	}

	public int getCount() {
		return maps.values().size();
	}

	public double getAmount() {
		return maps.values().stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
	}
}
