package ru.ifmo.se.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.validator.ValidatorMessages;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Comparable<Vehicle> {

    @Min(
            value = 1L,
            message = ValidatorMessages.ID_MUST_BE_MORE_ZERO,
            groups = ServiceInputField.class
    )
    private long id;

    @NotBlank(
            message = ValidatorMessages.NAME_MUST_BE_NON_BLANK,
            groups = UserInputField.class
    )
    private String name;

    @NotNull(
            message = ValidatorMessages.COORDS_MUST_BE_NOT_NULL,
            groups = UserInputField.class
    )
    private Coordinates coordinates;

//    @JsonFormat(shape = JsonFormat.Shape.STRING,
//            pattern = "dd-MM-yyyy HH:mm:ss",
//            timezone = "Europe/Moscow"
//    )
    @NotNull(
            message = ValidatorMessages.CREATE_DATE_MUST_BE_NOT_NULL,
            groups = ServiceInputField.class
    )
    private Date creationDate;

    @Min(
            value = 1L,
            message = ValidatorMessages.ENGINE_POWER_MUST_BE_MORE_ZERO,
            groups = UserInputField.class
    )
    private double enginePower;

    @NotNull(
            message = ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_NOT_NULL,
            groups = UserInputField.class
    )
    @Min(
            value = 1L,
            message = ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_MORE_ZERO,
            groups = UserInputField.class
    )
    private Float distanceTravelled;

    @NotNull(
            message = ValidatorMessages.VEHICLE_TYPE_MUST_BE_NOT_NULL,
            groups = UserInputField.class
    )
    private VehicleType type;

    @NotNull(
            message = ValidatorMessages.FUEL_TYPE_MUST_BE_NOT_NULL,
            groups = UserInputField.class
    )
    private FuelType fuelType;

    public interface UserInputField {
    }

    public interface ServiceInputField {
    }

    @Getter
    public enum FieldNames {
        ID("id"),
        NAME("name"),
        X("x"),
        Y("y"),
        CREATION_DATE("creationDate"),
        ENGINE_POWER("enginePower"),
        DISTANCE_TRAVELLED("distanceTravelled"),
        TYPE("type"),
        FUEL_TYPE("fuelType");

        private final String title;

        FieldNames(String title) {
            this.title = title;
        }
    }

    @Override
    public int compareTo(Vehicle vehicle) {
        Double thisDigit = enginePower * distanceTravelled;
        Double vehicleDigit = vehicle.enginePower * vehicle.distanceTravelled;
        return thisDigit.compareTo(vehicleDigit);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehicle{")
                .append("id=").append(id)
                .append(", name='").append(name).append('\'')
                .append(", coordinates=").append(coordinates)
                .append(", creationDate=").append(creationDate)
                .append(", enginePower=").append(enginePower)
                .append(", distanceTravelled=").append(distanceTravelled)
                .append(", type=").append(type)
                .append(", fuelType=").append(fuelType)
                .append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Vehicle vehicle))
            return false;
        return Double.compare(enginePower, vehicle.enginePower) == 0 &&
                Objects.equals(name, vehicle.name) &&
                Objects.equals(coordinates, vehicle.coordinates) &&
                Objects.equals(creationDate, vehicle.creationDate) &&
                Objects.equals(distanceTravelled, vehicle.distanceTravelled) &&
                Objects.equals(type, vehicle.type) &&
                Objects.equals(fuelType, vehicle.fuelType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, creationDate,
                            distanceTravelled, type, fuelType);
    }
}
