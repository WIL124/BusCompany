package thumbtack.buscompany.daoimpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thumbtack.buscompany.dao.TripDayDao;
import thumbtack.buscompany.model.TripDay;
import thumbtack.buscompany.repository.TripDayRepository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Transactional
public class TripDayDaoImpl implements TripDayDao {
    TripDayRepository tripDayRepository;
    @Override
    public Optional<TripDay> getTripDayByTripIdAndDate(Integer tripId, LocalDate date) {
        return Optional.ofNullable(tripDayRepository.getTripDayByTripIdAndDate(tripId, date));
    }
}
