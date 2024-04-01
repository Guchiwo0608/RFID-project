package sql;

import java.sql.*;
import java.util.*;

public class DatabaseDriver {

    // 接続文字列
    private String url = "jdbc:postgresql://localhost:5432/rfid-analysis";

    private Connection conn = null;
    private Statement stmt = null;

    public DatabaseDriver(String tableName) throws Exception {
        String user = "guchiwo";
        String password = "norireo0608";
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }
    };

    public ResultSet getData(String table) throws Exception {
        String sql = String.format("select * from %s", table);
        ResultSet rs = null;
        try {
            rs = this.stmt.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }
        return rs;
    }

    public void insertData(List<Map<String, Object>> dataList, String tableName) throws Exception {
        PreparedStatement preparedStmt = this.conn.prepareStatement(tableName);
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(String.format("insert into %s values(", tableName));
        int i = 0;
        for (i = 0; i < dataList.size() - 1; i++) {
            sqlBuilder.append("?, ");
        }
        i = 1;
        sqlBuilder.append("?)");
        for (Iterator<Map<String, Object>> it = dataList.iterator(); it.hasNext();) {
            Map<String, Object> dataJson = it.next();
            dataJson.forEach((k, v) -> {
                if (((Object) v).getClass().getSimpleName() == "String") {
                    try {
                        preparedStmt.setString(i, (String) v);
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (((Object) v).getClass().getSimpleName() == "Int") {
                    try {
                        preparedStmt.setInt(i, (int) v);
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (((Object) v).getClass().getSimpleName() == "Double")
                    try {
                        preparedStmt.setDouble(i, (double) v);
                    } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
            });
            i++;
        }

    }

}
