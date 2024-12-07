package org.hsbc.mapper;
import org.apache.ibatis.annotations.*;
import org.hsbc.entity.Incident;

import java.util.List;

@Mapper
public interface IncidentMapper {
    @Insert("INSERT INTO incident(description) VALUES(#{description})")
    public int insert(Incident incident);

    @Update("UPDATE incident SET description = #{description} WHERE id = #{id}")
    public int update(Incident incident);

    @Delete("DELETE FROM incident WHERE id = #{id}")
    int delete(Long id);

    @Select("SELECT * FROM incident WHERE id = #{id}")
    Incident selectById(Long id);

    @Select("SELECT * FROM incident")
    List<Incident> selectAll();
}