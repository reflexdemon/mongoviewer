package org.mongo.viewer.main;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.mongo.viewer.util.LoggingUtils;
import org.mongo.viewer.util.MongoUtil;
import org.mongo.viewer.vo.DataBaseInfo;

import com.mongodb.DB;

public class MainTester {
    private static DataBaseInfo prd = new DataBaseInfo(
            "mongo00prd.bay.cbeyond.net", 27017, "syslog", "syslogadmin",
            "sys_adm1n");

    private static DataBaseInfo atst = new DataBaseInfo(
            "mongo00atst.dev.cbeyond.net", 27017, "syslog", "syslogadmin",
            "sys_adm1n");

    private static final String[] fields = { "timestamp", "type",
            "accountNumber", "loginId", "hostName", "message", "version",
            "clientIp" };

    public static void main(String[] args) {

        try {
            LoggingUtils.initDefaultLogging();
            DB db = MongoUtil.getDB(atst);
            Jongo jongo = new Jongo(db);
            MongoCollection sysLog = jongo.getCollection("SYSLOG_06_2014");
            Iterable<Object> all = sysLog
                    .find("{ $or : [ {type:'REPLOGIN.user'}, {type:'REPLOGIN.account'}]}")
                    .as(Object.class);
            Iterator<Object> itr = all.iterator();
            for (String field : fields) {
                System.out.print(field + "\t");
            }
            System.out.println();
            while (itr.hasNext()) {
                Object result = itr.next();
                for (String field : fields) {
                    System.out.print(BeanUtilsBean.getInstance().getProperty(
                            result, field)
                            + "\t");
                }
                System.out.println();

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
