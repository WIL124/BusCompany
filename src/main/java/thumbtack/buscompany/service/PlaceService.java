package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.model.Trip;
import thumbtack.buscompany.request.ChoosingPlaceRequest;
import thumbtack.buscompany.response.ChoosingPlaceResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {
    PlaceDao placeDao;

    public List<Integer> getFreePlaces(Trip trip, LocalDate date) {
        int totalPlaces = trip.getBus().getPlaceCount();
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= totalPlaces; i++) {
            places.add(i);
        }
        places.removeAll(placeDao.getBookedPlaces(trip, date));
        return places;
    }

    public ChoosingPlaceResponse choicePlace(ChoosingPlaceRequest request) {
        placeDao.updatePlace(request);
        return null;
    }
}
