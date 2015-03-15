package sk.tuke.kpi.eprez.web.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

import sk.tuke.kpi.eprez.core.model.enums.PresentationCategory;

@FacesConverter("presentationCategoryConverter")
public class PresentationCategoryConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {
		return StringUtils.isEmpty(value) ? null : PresentationCategory.valueOf(value);
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value) {
		if (value instanceof PresentationCategory) {
			final PresentationCategory category = (PresentationCategory) value;
			return category.name();
		} else {
			return String.valueOf(value);
		}
	}
}
