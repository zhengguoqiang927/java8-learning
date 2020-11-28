package com.zhengguoqiang.spi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlJdbcDemo {
    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement pstm = null;
        try {
            //1.注册驱动
            Class.forName("com.mysql.jdbc.Driver");

            //2.获取连接
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");

            //3.创建查询语句
            pstm = connection.prepareStatement("select  * from test");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
