/**
 * 
 */
package com.hzerai.db.prototypes;

/**
 * @author Habib Zerai
 *
 */
final class Templates {
	static final String PROTOTYPE = "com.hzerai.db.prototypes.factory.Prototype";
	static final String CONSTRUCTOR = "public %s(%s){}";
	static final String ATTRIBUTEKEY = "public static final String %s = \"%s\";";
	static final String ATTRIBUTEINIT = "private %s %s = %s;";
	static final String ATTRIBUTE = "private %s %s;";
	static final String GETTER = "public %s get%s() {return this.%s;}";
	static final String SETTER = "public void set%s(%s %s) {this.%s = %s;}";
	static final String NULL = "null";

}
