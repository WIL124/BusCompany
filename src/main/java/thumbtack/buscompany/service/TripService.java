package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.SessionDao;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.ParamsMapper;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.TripRequest;
import thumbtack.buscompany.response.TripResponse;

import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService extends ServiceBase {
    private TripMapper tripMapper;
    private TripDao tripDao;
    private ParamsMapper paramsMapper;

    public TripResponse create(TripRequest tripRequest, String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        Trip trip = tripMapper.tripFromRequest(tripRequest);
        createAndSetTripDays(trip, tripRequest);
        tripDao.insert(trip);
        return tripMapper.tripToResponse(trip);
    }

    public TripResponse update(int tripId, TripRequest tripRequest, String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        Trip trip = getTripOrThrow(tripId);
        tripMapper.updateTrip(trip, tripRequest);
        createAndSetTripDays(trip, tripRequest);
        try {
            tripDao.update(trip);
        } catch (RuntimeException e) { //TODO fix
            throw new ServerException(ErrorCode.NOT_FOUND, "tripId");
        }
        return tripMapper.tripToResponse(trip);
    }

    public ResponseEntity<Void> deleteTrip(int tripId, String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        getTripOrThrow(tripId);
        if (tripDao.deleteTrip(tripId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else throw new ServerException(ErrorCode.TRIP_NOT_FOUND, "tripId");
    }

    public TripResponse getTrip(int tripId, String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        Trip trip = getTripOrThrow(tripId);
        return tripMapper.tripToResponse(trip);
    }


    public TripResponse approve(int tripId, String sessionId) throws ServerException {
        getAdminOrThrow(sessionId);
        Trip trip = getTripOrThrow(tripId);
        tripDao.approve(trip);
        trip.setApproved(true);
        return tripMapper.tripToResponse(trip);
    }

    public List<TripResponse> getTripsWithParams(String fromStation, String toStation, String busName, String fromDate, String toDate, String sessionId) throws ServerException {
        User user = getUserOrThrow(sessionId);
        RequestParams params = paramsMapper.paramsFromRequest(fromDate, toDate, busName, fromStation, toStation, user.getId());
        List<Trip> tripList = tripDao.getTripsWithParams(user, params);
        filterTripDays(tripList, params);
        List<TripResponse> responses = tripMapper.tripResponseListFromTrips(tripList);
        if (user instanceof Client) {
            responses.forEach(tripResponse -> tripResponse.setApproved(null));
        }
        return responses;
    }

    private Trip getTripOrThrow(Integer tripId) throws ServerException {
        return tripDao.getTrip(tripId).orElseThrow(() -> new ServerException(ErrorCode.TRIP_NOT_FOUND, "tripId"));
    }

    private void filterTripDays(List<Trip> tripList, RequestParams params) {
        if (params != null) {
            for (Trip trip : tripList) {
                if (params.getFromDate() != null) {
                    trip.setTripDays(trip.getTripDays().parallelStream().filter(tripDay -> tripDay.getDate().isAfter(params.getFromDate())).collect(Collectors.toList()));
                }
                if (params.getToDate() != null) {
                    trip.setTripDays(trip.getTripDays().parallelStream().filter(tripDay -> tripDay.getDate().isBefore(params.getToDate())).collect(Collectors.toList()));
                }
            }
        }
    }

    private void createAndSetTripDays(Trip trip, TripRequest request) throws ServerException {
        if (trip.getSchedule() != null) {
            List<LocalDate> dates = createDatesFromSchedule(trip.getSchedule());
            createTripDays(trip, dates);
        } else {
            createTripDays(trip, tripMapper.datesFromString(request.getDates()));
        }
    }

    private void createTripDays(Trip trip, List<LocalDate> dates) {
        List<TripDay> tripDays = new ArrayList<>();
        for (LocalDate date : dates) {
            tripDays.add(new TripDay(date, null, new ArrayList<>()));
        }
        tripDays.forEach(tripDay -> tripDay.setTrip(trip));
        trip.setTripDays(tripDays);
    }

    private List<LocalDate> createDatesFromSchedule(Schedule schedule) throws ServerException {
        List<LocalDate> totalDates = new ArrayList<>();
        LocalDate start = schedule.getFromDate();
        LocalDate end = schedule.getToDate();
        for (LocalDate current = start; !current.isAfter(end); current = current.plusDays(1)) {
            totalDates.add(current);
        }
        if (totalDates.isEmpty()) {
            throw new ServerException(ErrorCode.WRONG_DATE_INTERVAL, "from");
        }
        Predicate<LocalDate> filter = createFilterFromPeriod(schedule.getPeriod());
        return totalDates.stream().filter(filter).collect(Collectors.toList());
    }

    private Predicate<LocalDate> createFilterFromPeriod(String period) {
        switch (period.toLowerCase()) {
            case ("even"):
                return localDate -> localDate.getDayOfMonth() % 2 == 0;
            case ("odd"):
                return localDate -> localDate.getDayOfMonth() % 2 == 1;
            case ("daily"):
                return localDate -> true;
        }
        List<String> stringList = Arrays.asList(period.trim().split(","));
        if (NumberUtils.isParsable(stringList.get(0))) {
            return localDate -> stringList.parallelStream()
                    .map(Integer::parseInt)
                    .anyMatch(integer -> integer == localDate.getDayOfMonth());
        } else {
            return dayOfWeekFilter(stringList);
        }
    }

    private Predicate<LocalDate> dayOfWeekFilter(List<String> strings) {
        return localDate -> strings.parallelStream()
                .map(str ->  Weekday.valueOf(str).getDayOfWeek())
                .anyMatch(dayOfWeek -> dayOfWeek == localDate.getDayOfWeek());
    }

}
