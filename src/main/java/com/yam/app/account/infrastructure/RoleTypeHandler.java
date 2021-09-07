package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.Role;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(RoleTypeHandler.class)
public final class RoleTypeHandler implements TypeHandler<Role> {

    @Override
    public void setParameter(PreparedStatement ps, int i,
        Role parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public Role getResult(ResultSet rs, String columnName) throws SQLException {
        return Role.findRole(rs.getString(columnName));
    }

    @Override
    public Role getResult(ResultSet rs, int columnIndex) throws SQLException {
        return Role.findRole(rs.getString(columnIndex));
    }

    @Override
    public Role getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Role.findRole(cs.getString(columnIndex));
    }
}
