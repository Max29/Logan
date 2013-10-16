package de.iteratec.logan.view.config.dialog;

import de.iteratec.logan.Messages;
import de.iteratec.logan.model.Expression;
import de.iteratec.logan.view.config.CustomColorSelector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;

import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.internal.databinding.util.JFaceProperty;
import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.resource.StringConverter;


@SuppressWarnings("restriction")
public class AddExpressionDialog extends TitleAreaDialog {
  private boolean             dirty;
  private Expression          expression;
  private Text                nameText;
  private Text                searchText;
  private Button              buttonEnabled;
  private Button              buttonRegexp;
  private Button              buttonCaseSensitive;
  private CustomColorSelector bgColorSelector;
  private CustomColorSelector fgColorSelector;

  public AddExpressionDialog(Shell parentShell) {
    super(parentShell);
    this.expression = new Expression();
    this.expression.setEnabled(true);
  }

  public AddExpressionDialog(Shell parentShell, Expression expression) {
    super(parentShell);

    if (expression == null) {
      this.expression = new Expression();
      this.expression.setEnabled(true);
    }
    else {
      this.expression = expression;
    }
  }

  @Override
  public void create() {
    super.create();
    setTitle(Messages.AddExpressionDialog_title);
    setMessage(Messages.AddExpressionDialog_captionMessage, IMessageProvider.INFORMATION);
  }

  @Override
  protected Control createDialogArea(final Composite parent) {
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    parent.setLayout(layout);

    // The text fields will grow with the size of the dialog
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.FILL;

    Label nameLabel = new Label(parent, SWT.NONE);
    nameLabel.setText(Messages.AddExpressionDialog_name);
    nameText = new Text(parent, SWT.BORDER);
    nameText.setLayoutData(gridData);

    Label searchLabel = new Label(parent, SWT.NONE);
    searchLabel.setText(Messages.AddExpressionDialog_searchExpression);
    searchText = new Text(parent, SWT.BORDER);
    searchText.setLayoutData(gridData);

    Label label3 = new Label(parent, SWT.NONE);
    label3.setText(Messages.AddExpressionDialog_enabled);
    buttonEnabled = new Button(parent, SWT.CHECK);

    Label label4 = new Label(parent, SWT.NONE);
    label4.setText(Messages.AddExpressionDialog_regularExpression);
    buttonRegexp = new Button(parent, SWT.CHECK);

    Label label5 = new Label(parent, SWT.NONE);
    label5.setText(Messages.AddExpressionDialog_caseSensitive);
    buttonCaseSensitive = new Button(parent, SWT.CHECK);

    Label bgColorlabel = new Label(parent, SWT.NONE);
    bgColorlabel.setText(Messages.AddExpressionDialog_backgroundColor);
    bgColorSelector = new CustomColorSelector(parent);

    Label fgColorlabel = new Label(parent, SWT.NONE);
    fgColorlabel.setText(Messages.AddExpressionDialog_textColor);
    fgColorSelector = new CustomColorSelector(parent);

    DataBindingContext bindingContext = new DataBindingContext();
    bindingContext.bindValue(SWTObservables.observeText(nameText, SWT.Modify),
        PojoObservables.observeValue(expression, Expression.NAME));
    bindingContext.bindValue(SWTObservables.observeText(searchText, SWT.Modify),
        PojoObservables.observeValue(expression, Expression.SEARCH));
    bindingContext.bindValue(SWTObservables.observeSelection(buttonEnabled),
        PojoObservables.observeValue(expression, Expression.ENABLED_PROP));
    bindingContext.bindValue(SWTObservables.observeSelection(buttonCaseSensitive),
        PojoObservables.observeValue(expression, Expression.CASE_SENSITIVE));
    bindingContext.bindValue(SWTObservables.observeSelection(buttonRegexp),
        PojoObservables.observeValue(expression, Expression.REGEXP_PROP));

    UpdateValueStrategy toRgbConverter = new UpdateValueStrategy().setConverter(new ToRGBConverter());
    UpdateValueStrategy fromRgbConverter = new UpdateValueStrategy().setConverter(new FromRGBConverter());
    IObservableValue fgTarget = new JFaceProperty(ColorSelector.PROP_COLORCHANGE, ColorSelector.PROP_COLORCHANGE,
        CustomColorSelector.class).observe(fgColorSelector);
    bindingContext.bindValue(fgTarget, PojoObservables.observeValue(expression, Expression.TEXT_COLOR),
        fromRgbConverter, toRgbConverter);

    IObservableValue bgTarget = new JFaceProperty(ColorSelector.PROP_COLORCHANGE, ColorSelector.PROP_COLORCHANGE,
        CustomColorSelector.class).observe(bgColorSelector);
    bindingContext.bindValue(bgTarget, PojoObservables.observeValue(expression, Expression.BACKGROUND_COLOR),
        fromRgbConverter, toRgbConverter);

    bindingContext.updateTargets();

    IObservableList bindings = bindingContext.getValidationStatusProviders();
    for (Object o : bindings) {
      Binding b = (Binding) o;
      b.getTarget().addChangeListener(listener);
    }

    return parent;
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  @Override
  protected void okPressed() {
    if (nameText.getText().length() != 0 && searchText.getText().length() != 0) {
      super.okPressed();
    }
    else {
      setErrorMessage(Messages.AddExpressionDialog_errorEnterRequiredData);
    }
  }

  public Expression getExpression() {
    return expression;
  }

  public boolean isDirty() {
    return dirty;
  }

  private class ToRGBConverter extends Converter {
    public ToRGBConverter() {
      super("", new RGB(0, 0, 0)); //$NON-NLS-1$
    }

    @Override
    public Object convert(Object fromObject) {
      String colorValue = (String) fromObject;
      return StringConverter.asRGB(colorValue);
    }
  }

  private class FromRGBConverter extends Converter {
    public FromRGBConverter() {
      super(new RGB(0, 0, 0), ""); //$NON-NLS-1$
    }

    @Override
    public Object convert(Object fromObject) {
      RGB rgb = (RGB) fromObject;
      return StringConverter.asString(rgb);
    }
  }

  IChangeListener listener = new IChangeListener() {
                             @Override
                             public void handleChange(ChangeEvent event) {
                               dirty = true;
                             }
                           };
}
