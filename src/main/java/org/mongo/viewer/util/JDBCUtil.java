package org.mongo.viewer.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Class JDBCUtil.
 */
public final class JDBCUtil {
    private static final String NOT_FOUND = "not found.";
    private static Log log = LogFactory.getLog(JDBCUtil.class);
    private static Properties config = PropertyLoader.loadProperties("config");

    /**
     * Gets the connection.
     * 
     * @return the connection
     * @throws SQLException
     *             the SQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public static Connection getConnection() throws SQLException,
            ClassNotFoundException {
        Connection conn = null;
        String driver = config.getProperty("jdbc.driver");
        String connectionURL = config.getProperty("jdbc.url");
        try {
            log.debug("Loading driver " + driver);
            Class.forName(driver);
            conn = getConnection(connectionURL);

        } catch (SQLException e) {
            String message = e.getMessage();
            log.debug("Message = " + message);
            if (message.endsWith(NOT_FOUND)) {
                log.info("Looks like the DB is not properly setup");
                // Create a new DB
                connectionURL = connectionURL + ";create=true";
                conn = getConnection(connectionURL);
                createDefaultTables(conn);
            } else {
                throw e;
            }
        } catch (ClassNotFoundException e) {
            log.error("Problem with getting JDBC Connection", e);

            throw e;
        }

        return conn;
    }

    private static Connection getConnection(String connectionURL)
            throws SQLException {
        log.debug("Connecting to " + connectionURL);
        return DriverManager.getConnection(connectionURL);
    }

    /**
     * Creates the default tables.
     * 
     * @param conn
     * @throws SQLException
     */
    private static void createDefaultTables(Connection conn)
            throws SQLException {
        // create table scripts
        String sql = config.getProperty("create.script");
        log.info("Executing " + sql);
        Statement stmt = conn.createStatement();
        stmt.execute(sql);

    }

    /**
     * Execute query.
     * 
     * @param sql
     *            the sql
     * @return the list
     * @throws Exception
     *             the exception
     */
    public static List<Map<String, String>> executeQuery(String sql)
            throws Exception {
        List<Map<String, String>> results = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            log.info("Executing " + sql);
            rs = stmt.executeQuery(sql);

            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            results = new ArrayList<Map<String, String>>();

            while (rs.next()) {
                Map<String, String> row = new HashMap<String, String>();
                for (int column = 1; column <= count; column++) {
                    String name = rsmd.getColumnName(column);
                    String value = rs.getString(column);
                    row.put(name, value);
                }
                results.add(row);
            }

        } catch (Exception e) {
            log.error("Problem while firing sql " + sql, e);
            throw e;
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Ignore
                }
            }
        }

        return results;
    }

    
    /**
     * Execute update.
     *
     * @param sql the sql
     * @return the int
     * @throws Exception the exception
     */
    public static int executeUpdate(String sql) throws Exception {
        int results = -1;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            log.info("Executing " + sql);
            results = stmt.executeUpdate(sql);

        } catch (Exception e) {
            log.error("Problem while executing sql " + sql, e);
            throw e;
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Ignore
                }
            }
        }

        return results;
    }
}
