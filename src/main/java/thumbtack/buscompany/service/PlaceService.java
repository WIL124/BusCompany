package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.PlaceDao;
import thumbtack.buscompany.model.Order;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PlaceService {
    PlaceDao placeDao;

    public List<Integer> getFreePlaces(Order order) {
        int totalPlaces = order.getTrip().getBus().getPlaceCount();
        List<Integer> places = new ArrayList<>();
        for (int i = 1; i <= totalPlaces; i++) {
            places.add(i);
        }
        places.removeAll(placeDao.getBookedPlaces(order.getTrip(), order.getDate()));
        return places;
    }
}
