package ru.ifmo.se.io.output.formatter;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.readers.Reader;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class OutputStringFormatter {

    public String formatVehicle(Vehicle vehicle) {
        StringBuilder sb = new StringBuilder();
        sb.append("Объект Vehicle:\n")
          .append("  Id: ").append(vehicle.getId()).append("\n")
          .append("  Название: ").append(vehicle.getName()).append("\n")
          .append("  Координата x: ").append(vehicle.getCoordinates().getX()).append("\n")
          .append("  Координата y: ").append(vehicle.getCoordinates().getY()).append("\n")
          .append("  Дата и время сборки: ").append(vehicle.getCreationDate()).append("\n")
          .append("  Мощность двигателя: ").append(vehicle.getEnginePower()).append("\n")
          .append("  Пробег: ").append(vehicle.getDistanceTravelled()).append("\n")
          .append("  Тип транспорта: ").append(vehicle.getType().getTitle()).append("\n")
          .append("  Тип топлива: ").append(vehicle.getFuelType().getTitle()).append("\n");
        return sb.toString();
    }

    public String formatVehicleCollection(Collection<Vehicle> vehicles) {
        StringBuilder sb = new StringBuilder();
        for (Vehicle vehicle : vehicles) {
            sb.append(formatVehicle(vehicle)).append("\n\n");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public String formatCollectionWithInfoFields(
            Class<?> collectionType,
            Date initializationDate,
            Class<?> elementsType,
            int countOfElements) {
        StringBuilder sb = new StringBuilder();
        sb.append("Информация о коллекции:\n")
                .append(String.format(
                        "1. Тип коллекции: %s%n",
                        collectionType.getSimpleName()
                        )
                )
                .append(String.format(
                        "2. Дата инициализации: %s%n",
                        initializationDate
                        )
                )
                .append(String.format(
                        "3. Тип элементов: %s%n",
                        elementsType.getSimpleName()
                        )
                )
                .append(String.format(
                        "4. Количество элементов: %s",
                        countOfElements
                        )
                );
        return sb.toString();
    }

    public String formatCurrentReaderInfo(List<Reader> readers) {
        StringBuilder sb = new StringBuilder();
        sb.append("Активен режим чтения ");
        if (readers.size() == 1) {
            sb.append("терминала");
        } else {
            sb.append(
                    String.format(
                            "файла %s",
                            readers.get(readers.size() - 1).getName()
                    )
            );
        }
        return sb.toString();
    }

    public String formatNumberOfGroups(Map<Float, Integer> numberOfGroups) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Float, Integer> entry : numberOfGroups.entrySet()) {
            sb.append(
                    String.format(
                            "Объектов с пробегом=%f в коллекции ровно %d\n",
                            entry.getKey(), entry.getValue()
                    )
            );
        }
        return sb.delete(sb.length() - 1, sb.length()).toString();
    }
}
