package net.unknownuser.cloptions;

public class TypeHolder<T> {
	private T obj;

	public TypeHolder() {
		super();
	}
	
	public TypeHolder(T obj) {
		super();
		this.obj = obj;
	}

	public T get() {
		return obj;
	}

	public void set(T obj) {
		this.obj = obj;
	}
}
