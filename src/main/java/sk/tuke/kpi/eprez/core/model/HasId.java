package sk.tuke.kpi.eprez.core.model;

import java.io.Serializable;

public interface HasId<T extends Serializable> extends Serializable {

	T getId();

}
