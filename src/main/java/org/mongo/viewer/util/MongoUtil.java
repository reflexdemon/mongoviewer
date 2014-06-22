package org.mongo.viewer.util;

import java.net.UnknownHostException;

import org.mongo.viewer.vo.DataBaseInfo;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoUtil {
    public static DB getDB(DataBaseInfo info) throws UnknownHostException {
        Mongo mongoClient = new Mongo(info.getHost(), info.getPort());
        DB db = mongoClient.getDB(info.getDatabase());
        boolean auth = db.authenticate(info.getUser(), info.getPassword()
                .toCharArray());
        if (auth) {
            return db;
        } else {
            throw new RuntimeException("Cannot authenticate user "
                    + info.getUser());
        }

    }
}
