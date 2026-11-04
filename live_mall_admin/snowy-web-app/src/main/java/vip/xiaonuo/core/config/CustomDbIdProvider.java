package vip.xiaonuo.core.config;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.noear.solon.annotation.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库id选择器，用于Mapper.xml中
 * MyBatis可以根据不同的数据库厂商执行不同的语句
 *
 * @author xuyuxiang
 * @date 2022/1/8 2:16
 */
@Component
public class CustomDbIdProvider implements DatabaseIdProvider {

    @Override
    public String getDatabaseId(DataSource dataSource) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            String url = conn.getMetaData().getURL().toLowerCase();
            if (url.contains("jdbc:oracle")) {
                return "oracle";
            } else if (url.contains("jdbc:postgresql")) {
                return "pgsql";
            } else if (url.contains("jdbc:mysql")) {
                return "mysql";
            } else if (url.contains("jdbc:dm")) {
                return "dm";
            } else if (url.contains("jdbc:kingbase")) {
                return "kingbase";
            }  else {
                return "mysql";
            }
        } finally {
            conn.close();
        }
    }
}

