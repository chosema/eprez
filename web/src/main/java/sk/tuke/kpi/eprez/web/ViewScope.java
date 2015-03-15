package sk.tuke.kpi.eprez.web;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ViewScope implements Scope {

	@Override
	public Object get(final String name, final ObjectFactory<?> objectFactory) {
		final Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
		if (viewMap.containsKey(name)) {
			return viewMap.get(name);
		} else {
			final Object object = objectFactory.getObject();
			viewMap.put(name, object);
			return object;
		}
	}

	@Override
	public Object remove(final String name) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
	}

	@Override
	public void registerDestructionCallback(final String name, final Runnable callback) { // Not supported
	}

	@Override
	public Object resolveContextualObject(final String key) {
		return null;
	}

	@Override
	public String getConversationId() {
		return null;
	}

}
