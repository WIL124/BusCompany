package thumbtack.buscompany.mapper;

import org.mapstruct.*;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.*;
import thumbtack.buscompany.response.AdminResponse;
import thumbtack.buscompany.response.ClientResponse;

@Mapper(componentModel = "spring", typeConversionPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    Admin adminFromRequest(AdminRegisterRequest request);

    AdminResponse adminToResponse(Admin admin);

    Client clientFromRequest(ClientRegisterRequest request);

    @AfterMapping
    default Client canonizePhone(@MappingTarget Client client) {
        client.canonizePhoneFormat();
        return client;
    }

    ClientResponse clientToResponse(Client client);

    LoginRequest userToLoginRequest(User user);

    @Mapping(target = "password", source = "newPassword")
    void updateClientFromRequest(ClientUpdateRequest request, @MappingTarget Client client);

    @Mapping(target = "password", source = "newPassword")
    void updateAdminFromRequest(AdminUpdateRequest request, @MappingTarget Admin admin);
}
