package com.szuni.inventory.dialogs;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
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
import org.eclipse.swt.widgets.Widget;

import com.szuni.inventory.model.StoreItem;
import com.szuni.inventory.model.Unit;

public class ModifyStoreItemQuantityDialog extends AbstractDialog {

	private static final char DECIMAL_SEPARATOR = new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator();
	private Map<String, Widget> observableWidgets = new HashMap<>();
	private Text barcodeField;
	private Text nameField;
	private Text quantityField;
	private Text unitField;
	private Text addedQuantityField;
	private ModifyStoreItemModel storeItemModel = new ModifyStoreItemModel();
	private double addedQuantity;

	public ModifyStoreItemQuantityDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("Modify quantity");
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
		createAddedQuantity(container);

		bindValues();

		return area;
	}

	private void createBarcode(Composite container) {
		Label barcodeLabel = new Label(container, SWT.NONE);
		barcodeLabel.setText("Barcode");

		barcodeField = new Text(container, SWT.BORDER);
		barcodeField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createControlDecoration(barcodeField);
		observableWidgets.put("barcode", barcodeField);
	}

	private void createName(Composite container) {
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setText("Name");

		nameField = new Text(container, SWT.BORDER);
		nameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createControlDecoration(nameField);
		observableWidgets.put("name", nameField);
	}

	private void createQuantity(Composite container) {
		Label quantityLabel = new Label(container, SWT.NONE);
		quantityLabel.setText("Quantity");

		quantityField = new Text(container, SWT.READ_ONLY);
		quantityField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		observableWidgets.put("quantity", quantityField);
	}

	private void createUnit(Composite container) {
		Label unitLabel = new Label(container, SWT.NONE);
		unitLabel.setText("Unit");

		unitField = new Text(container, SWT.READ_ONLY);
		unitField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		DataBindingContext ctx = new DataBindingContext();
		IObservableValue widgetValue = WidgetProperties.text().observe(unitField);
		IObservableValue modelValue = BeanProperties.value(ModifyStoreItemModel.class, "unit").observe(storeItemModel);
		IConverter convertToString = IConverter.create(Unit.class, String.class,
				(o1) -> o1 == null ? "" : ((Unit) o1).getStringRepresentation());
		ctx.bindValue(widgetValue, modelValue, new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER),
				UpdateValueStrategy.create(convertToString));
	}

	private void createAddedQuantity(Composite container) {
		Label addedQuantityLabel = new Label(container, SWT.NONE);
		addedQuantityLabel.setText("Quantity to add");

		addedQuantityField = new Text(container, SWT.BORDER);
		addedQuantityField.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event e) {
				String string = e.text;
				char[] chars = new char[string.length()];
				string.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9' || chars[i] == DECIMAL_SEPARATOR || chars[i] == '-')) {
						e.doit = false;
						return;
					}
				}
			}
		});
		createControlDecoration(addedQuantityField);
	}

	private void bindValues() {
		DataBindingContext ctx = new DataBindingContext();
		for (Entry<String, Widget> entry : observableWidgets.entrySet()) {
			IObservableValue widgetValue = WidgetProperties.text(SWT.Modify).observe(entry.getValue());
			IObservableValue modelValue = BeanProperties.value(ModifyStoreItemModel.class, entry.getKey())
					.observe(storeItemModel);
			ctx.bindValue(widgetValue, modelValue);
		}
	}

	@Override
	protected void verifyInput() {
		if (!storeItemModel.hasValue()) {
			setError(barcodeField, "No item selected");
		}

		if (StringUtils.isBlank(addedQuantityField.getText())) {
			setError(addedQuantityField, "Added quantity is empty");
		} else {
			try {
				NumberFormat.getNumberInstance().parse(addedQuantityField.getText()).doubleValue();
			} catch (ParseException e) {
				setError(addedQuantityField, e.getMessage());
			}
		}
	}

	@Override
	protected void saveInput() {
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		try {
			addedQuantity = numberFormat.parse(addedQuantityField.getText()).doubleValue();
		} catch (ParseException e) {
			// error message already set
		}

	}

	public StoreItem getStoreItem() {
		return storeItemModel.getStoreItem();
	}

	public void setStoreItem(StoreItem storeItem) {
		storeItemModel.setStoreItem(storeItem);
	}

	public double getAddedQuantity() {
		return addedQuantity;
	}

}
