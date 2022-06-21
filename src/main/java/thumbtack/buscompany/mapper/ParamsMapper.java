package thumbtack.buscompany.mapper;

import org.mapstruct.*;
import thumbtack.buscompany.model.RequestParams;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR)

public interface ParamsMapper {
    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    @Mapping(target = "fromDate", dateFormat = "yyyy.MM.dd")
    @Mapping(target = "toDate", dateFormat = "yyyy.MM.dd")
    RequestParams paramsFromRequest(String fromDate, String toDate, String busName,
                                                    String fromStation, String toStation, Integer clientId);
}
