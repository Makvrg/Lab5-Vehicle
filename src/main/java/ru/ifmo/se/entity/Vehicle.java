package ru.ifmo.se.entity;

//import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle implements Comparable<Vehicle> {

    private long id;
    private String name;
    private Coordinates coordinates;
//    @JsonFormat(shape = JsonFormat.Shape.STRING,
//            pattern = "dd-MM-yyyy HH:mm:ss",
//            timezone = "Europe/Moscow"
//    )
    private Date creationDate;
    private double enginePower;
    private Float distanceTravelled;
    private VehicleType type;
    private FuelType fuelType;

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
        Double cityDigit = vehicle.enginePower * vehicle.distanceTravelled;
        return thisDigit.compareTo(cityDigit);
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
