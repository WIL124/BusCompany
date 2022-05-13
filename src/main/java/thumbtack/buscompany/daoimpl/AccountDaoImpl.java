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
    public void delete(Integer user_id) {
        userRepository.deactivate(user_id);
    }
}
