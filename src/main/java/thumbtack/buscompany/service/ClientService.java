package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.UserType;
import thumbtack.buscompany.repository.UserRepository;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.HttpServletResponse;

@Service
@AllArgsConstructor
public class ClientService {
    UserRepository userRepository;
    SessionService sessionService;
    UserDao userDao;
    UserMapper userMapper;

    public UserResponse register(ClientRegisterRequest request, HttpServletResponse response) {
        Client client = userMapper.clientFromRegisterRequest(request);
        client.loginToLowerCase();
        client.phoneNumberFormat();
        client.setUserType(UserType.CLIENT);
        userDao.insert(client);
        return sessionService.login(userMapper.userToLoginRequest(client), response);
    }
}
