/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tomoare.mybatis.type;

import com.tomoare.mybatis.constant.DbCodeConstant;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 *
 * @author tomoare
 * @param <E> Enum
 */
public class EnumValueTypeHandler<E extends Enum<E> & DbCodeConstant> extends BaseTypeHandler<E> {

    private Class<E> type;

    public EnumValueTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if (jdbcType == null) {
            ps.setString(i, ((DbCodeConstant) parameter).getCodeValue());
        } else {
            ps.setObject(i, ((DbCodeConstant) parameter).getCodeValue(), jdbcType.TYPE_CODE); // TODO
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        return getEnum(s);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        return getEnum(s);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        return getEnum(s);
    }

    /**
     *
     * @param value
     * @return
     */
    @Nullable
    private E getEnum(@Nullable String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        List<E> enums = EnumUtils.getEnumList(type);
        if (enums == null || enums.isEmpty()) {
            return null;
        }

        for (E enm : enums) {
            if (((DbCodeConstant) enm).getCodeValue().equals(value)) {
                return enm;
            }
        }
        return null;
    }
}
