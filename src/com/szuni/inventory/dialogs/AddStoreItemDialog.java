package com.szuni.inventory.dialogs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.szuni.inventory.dao.StoreItemDao;
import com.szuni.inventory.model.StoreItem;
import com.szuni.inventory.model.Unit;

public class AddStoreItemDialog extends AbstractDialog {

	private static final char DECIMAL_SEPARATOR = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
	private Text barcodeField;
	private Text nameField;
	private Text quantityField;
	private ComboViewer unitField;
	private StoreItem storeItem;

	public AddStoreItemDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Add new store item");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createBarcode(container);
		createName(container);
		createQuantity(container);
		createUnit(container);

		return area;
	}

	private void createBarcode(Composite container) {
		Label barcodeLabel = new Label(container, SWT.NONE);
		barcodeLabel.setText("Barcode");

		barcodeField = new Text(container, SWT.BORDER);
		barcodeField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createControlDecoration(barcodeField);
	}

	private void createName(Composite container) {
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name");

		nameField = new Text(container, SWT.BORDER);
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createControlDecoration(nameField);
	}

	private void createQuantity(Composite container) {
		Label quantityLabel = new Label(container, SWT.NONE);
		quantityLabel.setText("Quantity");

		quantityField = new Text(container, SWT.BORDER);
		quantityField.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9' || chars[i] == DECIMAL_SEPARATOR)) {
						e.doit = false;
						return;
					}
				}
			}
		});
		createControlDecoration(quantityField);
	}

	private void createUnit(Composite container) {
		Label unitLabel = new Label(container, SWT.NONE);
		unitLabel.setText("Unit");

		unitField = new ComboViewer(container, SWT.READ_ONLY);
		unitField.setContentProvider(ArrayContentProvider.getInstance());
		unitField.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Unit) {
					Unit unit = (Unit) element;
					return unit.getStringRepresentation();
				}
				return super.getText(element);
			}
		});
		unitField.setInput(Unit.values());
		createControlDecoration(unitField.getControl());
	}

	// save content of the Text fields because they get disposed
	// as soon as the Dialog closes
	@Override
	protected void saveInput() {
		storeItem = new StoreItem();
		storeItem.setBarcode(barcodeField.getText());
		storeItem.setName(nameField.getText());

		if (StringUtils.isEmpty(quantityField.getText())) {
			storeItem.setQuantity(0.0);
		} else {
			NumberFormat numberFormat = NumberFormat.getNumberInstance();
			try {
				double quantity = numberFormat.parse(quantityField.getText()).doubleValue();
				storeItem.setQuantity(quantity);
			} catch (ParseException e) {
				// error message already set
			}
		}
		storeItem.setUnit((Unit) unitField.getStructuredSelection().getFirstElement());
	}

	@Override
	protected void verifyInput() {
	
		if (StringUtils.isBlank(barcodeField.getText())) {
			setError(barcodeField, "Barcode is empty");
		} else {
			try {
				StoreItemDao.getInstance().findByBarcode(barcodeField.getText());
				setError(barcodeField, "Barcode must be unique");
			} catch(NonUniqueResultException | NoResultException e) {
				//No problem
			}
		}

		if (StringUtils.isBlank(nameField.getText())) {
			setError(nameField, "Name is empty");
		}

		if (StringUtils.isNotBlank(quantityField.getText())) {
			try {
				NumberFormat.getNumberInstance().parse(quantityField.getText()).doubleValue();
			} catch (ParseException e) {
				setError(quantityField, e.getMessage());
			}
		}
		
		if(unitField.getSelection().isEmpty()) {
			setError(unitField.getControl(), "Unit is empty");
		}
	}

	public StoreItem getStoreItem() {
		return storeItem;
	}

}
