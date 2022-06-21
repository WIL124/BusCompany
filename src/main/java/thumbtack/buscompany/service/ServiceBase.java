package thumbtack.buscompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.Admin;
import thumbtack.buscompany.model.Client;
import thumbtack.buscompany.model.Order;
import thumbtack.buscompany.model.User;

@Service
@Transactional(rollbackFor = ServerException.class)
public abstract class ServiceBase {
    @Autowired
    private SessionDao sessionDao;
    protected static final String JAVASESSIONID = "JAVASESSIONID";

    protected Admin getAdminOrThrow(String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        if (!(user instanceof Admin)) {
            throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
        }
        sessionDao.updateTime(sessionId);
        return (Admin) user;
    }

    protected Client getClientOrThrow(String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        if (!(user instanceof Client)) {
            throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
        }
        sessionDao.updateTime(sessionId);
        return (Client) user;
    }

    protected User getUserOrThrow(String sessionId) throws ServerException {
        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
        sessionDao.updateTime(sessionId);
        return user;
    }

    protected Order getClientsOrderOrThrow(Client client, int orderId) throws ServerException {
        return client.getOrders().stream().filter(order -> order.getOrderId() == orderId)
                .findFirst().orElseThrow(() -> new ServerException(ErrorCode.ORDER_NOT_FOUND, "orderId"));
    }
//    protected <T extends User> T getUserAndCastOrThrow(Class<T> t,String sessionId) throws ServerException {
//        User user = sessionDao.getSessionById(sessionId).orElseThrow(() -> new ServerException(ErrorCode.SESSION_NOT_FOUND, "JAVASESSIONID")).getUser();
//        if (t.isInstance(user)) {
//            throw new ServerException(ErrorCode.DO_NOT_HAVE_PERMISSIONS, "JAVASESSIONID");
//        }
//        sessionDao.updateTime(sessionId);
//        return (T) user;
//    }
}
