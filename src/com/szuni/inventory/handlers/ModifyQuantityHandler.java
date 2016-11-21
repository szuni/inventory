package com.szuni.inventory.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.szuni.inventory.dao.StoreItemDao;
import com.szuni.inventory.dialogs.ModifyStoreItemQuantityDialog;
import com.szuni.inventory.model.StoreItem;
import com.szuni.inventory.parts.InventoryPart;

public class ModifyQuantityHandler {

	@Execute
	public void execute(Shell shell, EPartService partService) {
		ModifyStoreItemQuantityDialog dialog = new ModifyStoreItemQuantityDialog(shell);
		if ("inventory.part.inventoryTable".equals(partService.getActivePart().getElementId())) {
			InventoryPart inventoryPart = (InventoryPart) partService.getActivePart().getObject();
			StoreItem selectedStoreItem = inventoryPart.getSelectedStoreItem();
			if (selectedStoreItem != null)
				dialog.setStoreItem(selectedStoreItem);
		}
		if (Window.OK == dialog.open()) {
			StoreItem storeItem = dialog.getStoreItem();
			storeItem.setQuantity(storeItem.getQuantity() + dialog.getAddedQuantity());
			StoreItemDao.getInstance().save(storeItem);

			MPart part = partService.findPart("inventory.part.inventoryTable");
			InventoryPart inventoryPart = (InventoryPart) part.getObject();
			inventoryPart.refreshData();
		}
	}
}
