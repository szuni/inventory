package com.szuni.inventory.dao;

import com.szuni.inventory.model.StoreItem;

public class StoreItemDao extends AbstractDao<StoreItem> {
	private static final StoreItemDao INSTANCE = new StoreItemDao();

	private StoreItemDao() {
		super(StoreItem.class);
	}

	public static StoreItemDao getInstance() {
		return INSTANCE;
	}
	
	public StoreItem findByName(String name) {
		return findByAttribute("name", name);
	}
	
	public StoreItem findByBarcode(String barcode) {
		return findByAttribute("barcode", barcode);
	}
}
