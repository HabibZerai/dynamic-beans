/**
 * 
 */
package com.hzerai.db.data.interfaces;

import java.util.Collection;

import com.hzerai.db.prototypes.DynamicBean;

/**
 * @author Habib Zerai
 *
 */
public interface Repository<T> {
	
	Collection<T> findAll();
	long count();
	Collection<T> find();
	Collection<T> findOnePage();
	T findOne();
	T save();
	T delete();
	T update();

}
