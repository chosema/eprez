package sk.tuke.kpi.eprez.web.renderers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.autocomplete.AutoCompleteRenderer;
import org.primefaces.util.ComponentUtils;

public class EprezAutocompleteRenderer extends AutoCompleteRenderer {

	@Override
	protected void encodeMultipleMarkup(final FacesContext context, final AutoComplete ac) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = ac.getClientId(context);
		final String inputId = clientId + "_input";
		final List values = (List) ac.getValue();
		final List<String> stringValues = new ArrayList<String>();
		final boolean disabled = ac.isDisabled();
		final String tabindex = ac.getTabindex();

		String styleClass = ac.getStyleClass();
		styleClass = styleClass == null ? AutoComplete.MULTIPLE_STYLE_CLASS : AutoComplete.MULTIPLE_STYLE_CLASS + " " + styleClass;
		final String listClass = disabled ? AutoComplete.MULTIPLE_CONTAINER_CLASS + " ui-state-disabled" : AutoComplete.MULTIPLE_CONTAINER_CLASS;

		writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("class", styleClass, null);
		if (ac.getStyle() != null) {
			writer.writeAttribute("style", ac.getStyle(), null);
		}

		writer.startElement("ul", null);
		writer.writeAttribute("class", listClass, null);

		if (values != null && !values.isEmpty()) {
			final Converter converter = ComponentUtils.getConverter(context, ac);
			final String var = ac.getVar();
			final boolean pojo = var != null;

			for (final Iterator<Object> it = values.iterator(); it.hasNext();) {
				final Object value = it.next();
				Object itemValue = null;
				String itemLabel = null;

				if (pojo) {
					context.getExternalContext().getRequestMap().put(var, value);
					itemValue = ac.getItemValue();
					itemLabel = ac.getItemLabel();
				} else {
					itemValue = value;
					itemLabel = String.valueOf(value);
				}

				final String tokenValue = converter != null ? converter.getAsString(context, ac, itemValue) : String.valueOf(itemValue);

				writer.startElement("li", null);
				writer.writeAttribute("data-token-value", tokenValue, null);
				writer.writeAttribute("class", AutoComplete.TOKEN_DISPLAY_CLASS, null);

				writer.startElement("span", null);
				writer.writeAttribute("class", AutoComplete.TOKEN_LABEL_CLASS, null);
				writer.writeText(itemLabel, null);
				writer.endElement("span");

				writer.startElement("span", null);
				writer.writeAttribute("class", AutoComplete.TOKEN_ICON_CLASS, null);
				writer.endElement("span");

				writer.endElement("li");

				stringValues.add(tokenValue);
			}
		}

		writer.startElement("li", null);
		writer.writeAttribute("class", AutoComplete.TOKEN_INPUT_CLASS, null);
		writer.startElement("input", null);
		writer.writeAttribute("type", "text", null);
		writer.writeAttribute("id", inputId, null);
		writer.writeAttribute("name", inputId, null);
		writer.writeAttribute("autocomplete", "off", null);
		if (StringUtils.isNotEmpty(ac.getPlaceholder()) && CollectionUtils.isEmpty(values)) {
			writer.writeAttribute("placeholder", ac.getPlaceholder(), null);
		}
		if (disabled) {
			writer.writeAttribute("disabled", "disabled", "disabled");
		}
		if (tabindex != null) {
			writer.writeAttribute("tabindex", tabindex, null);
		}
		if (ac.getMaxlength() != Integer.MIN_VALUE) {
			writer.writeAttribute("maxlength", "" + ac.getMaxlength(), null);
		}

		writer.endElement("input");
		writer.endElement("li");

		writer.endElement("ul");

		encodePanel(context, ac);

		encodeHiddenSelect(context, ac, clientId, stringValues);

		writer.endElement("div");
	}

}
