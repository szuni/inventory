package com.szuni.inventory.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.szuni.inventory.dao.StoreItemDao;
import com.szuni.inventory.model.StoreItem;
import com.szuni.inventory.parts.uiComponents.StoreItemTableFactory;
import com.szuni.inventory.parts.uiComponents.StoreItemTableFilter;

public class InventoryPart {
	private Text filterTxtInput;
	private TableViewer tableViewer;
	private StoreItemTableFilter tableFilter = new StoreItemTableFilter();

	@Inject
	private MDirtyable dirty;
	private StoreItemDao dao = StoreItemDao.getInstance();

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		filterTxtInput = new Text(parent, SWT.BORDER);
		filterTxtInput.setMessage("Enter filter text");
		filterTxtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				tableFilter.setSearchText(filterTxtInput.getText());
				tableViewer.getTable().setSelection(-1);
				tableViewer.refresh();
			}
		});
		// txtInput.addModifyListener(new ModifyListener() {
		// @Override
		// public void modifyText(ModifyEvent e) {
		// dirty.setDirty(true);
		// }
		// });
		filterTxtInput.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		tableViewer = StoreItemTableFactory.createTable(parent);
		tableViewer.setFilters(tableFilter);
		refreshData();
	}

	public void refreshData() {
		tableViewer.setInput(dao.findAll());
		tableViewer.getTable().setSelection(-1);
		tableViewer.refresh();
	}

	@Focus
	public void setFocus() {
		tableViewer.getTable().setFocus();
		tableViewer.getTable().setSelection(-1);
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	public StoreItem getSelectedStoreItem() {
		return (StoreItem) tableViewer.getStructuredSelection().getFirstElement();
	}
}
