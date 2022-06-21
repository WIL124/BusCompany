package thumbtack.buscompany.validation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import thumbtack.buscompany.request.ScheduleDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScheduleDtoValidationTest {
    public Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        factory.close();
    }

    public static Stream<Arguments> validDto() {
        return Stream.of(
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "daily")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "daily")),
                Arguments.arguments(new ScheduleDto("2005.07.12", "2006.03.15", "odd")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "even")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "EvEn")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "Sun, Tue, Sat")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "Wed, Fri")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "1,2,5,15")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "4,8,12,29,30"))
        );
    }

    @ParameterizedTest
    @MethodSource("validDto")
    public void testScheduleDtoSuccess(ScheduleDto scheduleDto) {
        Set<ConstraintViolation<ScheduleDto>> violations = validator.validate(scheduleDto);
        assertTrue(violations.isEmpty());
    }

    public static Stream<Arguments> invalidDto() {
        return Stream.of(
                Arguments.arguments(new ScheduleDto("2000.03.16", "2000.03.15", "daily")),
                Arguments.arguments(new ScheduleDto("2000.03.120", "2000.03.150", "daily")),
                Arguments.arguments(new ScheduleDto("12/03/2000", "15/03/2000", "daily")),
                Arguments.arguments(new ScheduleDto("2000/03/12", "2000/03/15", "daily")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "daiily")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "WED")),
                Arguments.arguments(new ScheduleDto("12.13.2000", "15.13.2000", "Wednesday")),
                Arguments.arguments(new ScheduleDto("12.13.2000", "15.13.2000", "Fri, wed")),
                Arguments.arguments(new ScheduleDto("12.03.2000", "15.03.2000", "1,2,35")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "0")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "4,33,2")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "1.2")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "1,2,1")),
                Arguments.arguments(new ScheduleDto("2000.03.12", "2000.03.15", "Fri, Wed, Fri"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidDto")
    public void testInvalidScheduleDto(ScheduleDto scheduleDto) {
        Set<ConstraintViolation<ScheduleDto>> violations = validator.validate(scheduleDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testInvalidScheduleDto() {
        ScheduleDto scheduleDto = new ScheduleDto("2000.03.120", "2000.03.150", "daily");
        Set<ConstraintViolation<ScheduleDto>> violations = validator.validate(scheduleDto);
        assertFalse(violations.isEmpty());
    }
}
