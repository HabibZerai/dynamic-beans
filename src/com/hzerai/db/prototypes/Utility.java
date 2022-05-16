/**
 * 
 */
package com.hzerai.db.prototypes;

import static com.hzerai.db.prototypes.Templates.ATTRIBUTE;
import static com.hzerai.db.prototypes.Templates.ATTRIBUTEINIT;
import static com.hzerai.db.prototypes.Templates.ATTRIBUTEKEY;
import static com.hzerai.db.prototypes.Templates.GETTER;
import static com.hzerai.db.prototypes.Templates.PROTOTYPE;
import static com.hzerai.db.prototypes.Templates.SETTER;
import static com.hzerai.db.types.JavaTypes.BOOLEAN;
import static com.hzerai.db.types.JavaTypes.BYTE_ARRAY;
import static com.hzerai.db.types.JavaTypes.CHAR;
import static com.hzerai.db.types.JavaTypes.CHARACTER;
import static com.hzerai.db.types.JavaTypes.DOUBLE;
import static com.hzerai.db.types.JavaTypes.FLOAT;
import static com.hzerai.db.types.JavaTypes.INTEGER;
import static com.hzerai.db.types.JavaTypes.LONG;
import static com.hzerai.db.types.JavaTypes.b;
import static com.hzerai.db.types.JavaTypes.ba;
import static com.hzerai.db.types.JavaTypes.d;
import static com.hzerai.db.types.JavaTypes.f;
import static com.hzerai.db.types.JavaTypes.i;
import static com.hzerai.db.types.JavaTypes.l;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;

import com.hzerai.db.metadata.Database;
import com.hzerai.db.metadata.Table;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * @author Habib Zerai
 *
 */
public abstract class Utility {
	private static Map<String, Class> CLASSREGISTER = new HashMap<>();
	private static Map<String, DynamicBean> BEANREGISTER = new HashMap<>();
	private static Map<String, CtClass> CTCLASS_REGISTER = new HashMap<>();
	public static Map<String, String> SELECT_QUERY_REGISTER = new HashMap<>();
	public static Map<String, String> COUNT_QUERY_REGISTER = new HashMap<>();
	private static Map<String, Set<String>> KEYREGISTER = new HashMap<>();
	private static Map<String, Map<String, String>> KEYTYPES = new HashMap<>();

	static CtClass getPrototype(String name) {
		if (CTCLASS_REGISTER.containsKey(name)) {
			return CTCLASS_REGISTER.get(name);
		}
		CtClass ctClazz = null;
		try {
			ctClazz = initiliazeCtClass(ctClazz);
			if (name != null)
				ctClazz.setName(name);
		} catch (NotFoundException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		CTCLASS_REGISTER.put(name, ctClazz);
		return ctClazz;
	}

	static DynamicBean getBean(String name, Map fields, String packageName) {
		packageName = packageName != null && !packageName.endsWith(".") ? packageName + "." : packageName;

		String localName = packageName != null ? packageName + WordUtils.capitalizeFully(name) : name;
		if (fields == null || fields.isEmpty()) {
			if (!CTCLASS_REGISTER.containsKey(localName)) {
				System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			} else {
				return (DynamicBean) cloned(BEANREGISTER.get(localName));
			}
		}
		if (CTCLASS_REGISTER.containsKey(localName)) {
			System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			return (DynamicBean) cloned(BEANREGISTER.get(localName));
		}
		createSqlQuery(name, fields, localName);
		return getJavaBean(Utility.createFields(getPrototype(localName), fields));
	}

	static DynamicBean getBean(String name, Map fields) {
		String packageName = readPackageFromProperties();
		packageName = packageName != null && !packageName.endsWith(".") ? packageName + "." : packageName;

		String localName = packageName != null ? packageName + WordUtils.capitalizeFully(name) : name;
		if (fields == null || fields.isEmpty()) {
			if (!CTCLASS_REGISTER.containsKey(localName)) {
				System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			} else {
				return (DynamicBean) cloned(BEANREGISTER.get(localName));
			}
		}
		if (CTCLASS_REGISTER.containsKey(localName)) {
			System.err.println("#################### WARN ################## : CAREFUL ! CREATING EMPTY BEAN ! ");
			return (DynamicBean) cloned(BEANREGISTER.get(localName));
		}
		createSqlQuery(name, fields, localName);
		return getJavaBean(Utility.createFields(getPrototype(localName), fields));
	}

	static DynamicBean getBean(String name) {
		if (!BEANREGISTER.containsKey(name)) {
			throw new IllegalArgumentException("Class not yet created : " + name);
		}
		return (DynamicBean) cloned(BEANREGISTER.get(name));
	}

	/**
	 * 
	 */
	private static String readPackageFromProperties() {
		Properties prop = new Properties();
		String propFileName = "config.properties";
		try (InputStream inputStream = Utility.class.getClassLoader().getResourceAsStream(propFileName);) {
			if (inputStream != null) {
				prop.load(inputStream);
			}
			return prop.getProperty("package");
		} catch (IOException e) {
			return null;
		}

	}

	/**
	 * @param name
	 * @param fields
	 */
	private static void createSqlQuery(String name, Map<String, String> fields, String className) {
		if (SELECT_QUERY_REGISTER.containsKey(className)) {
			return;
		}
		StringBuilder sb = new StringBuilder("SELECT ");
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			sb.append(entry.getKey());
			sb.append(",");
		}
		sb.append("1 ");
		sb.append("FROM ");
		sb.append(name);
		SELECT_QUERY_REGISTER.put(className, sb.toString());
		COUNT_QUERY_REGISTER.put(className, "SELECT COUNT(*) FROM " + name);
	}

	/**
	 * @param daughter
	 * @return
	 */
	private static DynamicBean cloned(DynamicBean daughter) {
		try {
			return (DynamicBean) daughter.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static CtClass initiliazeCtClass(CtClass ctClazz) throws NotFoundException, ClassNotFoundException {
		String prot = PROTOTYPE + CLASSREGISTER.size();
		ClassPool cp = ClassPool.getDefault();
		cp.insertClassPath(new ClassClassPath(Class.forName(prot)));
		ctClazz = ClassPool.getDefault().get(prot);
		createField(ctClazz, "ctClass", CtClass.class.getName(), "null");
		createField(ctClazz, "keys", Set.class.getName(), "null");
		return ctClazz;
	}

	static Class getJavaClass(CtClass prototype) {
		if (CLASSREGISTER.containsKey(prototype.getName())) {
			return CLASSREGISTER.get(prototype.getName());
		}
		Class clazz = null;
		try {
			clazz = prototype.toClass();
			CLASSREGISTER.put(prototype.getName(), clazz);
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	static DynamicBean getJavaBean(CtClass prototype) {
		if (BEANREGISTER.containsKey(prototype.getName())) {
			try {
				return (DynamicBean) ((DynamicBean) BEANREGISTER.get(prototype.getName())).clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
		Daughter obj = null;
		obj = createBean(getJavaClass(prototype), prototype);
		BEANREGISTER.put(prototype.getName(), (DynamicBean) obj);
		try {
			return (DynamicBean) ((DynamicBean) obj).clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static DynamicBean createBean(Class<?> clazz, CtClass prototype) {
		DynamicBean obj = null;
		try {
			obj = (DynamicBean) clazz.newInstance();
			obj.setCtClass(prototype);
			obj.setKeys(KEYREGISTER.get(prototype.getSimpleName()));
			obj.setKeyTypes(KEYTYPES.get(prototype.getSimpleName()));
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return obj;
	}

	static CtClass createField(CtClass ctClass, String fieldName, String javaType, String defaultValue) {
		javaType = getRealJavaType(javaType);
		try {
			ctClass.addField(CtField.make(getAttributKey(fieldName), ctClass));
			ctClass.addField(CtField.make(getClassAttribut(fieldName, javaType, defaultValue), ctClass));
			ctClass.addMethod(CtMethod.make(getSetterMethod(fieldName, javaType), ctClass));
			ctClass.addMethod(CtMethod.make(getGetterMethod(fieldName, javaType), ctClass));
		} catch (CannotCompileException e) {
			e.printStackTrace();
		}

		if (KEYREGISTER.containsKey(ctClass.getSimpleName())) {
			KEYREGISTER.get(ctClass.getSimpleName()).add(fieldName);
			KEYTYPES.get(ctClass.getSimpleName()).put(fieldName, javaType);
		} else {
			Set<String> set = new HashSet<>();
			set.add(fieldName);
			KEYREGISTER.put(ctClass.getSimpleName(), set);
			Map<String, String> keyType = new HashMap<>();
			keyType.put(fieldName, javaType);
			KEYTYPES.put(ctClass.getSimpleName(), keyType);
		}
		return ctClass;
	}

	/**
	 * @param fieldName
	 * @return
	 */
	private static String getAttributKey(String fieldName) {
		return String.format(ATTRIBUTEKEY, createKeyForAttribute(fieldName), fieldName);
	}

	/**
	 * @param fieldName
	 * @return
	 */
	private static String createKeyForAttribute(String fieldName) {
		String key = "KEY_";
		for (int i = 0; i < fieldName.length(); i++) {
			char ch = fieldName.charAt(i);
			if (Character.isUpperCase(ch)) {
				key += "_" + ch;
			} else {
				key += ch;
			}
		}
		return key.toUpperCase();
	}

	private static String addQuotes(String defaultValue) {
		return "\"" + defaultValue + "\"";
	}

	private static String getSetterMethod(String fieldName, String javaType) {
		String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return String.format(SETTER, fieldNameCapitalized, javaType, fieldName, fieldName, fieldName);
	}

	private static String getGetterMethod(String fieldName, String javaType) {
		String fieldNameCapitalized = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		return String.format(GETTER, javaType, fieldNameCapitalized, fieldName);
	}

	private static String getRealJavaType(String javaType) {
		switch (javaType) {
		case i:
			return INTEGER;
		case f:
			return FLOAT;
		case b:
			return BOOLEAN;
		case d:
			return DOUBLE;
		case CHAR:
			return CHARACTER;
		case l:
			return LONG;
		case ba:
			return BYTE_ARRAY;
		}
		return javaType;
	}

	private static String getClassAttribut(String fieldName, String javaType, String defaultValue) {
		if (defaultValue == null && javaType.matches("int|boolean|float|double|long|char")) {
			return String.format(ATTRIBUTE, javaType, fieldName);
		}
		return String.format(ATTRIBUTEINIT, javaType, fieldName, getDefaultValue(defaultValue, javaType));
	}

	private static String getDefaultValue(String defaultValue, String javaType) {
//		if (javaType.equals(String.class.getName()))
//			return addQuotes(defaultValue);
		return defaultValue;
	}

	/**
	 * @param ctClass
	 * @param fields
	 */
	static CtClass createFields(CtClass ctClass, Map<String, String> fields) {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			createField(ctClass, entry.getKey(), entry.getValue(), null);
		}
		return ctClass;
	}

	public static boolean isRegistredBean(String beanName) {
		return BEANREGISTER.containsKey(beanName);
	}
	
	public static Set<String> getRegistredBeans(){
		return Collections.unmodifiableSet(BEANREGISTER.keySet());
	}

	public static void load(String packageName) {
		List<Table> tables;
		try {
			tables = Database.getTables();
		} catch (Exception e) {
			return;
		}
		for (Table table : tables) {
			Map<String, String> fields = new HashMap<String, String>();
			table.getColumns().forEach(c -> {
				fields.put(c.getName(), c.getClassName());
			});
			if (packageName != null) {
				getBean(table.getName(), fields, packageName);
			} else {
				getBean(table.getName(), fields);
			}
		}
	}
}
