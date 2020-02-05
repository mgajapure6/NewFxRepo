package app.db.util;

public class GenericClass<T> {
	private T type;

	GenericClass(T t) {
		this.type = t;
	}

	public String getType() {
		return this.type.getClass().getName();
	}
}
