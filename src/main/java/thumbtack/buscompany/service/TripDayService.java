package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.TripDayDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.model.TripDay;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TripDayService {
    @Autowired
    private TripDayDao tripDayDao;

    public TripDay getTripDayByTripIdAndDate(Integer tripId, LocalDate date) throws ServerException {
        return tripDayDao.getTripDayByTripIdAndDate(tripId, date).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "tripId, date")); //TODO fix errorCode
    }
}
