package com.eplugger.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// NamedParameterStatement 类
public class NamedParameterStatement {
    private final PreparedStatement preparedStatement;
    private String sql;
    private String parseSql;

    public NamedParameterStatement(Connection connection, String sql) throws SQLException {
        this.sql = sql;
        this.parseSql = parse(sql);
        this.preparedStatement = new LoggableStatement(connection, parseSql);
    }

    public void setObject(String parameterName, Object value) throws SQLException {
        int parameterIndex = findParameterIndex(parameterName);
        preparedStatement.setObject(parameterIndex, value);
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    private String parse(String sql) {
        // 简单的替换，你可能需要根据实际需求改进这部分逻辑
        return sql.replaceAll(":([a-zA-Z_]+)", "?");
    }

    private int findParameterIndex(String parameterName) {
        // 通过正则表达式找到命名参数对应的位置
        Pattern pattern = Pattern.compile(":\\b" + parameterName + "\\b");
        Matcher matcher = pattern.matcher(sql);

        if (matcher.find()) {
            // 获取匹配字符串的起始位置
            int matchStart = matcher.start();

            // 计算在 PreparedStatement 中的参数位置（从1开始）
            int parameterIndex = 1;
            for (int i = 0; i < matchStart; i++) {
                if (parseSql.charAt(i) == '?') {
                    parameterIndex++;
                }
            }

            return parameterIndex;
        } else {
            throw new IllegalArgumentException("Parameter not found: " + parameterName);
        }
    }
}