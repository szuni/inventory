package com.szuni.inventory.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import com.szuni.inventory.dao.StoreItemDao;
import com.szuni.inventory.dialogs.AddStoreItemDialog;
import com.szuni.inventory.parts.InventoryPart;

public class CreateStoreItemHandler {

	@Execute
	public void execute(Shell shell, EPartService partService) {
		AddStoreItemDialog dialog = new AddStoreItemDialog(shell);
		if(Window.OK == dialog.open()) {
			StoreItemDao.getInstance().save(dialog.getStoreItem());
			
			MPart part = partService.findPart("inventory.part.inventoryTable");
			InventoryPart inventoryPart = (InventoryPart) part.getObject();
			inventoryPart.refreshData();
		}
	}
}
