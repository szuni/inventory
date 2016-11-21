package com.szuni.inventory.dialogs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.szuni.inventory.dao.StoreItemDao;
import com.szuni.inventory.model.StoreItem;
import com.szuni.inventory.model.Unit;

public class ModifyStoreItemModel {

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	private final StoreItemDao dao = StoreItemDao.getInstance();
	private StoreItem storeItem = new StoreItem();

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}
	
	public String getName() {
		return storeItem.getName();
	}

	public void setName(String name) {
		try {
			updateModel(dao.findByName(name));
		} catch (NonUniqueResultException | NoResultException ex) {
			clearModel();
			storeItem.setName(name);
		}
	}
	
	public String getBarcode() {
		return storeItem.getBarcode();
	}

	public void setBarcode(String barcode) {
		try {
			updateModel(dao.findByBarcode(barcode));
		} catch (NonUniqueResultException | NoResultException ex) {
			clearModel();
			storeItem.setBarcode(barcode);
		}
	}
	
	public Double getQuantity() {
		return storeItem.getQuantity();
	}

	public Unit getUnit() {
		return storeItem.getUnit();
	}

	public StoreItem getStoreItem() {
		return storeItem;
	}
	
	public void setStoreItem(StoreItem storeItem) {
		this.storeItem = storeItem;
	}

	private void clearModel() {
		StoreItem oldModel = storeItem ;
		storeItem = new StoreItem();
		firePropertyChange(oldModel);
	}

	private void updateModel(StoreItem dbo) {
		StoreItem oldModel = storeItem;
		storeItem = dbo;
		firePropertyChange(oldModel);
	}
	
	private void firePropertyChange(StoreItem oldModel) {
		changeSupport.firePropertyChange("barcode", oldModel.getBarcode(), storeItem.getBarcode());
		changeSupport.firePropertyChange("name", oldModel.getName(), storeItem.getName());
		changeSupport.firePropertyChange("quantity", oldModel.getQuantity(), storeItem.getQuantity());
		changeSupport.firePropertyChange("unit", oldModel.getUnit(), storeItem.getUnit());
	}

	public boolean hasValue() {
		return storeItem.getId() != null;
	}
}
