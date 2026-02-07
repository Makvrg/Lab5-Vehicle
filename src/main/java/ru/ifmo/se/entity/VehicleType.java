package ru.ifmo.se.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VehicleType {

    BICYCLE("Велосипед"),
    MOTORCYCLE("Мотоцикл"),
    CHOPPER("Чоппер"),
    HOVERBOARD("Ховерборд");

    private final String title;

    public static VehicleType fromRussianString(String russianName) {
        if (russianName == null) {
            throw new IllegalArgumentException("Передано пустое значение");
        }
        for (VehicleType vehicleType : VehicleType.values()) {
            if (vehicleType.title.equals(russianName)) {
                return vehicleType;
            }
        }
        throw new IllegalArgumentException("Неизвестное значение: " + russianName);
    }

    @Override
    public String toString() {
        return title;
    }
}
