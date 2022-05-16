/**
 * 
 */
package com.hzerai.db.data.interfaces;

import java.util.HashMap;
import java.util.Map;

import com.hzerai.db.data.RepositoryImpl;
import com.hzerai.db.prototypes.Utility;

/**
 * @author Habib Zerai
 *
 */
public class RepositoryFactory {

	private static final Map<String, Repository> repos = new HashMap<>();

	public synchronized static Repository getRepository(String className) {
		if (repos.containsKey(className)) {
			return repos.get(className);
		}
		return buildRepo(className);
	}

	/**
	 * @param className
	 * @return
	 */
	private static Repository buildRepo(String className) {
		RepositoryImpl repo = new RepositoryImpl();
		repo.className = className;
		repo.selectQuery = Utility.SELECT_QUERY_REGISTER.get(className);
		repo.countQuery = Utility.COUNT_QUERY_REGISTER.get(className);
		repos.put(className, repo);
		return repo;
	}

}
