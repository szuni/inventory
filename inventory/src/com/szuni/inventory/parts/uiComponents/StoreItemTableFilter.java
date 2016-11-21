package com.szuni.inventory.parts.uiComponents;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.szuni.inventory.model.StoreItem;

public class StoreItemTableFilter extends ViewerFilter {
	
	private String searchString;

    public void setSearchText(String s) {
            // ensure that the value can be used for matching
            this.searchString = ".*" + s + ".*";
    }
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
            return true;
		}
		StoreItem selectedItem = (StoreItem) element;
		if(selectedItem.getBarcode().matches(searchString))
			return true;
		if(selectedItem.getName().matches(searchString))
			return true;
		return false;
	}

}
