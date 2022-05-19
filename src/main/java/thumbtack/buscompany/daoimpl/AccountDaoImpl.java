package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import thumbtack.buscompany.dao.AccountDao;
import thumbtack.buscompany.repository.UserRepository;

@Repository
@AllArgsConstructor
public class AccountDaoImpl implements AccountDao {
    UserRepository userRepository;
    @Override
    public boolean deactivateUser(Integer user_id) {
        return userRepository.deactivate(user_id);
    }
}
