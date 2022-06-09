package thumbtack.buscompany.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import thumbtack.buscompany.dao.TripDao;
import thumbtack.buscompany.exception.ErrorCode;
import thumbtack.buscompany.exception.ServerException;
import thumbtack.buscompany.mapper.TripMapper;
import thumbtack.buscompany.model.*;
import thumbtack.buscompany.request.TripRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TripService {
    private TripMapper tripMapper;
    private TripDao tripDao;

    public Trip create(TripRequest tripRequest) throws ServerException {

        Trip trip = tripMapper.tripFromRequest(tripRequest);
        if (trip.getSchedule() != null) {
            List<LocalDate> dates = createDatesFromSchedule(trip.getSchedule());
            trip.setDates(dates);
        }
        tripDao.insert(trip);
        return trip;
    }

    public Trip update(int tripId, TripRequest body) throws ServerException {
        Trip updatedTrip = tripMapper.tripFromRequest(body);
        updatedTrip.setTripId(tripId);
        if (updatedTrip.getSchedule() != null) {
            List<LocalDate> dates = createDatesFromSchedule(updatedTrip.getSchedule());
            updatedTrip.setDates(dates);
        }
        try {
            tripDao.update(updatedTrip);
        } catch (RuntimeException e) {
            throw new ServerException(ErrorCode.NOT_FOUND, "tripId");
        }
        return updatedTrip;
    }

    public boolean deleteTrip(int tripId) {
        return tripDao.deleteTrip(tripId);
    }

    public Trip getTrip(int tripId) throws ServerException {
        return tripDao.getTrip(tripId).orElseThrow(() -> new ServerException(ErrorCode.TRIP_NOT_FOUND, "tripId"));
    }

    public Trip approve(int tripId) throws ServerException {
        if (tripDao.approve(tripId)) {
            return tripDao.getTrip(tripId).orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "tripId"));
        } else throw new ServerException(ErrorCode.NOT_FOUND, "tripId"); //TODO fix exceptions
    }

    public List<Trip> getTripsWithParams(User user, RequestParams params) {
        List<Trip> tripList = tripDao.getTripsWithParams(user, params);
        if (params != null) {
            for (Trip trip : tripList) {
                if (params.getFromDate() != null) {
                    trip.setDates(trip.getDates().parallelStream().filter(localDate -> localDate.isAfter(params.getFromDate())).collect(Collectors.toList()));
                }
                if (params.getToDate() != null) {
                    trip.setDates(trip.getDates().parallelStream().filter(localDate -> localDate.isBefore(params.getToDate())).collect(Collectors.toList()));
                }
            }
        }
        if (user instanceof Client) {
            tripList.forEach(e -> e.setApproved(null));
        }
        return tripList;
    }

    private List<LocalDate> createDatesFromSchedule(Schedule schedule) throws ServerException {
        List<LocalDate> totalDates = new ArrayList<>();
        LocalDate start = schedule.getFromDate();
        LocalDate end = schedule.getToDate();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        if (totalDates.isEmpty()) {
            throw new ServerException(ErrorCode.WRONG_DATE_INTERVAL, "from");
        }
        Predicate<LocalDate> filter = createFilterFromPeriod(schedule.getPeriod());
        return totalDates.parallelStream().filter(filter).collect(Collectors.toList());
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
                .map(str -> Weekday.valueOf(str).getDayOfWeek())
                .anyMatch(dayOfWeek -> dayOfWeek == localDate.getDayOfWeek());
    }

}
