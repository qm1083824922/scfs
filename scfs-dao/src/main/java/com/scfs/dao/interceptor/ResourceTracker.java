package com.scfs.dao.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Resource management facilities for IO resources.
 */
public class ResourceTracker {

	/**
	 * A class which knows how to tidy up another object
	 */
	private interface Cleanup<T> {
		public void close(T object) throws Exception;
	}

	private static final Cleanup<ResultSet> CLEAN_JAVA_SQL_RESULTSET = new Cleanup<ResultSet>() {
		public void close(final ResultSet rs) throws SQLException {
			rs.close();
		}
	};
	private static final Cleanup<Statement> CLEAN_JAVA_SQL_STATEMENT = new Cleanup<Statement>() {
		public void close(final Statement s) throws SQLException {
			s.close();
		}
	};
	private static final Cleanup<Connection> CLEAN_JAVA_SQL_CONNECTION = new Cleanup<Connection>() {
		public void close(final Connection c) throws SQLException {
			c.close();
		}
	};
	private static final Cleanup<Reader> CLEAN_JAVA_IO_READER = new Cleanup<Reader>() {
		public void close(final Reader reader) throws IOException {
			reader.close();
		}
	};
	private static final Cleanup<Writer> CLEAN_JAVA_IO_WRITER = new Cleanup<Writer>() {
		public void close(final Writer writer) throws IOException {
			writer.close();
		}
	};
	private static final Cleanup<InputStream> CLEAN_JAVA_INPUT_STREAM = new Cleanup<InputStream>() {
		public void close(final InputStream in) throws IOException {
			in.close();
		}
	};
	private static final Cleanup<OutputStream> CLEAN_JAVA_OUTPUT_STREAM = new Cleanup<OutputStream>() {
		public void close(final OutputStream out) throws IOException {
			out.close();
		}
	};

	private final String description;

	/**
	 * Creates a new instance of ResourceTracker.
	 *
	 * @param desc
	 *            A description of the tracker, useful for logging
	 */
	public ResourceTracker(final String desc) {
		this.description = desc;
	}

	private final ArrayList<Object> objects = new ArrayList<Object>();

	/**
	 * Attaches new resource to the tracker. This can be used for any resource.
	 * Built in implementation handles basic JDBC types as is
	 */
	private void attach(final Object object, final Cleanup<?> cleaner) {
		objects.add(object);
		objects.add(cleaner);
	}

	/**
	 * Attaches any JDBC Statement to this tracker
	 *
	 * @param stmt
	 */
	public void attach(final Statement stmt) {
		attach(stmt, CLEAN_JAVA_SQL_STATEMENT);
	}

	/**
	 * Attaches a JDBC ResultSet to this tracker
	 *
	 * @param rs
	 */
	public void attach(final ResultSet rs) {
		attach(rs, CLEAN_JAVA_SQL_RESULTSET);
	}

	/**
	 * Attach a connection to the tracker. The default clean up is to close the
	 * Connection. If using a connection pool, it may not be a good idea to call
	 * this. Try attach(Connection, ResourcePool) instead.
	 *
	 * @param conn
	 */
	public void attach(final Connection conn) {
		attach(conn, CLEAN_JAVA_SQL_CONNECTION);
	}

	/**
	 * Attaches Reader to this tracker
	 *
	 * @param r
	 */
	public void attach(final Reader r) {
		attach(r, CLEAN_JAVA_IO_READER);
	}

	/**
	 * Attaches Writer to this tracker
	 *
	 * @param w
	 */
	public void attach(final Writer w) {
		attach(w, CLEAN_JAVA_IO_WRITER);
	}

	/**
	 * Attaches an input stream to this tracker
	 *
	 * @param in
	 */
	public void attach(final InputStream in) {
		attach(in, CLEAN_JAVA_INPUT_STREAM);
	}

	/**
	 * Attaches an input stream to this tracker
	 *
	 * @param out
	 */
	public void attach(final OutputStream out) {
		attach(out, CLEAN_JAVA_OUTPUT_STREAM);
	}

	/**
	 * Clears resources, does not receive exceptions. This should be used inside
	 * finally block.
	 */
	public void clear() {
		while (!objects.isEmpty()) {
			try {
				close();
			} catch (final Exception lost) {
			}
		}
	}

	/**
	 * close resources, returns first error to caller.
	 */
	private void close() throws Exception {
		while (!objects.isEmpty()) {
			closeLastResource();
		}
	}

	/**
	 * Close last resource
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void closeLastResource() throws Exception {
		int index = objects.size() - 1;
		final Cleanup cleaner = (Cleanup<?>) objects.remove(index--);
		final Object resource = objects.remove(index);
		if (resource != null) {
			cleaner.close(resource);
		}
	}

	/**
	 * Returns number of stored resources
	 *
	 * @return
	 */
	public int size() {
		return objects.size() / 2;
	}

	@Override
	public String toString() {
		return "ResourceTracker[" + description + ']';
	}

}