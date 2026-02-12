package ru.ifmo.se.io.output.formatter;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.validation.ConstraintViolation;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.io.input.readers.Reader;

import java.util.*;

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
          .append("  Тип топлива: ").append(vehicle.getFuelType().getTitle());
        return sb.toString();
    }

    public String formatVehicleCollection(Collection<Vehicle> vehicles) {
        StringBuilder sb = new StringBuilder();
        for (Vehicle vehicle : vehicles) {
            sb.append(formatVehicle(vehicle)).append("\n\n");
        }
        if (sb.length() >= 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
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
                            "Объектов с пробегом=%f в коллекции ровно %d%n",
                            entry.getKey(), entry.getValue()
                    )
            );
        }
        return sb.delete(sb.length() - 1, sb.length()).toString();
    }

    public String formatFieldViolations(Set<? extends ConstraintViolation<?>> fieldViols) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> viol : fieldViols) {
            sb.append(viol.getMessage())
                    .append("\n");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    public String formatFileVehiclesExc(List<CsvException> exceptions) {
        StringBuilder sb = new StringBuilder();
        for (CsvException e : exceptions) {
            sb.append("Ошибка в строке ")
                    .append(e.getLineNumber())
                    .append(": ");

            if (e instanceof CsvDataTypeMismatchException mismatch) {
                sb.append("Поле имеет некорректное значение: ")
                        .append(mismatch.getSourceObject());
            }
            else if (e instanceof CsvRequiredFieldEmptyException required) {
                sb.append("Обязательное поле ")
                        .append(required.getDestinationField().getName())
                        .append(" отсутствует");
            }
            else {
                sb.append(e.getMessage());
            }

            sb.append("\n");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
