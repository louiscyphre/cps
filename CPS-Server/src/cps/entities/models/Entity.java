package cps.entities.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Entity implements Serializable {
	private static final long serialVersionUID = 1L;

	public static Entity buildFromQueryResult(ResultSet rs) throws SQLException {
		throw new UnsupportedOperationException();
	}
}
