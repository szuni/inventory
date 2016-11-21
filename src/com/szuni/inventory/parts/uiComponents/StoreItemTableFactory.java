package com.szuni.inventory.parts.uiComponents;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import com.szuni.inventory.model.StoreItem;

public class StoreItemTableFactory {

	public static TableViewer createTable(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		TableColumnLayout tableColumnLayout = new TableColumnLayout();
		tableComposite.setLayout(tableColumnLayout);
		tableComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		TableViewer tableViewer = new TableViewer(tableComposite,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.FILL);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());

		createColumns(tableViewer, tableColumnLayout);

		return tableViewer;
	}

	private static void createColumns(TableViewer tableViewer, TableColumnLayout tableColumnLayout) {
		String[] titles = { "Barcode", "Item name", "quantity", "unit" };
		int[] minWidth = { 100, 250, 60, 40 };
		int[] width = { 10, 100, 6, 4 };
		StoreItemLabelProvider columnLabelProviders[] = { s -> s.getBarcode(), s -> s.getName(),
				s -> Double.toString(s.getQuantity()), s -> s.getUnit().getStringRepresentation() };

		for (int i = 0; i < titles.length; ++i) {
			final StoreItemLabelProvider labelProvider = columnLabelProviders[i];
			TableViewerColumn column = createTableViewerColumn(tableViewer, titles[i]);
			column.setLabelProvider(new ColumnLabelProvider() {
				@Override
				public String getText(Object element) {
					StoreItem storeItem = (StoreItem) element;
					return labelProvider.getLabel(storeItem);
				}
			});
			tableColumnLayout.setColumnData(column.getColumn(),
                    new ColumnWeightData(width[i], minWidth[i], true));
		}
	}

	private static TableViewerColumn createTableViewerColumn(TableViewer tableViewer, String title) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	@FunctionalInterface
	private static interface StoreItemLabelProvider {
		String getLabel(StoreItem storeItem);
	}
}
