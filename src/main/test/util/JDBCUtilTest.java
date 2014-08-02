package util;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mongo.viewer.util.JDBCUtil;


public class JDBCUtilTest {

    @Test
    public void testGetConnection() {
        try {
            Connection c = JDBCUtil.getConnection();
            Assert.assertNotNull("Checking for connection", c);
        } catch (Throwable e) {
            Assert.fail("Problem while getting JDBC Connection " + e);
        }

    }

    @Test
    public void testExecuteUpdate() {
        try {
            String sql = "INSERT INTO DATABASE_INFO(HOST,DATABASE,USERNAME,PASSWORD,PORT,ID) VALUES ('mongo00atst.dev.cbeyond.net', 'syslog', 'syslogadmin', 'sys_adm1n', 27017, 1)";
            int results = JDBCUtil.executeUpdate(sql);
            Assert.assertNotEquals(-1, results);
        } catch (Throwable e) {
            Assert.fail("Problem while executing query");
        }
    }

    @Test
    public void testExecuteQuery() {
        try {
            String sql = "SELECT * from DATABASE_INFO";
            List<Map<String, String>> results = JDBCUtil.executeQuery(sql);
            System.out.println(results);
            Assert.assertNotNull("Checking for results", results);
        } catch (Throwable e) {
            Assert.fail("Problem while executing query");
        }
    }

    @Test
    public void testExecuteDelete() {
        try {
            String sql = "DELETE FROM DATABASE_INFO WHERE ID = 1";
            int results = JDBCUtil.executeUpdate(sql);
            Assert.assertNotEquals(-1, results);
        } catch (Throwable e) {
            Assert.fail("Problem while executing query");
        }
    }
}
