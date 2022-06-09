package thumbtack.buscompany.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR)
public interface PlaceMapper {
    ChoosingPlaceResponse responseFromRequestAndTicket(ChoosingPlaceRequest request, String ticket);
}
