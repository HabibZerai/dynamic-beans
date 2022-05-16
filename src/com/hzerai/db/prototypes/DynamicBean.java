/**
 * 
 */
package com.hzerai.db.prototypes;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Habib Zerai
 *
 */
public class DynamicBean extends Daughter {

	public static DynamicBean newInstance(String beanName) {
		return Utility.getBean(beanName);
	}

	public static DynamicBean newInstance(String beanName, Map<String, String> fields) {
		return Utility.getBean(beanName, fields);
	}

	public static DynamicBean newInstance(String beanName, Map<String, String> fields, String packageName) {
		return Utility.getBean(beanName, fields, packageName);
	}

	public static void register(String beanName, Map<String, String> fields, String packageName) {
		Utility.getBean(beanName, fields, packageName);
	}

	public static void register(String beanName) {
		Utility.getBean(beanName);
	}

	public static void register(String beanName, Map<String, String> fields) {
		Utility.getBean(beanName, fields);
	}

	public Class<?> getDynamicClass() {
		return super.getClass();
	}

	public void set(String key, Object value) {
		super.set(key, value);
	}

	public Object get(String key) {
		return super.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.getName() + "[ ");
		Iterator<String> keys = super.getKeys().iterator();

		while (keys.hasNext()) {
			String key = keys.next();
			if (get(key) != null) {
				sb.append(key);
				sb.append(" : ");
				sb.append(get(key));
				if (keys.hasNext())
					sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public static boolean isRegistred(String beanName) {
		return Utility.isRegistredBean(beanName);
	}

	/**
	 * @param out
	 */
	public void toString(PrintStream out) {
		out.println(this);
	}

}
