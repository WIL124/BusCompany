package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import thumbtack.buscompany.model.Bus;

import java.util.List;

@Mapper
public interface BusRepository {
    @Select("SELECT * FROM buses")
    List<Bus> getAll();
    @Select("SELECT * FROM buses WHERE busName = #{name}")
    Bus get(@Param("name") String name);
}
