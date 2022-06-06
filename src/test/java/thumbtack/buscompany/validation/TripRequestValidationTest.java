package thumbtack.buscompany.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import thumbtack.buscompany.request.ScheduleDto;
import thumbtack.buscompany.request.TripRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static thumbtack.buscompany.TestUtils.*;

public class TripRequestValidationTest {
    public Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    public static Stream<Arguments> validRequest() {
        return Stream.of(
                Arguments.arguments(createTripRequestWithSchedule()),
                Arguments.arguments(createTripRequestWithDates())
        );
    }

    @ParameterizedTest
    @MethodSource("validRequest")
    public void validRequestTest(TripRequest tripRequest) {
        Set<ConstraintViolation<TripRequest>> violations = validator.validate(tripRequest);
        assertTrue(violations.isEmpty());
    }

    public static Stream<Arguments> invalidRequest() {
        ScheduleDto invalidDto = createScheduleDto();
        invalidDto.setFromDate(null);
        return Stream.of(
                Arguments.arguments(new TripRequest("", "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest(null, "Omsk", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "", "Moscow", "04:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "", "04:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", null, "04:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "26:30", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "v tri", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", null, "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "", "12:00", 100L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 0L, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", null, createScheduleDto(), null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, invalidDto, null)),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, List.of("123"))),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, List.of("15.03.1200"))),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, List.of("2000.13.12"))),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, new ArrayList<>())),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, List.of("2000.12.55"))),
                Arguments.arguments(new TripRequest("Volvo", "Omsk", "Moscow", "04:30", "12:00", 100L, null, List.of("2000/12/05")))
                );
    }

    @ParameterizedTest
    @MethodSource("invalidRequest")
    public void invalidRequestTest(TripRequest tripRequest) {
        Set<ConstraintViolation<TripRequest>> violations = validator.validate(tripRequest);
        assertFalse(violations.isEmpty());
    }
}
