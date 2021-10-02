package com.yam.app.common.configuration;

import com.yam.app.common.EntityStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(EntityStatusTypeHandler.class)
public final class EntityStatusTypeHandler implements TypeHandler<EntityStatus> {

    @Override
    public void setParameter(PreparedStatement ps, int i,
        EntityStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public EntityStatus getResult(ResultSet rs, String columnName) throws SQLException {
        return EntityStatus.findStatus(rs.getString(columnName));
    }

    @Override
    public EntityStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        return EntityStatus.findStatus(rs.getString(columnIndex));
    }

    @Override
    public EntityStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return EntityStatus.findStatus(cs.getString(columnIndex));
    }
}
