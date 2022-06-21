package thumbtack.buscompany.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.model.Bus;

import java.util.List;

@Mapper
public interface BusRepository {
    @Select("SELECT busName, placeCount FROM buses")
    List<Bus> getAll();

    @Select("SELECT busName, placeCount FROM buses WHERE busName = #{busName}")
    Bus get(@Param("busName") String busName);
}
