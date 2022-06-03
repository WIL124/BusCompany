package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import thumbtack.buscompany.model.RequestParams;

@Mapper
public interface ParamsMapper {
    @Mapping(target = "fromDate", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "toDate", dateFormat = "yyyy.MM.dd")
    RequestParams paramsFromRequest(String fromDate, String toDate, String busName,
                                                    String fromStation, String toStation, Integer clientId);
}
