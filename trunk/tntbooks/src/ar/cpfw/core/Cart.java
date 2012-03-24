package ar.cpfw.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * @author Enrique Molinari
 */
public class Cart<T> {

	private Map<T, Integer> items = new HashMap<T, Integer>();
	private Validatable<T> validatable;

	public void add(T item) {
		add(item, 1);
	}

	public boolean contains(T item) {
		return items.containsKey(item);
	}

	public void add(T item, int quantity) {

		checkValidItem(item);

		if (quantity < 1) {
			throw new IllegalArgumentException(
					"Item quantity must be greater than zero");
		}

		if (contains(item)) {
			items.put(item, quantityFor(item) + quantity);
		} else {
			items.put(item, quantity);
		}
	}

	private void checkValidItem(T item) {
		if (this.validatable != null) {
			this.validatable.validate(item);
		}
	}

	public int quantityFor(T item) {
		return items.get(item);
	}

	public int size() {
		int size = 0;
		for (T item : items.keySet()) {
			size += items.get(item);
		}
		return size;
	}

	public Set<T> allItems() {
		return new ImmutableSet.Builder<T>().addAll(items.keySet()).build();
	}

	public Map<T, Integer> itemsAndQuantity() {
		return new ImmutableMap.Builder<T, Integer>().putAll(items).build();
	}

	public void setValidator(Validatable<T> validatable) {
		this.validatable = validatable;
	}
}
