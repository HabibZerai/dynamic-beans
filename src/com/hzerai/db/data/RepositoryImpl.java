/**
 * 
 */
package com.hzerai.db.data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.hzerai.db.data.interfaces.AbstractRepositoryMetaData;
import com.hzerai.db.data.interfaces.Repository;
import com.hzerai.db.metadata.ConnectionManager;
import com.hzerai.db.prototypes.DynamicBean;

/**
 * @author Habib Zerai
 *
 */
public class RepositoryImpl extends AbstractRepositoryMetaData implements Repository {

	@Override
	public Collection<?> findAll() {
		List<DynamicBean> result = new ArrayList<>();
		try (Statement st = ConnectionManager.getInstance().getConnection().createStatement();
				ResultSet rs = st.executeQuery(super.selectQuery)) {
			while (rs.next()) {
				DynamicBean db = DynamicBean.newInstance(className);
				((Set<String>) db.get("keys")).forEach(key -> {
					try {
						db.set(key, rs.getObject(key));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				result.add(db);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Collection<?> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<?> findOnePage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object findOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object save() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean delete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object update() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		try (Statement st = ConnectionManager.getInstance().getConnection().createStatement();
				ResultSet rs = st.executeQuery(super.countQuery)) {
			rs.next();
			return rs.getLong(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
