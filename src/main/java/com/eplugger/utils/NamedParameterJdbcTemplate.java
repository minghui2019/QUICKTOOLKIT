package com.eplugger.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NamedParameterJdbcTemplate {

    private Connection connection;

    public NamedParameterJdbcTemplate(Connection connection) {
        // 建立数据库连接
        this.connection = connection;
    }

    public <T> List<T> queryForList(String sql, Map<String, Object> params, Class<T> requiredType) {
        return query(sql, params, new BeanPropertyRowMapper<>(requiredType));
    }

    public <T> List<T> query(String sql, Map<String, Object> params, RowMapper<T> rowMapper) {
        try {
            // 使用 NamedParameterStatement 进行预编译
            NamedParameterStatement namedParameterStatement = new NamedParameterStatement(connection, sql);
            setNamedParameters(namedParameterStatement, params);

            List<T> result = new ArrayList<>();
            int rowNum = 0;
            log.debug("Executing SQL: " + ((LoggableStatement) namedParameterStatement.getPreparedStatement()).getQueryString());
            try (ResultSet resultSet = namedParameterStatement.getPreparedStatement().executeQuery()) {
                while (resultSet.next()) {
                    T row = rowMapper.mapRow(resultSet, rowNum++);
                    result.add(row);
                }
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }
    }

    private void setNamedParameters(NamedParameterStatement namedParameterStatement, Map<String, Object> params) throws SQLException {
        if (params != null) {
            // 设置命名参数的值
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                namedParameterStatement.setObject(entry.getKey(), entry.getValue());
            }
        }
    }
}
