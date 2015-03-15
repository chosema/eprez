package sk.tuke.kpi.eprez.web.renderers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.inplace.Inplace;
import org.primefaces.component.inplace.InplaceRenderer;
import org.primefaces.util.HTML;

public class EprezInplaceRenderer extends InplaceRenderer {

	@Override
	protected void encodeMarkup(final FacesContext context, final Inplace inplace) throws IOException {
		final ResponseWriter writer = context.getResponseWriter();
		final String clientId = inplace.getClientId(context);
		final String widgetVar = inplace.resolveWidgetVar();

		final String userStyleClass = inplace.getStyleClass();
		final String userStyle = inplace.getStyle();
		final String styleClass = userStyleClass == null ? Inplace.CONTAINER_CLASS : Inplace.CONTAINER_CLASS + " " + userStyleClass;
		final boolean disabled = inplace.isDisabled();
		final String displayClass = disabled ? Inplace.DISABLED_DISPLAY_CLASS : Inplace.DISPLAY_CLASS;

		final boolean validationFailed = context.isValidationFailed() && !inplace.isValid();
		final String displayStyle = validationFailed ? "none" : "inline-block";
		final String contentStyle = validationFailed ? "inline-block" : "none";

		final UIComponent outputFacet = inplace.getFacet("output");
		final UIComponent inputFacet = inplace.getFacet("input");

		//container
		writer.startElement("span", inplace);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("class", styleClass, "id");
		if (userStyle != null) {
			writer.writeAttribute("style", userStyle, "id");
		}

		writer.writeAttribute(HTML.WIDGET_VAR, widgetVar, null);

		//display
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_display", "id");
		writer.writeAttribute("class", displayClass, null);
		writer.writeAttribute("style", "display:" + displayStyle, null);

		if (outputFacet != null) {
			outputFacet.encodeAll(context);
		} else {
			writer.writeText(getLabelToRender(context, inplace), null);
		}

		writer.endElement("span");

		//content
		if (!inplace.isDisabled()) {
			writer.startElement("span", null);
			writer.writeAttribute("id", clientId + "_content", "id");
			writer.writeAttribute("class", Inplace.CONTENT_CLASS, null);
			writer.writeAttribute("style", "display:" + contentStyle, null);

			if (inputFacet != null) {
				inputFacet.encodeAll(context);
			} else {
				renderChildren(context, inplace);
			}

			if (inplace.isEditor()) {
				encodeEditor(context, inplace);
			}

			writer.endElement("span");
		}

		writer.endElement("span");
	}

}
