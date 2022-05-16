/**
 * 
 */
package com.hzerai.db.prototypes;

import java.io.Serializable;

import javassist.CtClass;

/**
 * @author Habib Zerai
 *
 */
interface Mother extends Serializable, Cloneable {
	public Object get(String key);
	public void set(String key, Object value);
}
