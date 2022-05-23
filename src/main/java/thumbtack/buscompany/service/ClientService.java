package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.UserDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.UserMapper;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.User;
import thumbtack.buscompany.request.ClientRegisterRequest;
import thumbtack.buscompany.request.ClientUpdateRequest;
import thumbtack.buscompany.response.UserResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClientService {
    // REVU private все
    SessionService sessionService;
    UserDao userDao;
    UserMapper userMapper;

    public Client register(ClientRegisterRequest request) throws ServerException {
        // REVU при таком подходе может быть, что в момент userDao.getUserByLogin
        // такого логина нет, а при userDao.insert уже есть
        // мы же в многопользовательской среде
        // не надо проверять. Надо просто userDao.insert и ловить
        // исключение. Если оно будет иметь в качестве cause
        // MySQLIntegrityConstraintViolationException: Duplicate entry
        // значит, этот логин уже используется
        if (userDao.getUserByLogin(request.getLogin()).isPresent()) {
            throw new ServerException(ErrorCode.LOGIN_ALREADY_EXISTS, "login");
        }
        Client client = userMapper.clientFromRegisterRequest(request);
        client.phoneNumberFormat();
        userDao.insert(client);
        return client;
    }

    public List<UserResponse> getAllClients() {
        return userDao.getAllClients().stream()
                .map(client -> userMapper.clientToClientResponse(client)).collect(Collectors.toList());
    }

    public UserResponse updateClient(String sessionId, ClientUpdateRequest request) throws ServerException {
        User user = sessionService.getUserBySessionId(sessionId);
        if (!request.getOldPassword().equals(user.getPassword())) {
            throw new ServerException(ErrorCode.DIFFERENT_PASSWORDS, "oldPassword");
        }
        Client client = (Client) user;
        updateClientModel(client, request);
        client.phoneNumberFormat();
        userDao.updateClient(client);
        sessionService.updateTime(sessionId);
        return userMapper.clientToClientResponse(client);
    }

    private void updateClientModel(Client client, ClientUpdateRequest request) {
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPatronymic(request.getPatronymic());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setPassword(request.getNewPassword());
    }

}
