package com.szuni.inventory.dialogs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public abstract class AbstractDialog extends TitleAreaDialog{

	protected Map<Control, ControlDecoration> controlDecorations = new HashMap<>();

	public AbstractDialog(Shell parentShell) {
		super(parentShell);
	}


	@Override
	protected void okPressed() {
		setErrorMessage(null);
		for(ControlDecoration decoration : controlDecorations.values())
			decoration.hide();
		verifyInput();
		if (StringUtils.isEmpty(getErrorMessage())) {
			saveInput();
			super.okPressed();
		}
	}

	protected abstract void verifyInput();
	protected abstract void saveInput();

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	protected void setError(Control field, String message) {
		if(getErrorMessage() == null) {
			setErrorMessage(message);
		}
		ControlDecoration controlDecoration = controlDecorations.get(field);
		controlDecoration.setDescriptionText(message);
		controlDecoration.show();
	}
	
	protected void createControlDecoration(Control field) {
		final ControlDecoration decorator = new ControlDecoration(field, SWT.TOP|SWT.LEFT);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		Image img = fieldDecoration.getImage();
		decorator.setImage(img);
		decorator.hide();
		controlDecorations.put(field, decorator);
	}
}
