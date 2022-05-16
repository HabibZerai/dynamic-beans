/**
 * 
 */
package com.hzerai.db.prototypes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javassist.CtClass;

/**
 * @author Habib Zerai
 *
 */
class Daughter implements Mother {
	private CtClass ctClass;
	private Set<String> Keys;
	private Map<String, String> keyTypes;
	private String name;

	@Override
	public Object get(String key) {
		if (key.contains(".")) {
			return complexGet(key);
		}
		if (key.matches("keys|keyTypes|ctClass")) {
			return handleMethod(key);
		}
		if (!Keys.contains(key)) {
			System.err.println("######## No such field : " + key);
			return null;
		}
		return get(getterMethod(key));
	}

	@Override
	public void set(String key, Object value) {
		if (key.contains(".")) {
			complexSet(key, value);
			return;
		}
		if (key.matches("keys|keyTypes|ctClass")) {
			System.err.println("######## can't set this key (locked) : " + key);
			return;
		}
		if (!Keys.contains(key)) {
			System.err.println("######## No such field : " + key);
			return;
		}
//		checkType(key, value);
		set(setterMethod(key, value == null ? null : value.getClass()), value);
	}

	/**
	 * @param key
	 * @return
	 */
	private void complexSet(String key, Object value) {
		String firstKey = key.substring(0, key.indexOf("."));
		String restKey = key.substring(key.indexOf(".") + 1, key.length());
		DynamicBean result = (DynamicBean) get(firstKey);
		result.set(restKey, value);
	}

	/**
	 * @param key
	 * @return
	 */
	private Object complexGet(String key) {
		String firstKey = key.substring(0, key.indexOf("."));
		String restKey = key.substring(key.indexOf(".") + 1, key.length());
		DynamicBean result = (DynamicBean) get(firstKey);
		return result.get(restKey);
	}

	/**
	 * @param key
	 * @return
	 */
	private Object handleMethod(String key) {
		switch (key) {
		case "keys":
			return Keys;
		case "keytypes":
			return keyTypes;
		case "ctClass":
			return ctClass;
		}
		return null;
	}

	private Method getterMethod(String key) {
		Method getter = null;
		try {
			getter = this.getClass().getMethod(getterName(key));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return getter;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	private Method setterMethod(String key, Class<? extends Object> clazz) {
		String setterName = setterName(key);
		Method setter = null;
		try {
			if (clazz == null) {
				for (Method m : this.getClass().getMethods()) {
					if (setterName.equals(m.getName())) {
						return m;
					}
				}
			}
			setter = this.getClass().getMethod(setterName, clazz);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return setter;
	}

	private Object get(Method getterMethod) {
		Object value = null;
		try {
			value = getterMethod.invoke(this);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return value;
	}

	private void set(Method setterMethod, Object value) {
		try {
			setterMethod.invoke(this, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	private String getterName(String key) {
		return "get" + capitalize(key);
	}

	private String setterName(String key) {
		return "set" + capitalize(key);
	}

	private String capitalize(String key) {
		return key.substring(0, 1).toUpperCase() + key.substring(1);
	}

	void setCtClass(CtClass ctClass) {
		this.ctClass = ctClass;
	}

	CtClass getCtClass() {
		return this.ctClass;
	}

	void setKeys(Set<String> set) {
		this.Keys = set;
	}

	Map<String, String> getKeyTypes() {
		return this.keyTypes;
	}

	void setKeyTypes(Map<String, String> keyTypes) {
		this.keyTypes = keyTypes;
	}

	Set<String> getKeys() {
		return this.Keys;
	}

	protected String getName() {
		return this.ctClass.getSimpleName();
	}

}
