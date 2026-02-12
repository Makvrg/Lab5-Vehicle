package ru.ifmo.se.commands;

import jakarta.validation.ConstraintViolation;
import ru.ifmo.se.entity.Coordinates;
import ru.ifmo.se.entity.FuelType;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.entity.VehicleType;
import ru.ifmo.se.io.input.readers.InputTextHandler;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.file.FileReader;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.ExecuteScriptValidateException;
import ru.ifmo.se.validator.exceptions.InputFieldValidationException;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class VehicleAwareCommand extends Command {

    protected Reader reader;

    protected final CollectionService collectionService;
    protected final CommandValidatorProvider validatorProvider;
    protected final Printer printer;
    private final OutputStringFormatter formatter;
    private final Map<String, Supplier<String>> readActions = buildMapOfReadActions();
    private boolean inputIsRepeated = false;

    protected Vehicle vehicle;

    private final Map<String, Consumer<String>> inputValidateMethods;
    private final Map<String, Consumer<String>> vehicleFieldSetters;

    protected VehicleAwareCommand(
            String commandSignature,
            String commandDescription,
            CollectionService collectionService,
            CommandValidatorProvider validatorProvider,
            Printer printer,
            OutputStringFormatter formatter) {
        super(commandSignature, commandDescription);
        this.collectionService = collectionService;
        this.validatorProvider = validatorProvider;
        this.printer = printer;
        this.formatter = formatter;
        vehicle = new Vehicle();
        vehicle.setCoordinates(new Coordinates());
        inputValidateMethods = buildMapOfInputTypeValidateMethods();
        vehicleFieldSetters = buildMapOfDtoFieldSetters();
    }

    protected void readManage() {
        printer.printlnIfOn("Следуя указаниям, введите данные объекта Vehicle");
        for (Map.Entry<String, Supplier<String>> actionEntry : readActions.entrySet()) {
            while (true) {
                String field = actionEntry.getKey();
                String input = actionEntry.getValue().get();
                try {
                    fieldMange(field, input);
                } catch (InputFieldValidationException e) {
                    printer.printlnIfOn("\n" + e.getMessage() + ", повторите ввод");
                    inputIsRepeated = true;
                    continue;
                }
                printer.printlnIfOn("");
                inputIsRepeated = false;
                break;
            }
        }
    }

    private void fieldMange(String field, String input) {
        Consumer<String> validateMethod = inputValidateMethods.get(field);
        if (validateMethod != null) {
            validateMethod.accept(input);
        }

        vehicleFieldSetters.get(field).accept(input);

        String fieldPath;
        if (field.equals(Vehicle.FieldNames.X.getTitle()) ||
                field.equals(Vehicle.FieldNames.Y.getTitle())) {
            fieldPath = "coordinates." + field;
        } else {
            fieldPath = field;
        }
        Set<? extends ConstraintViolation<?>> fieldViols =
                validatorProvider.getBeanValidator()
                        .validateProperty(vehicle, fieldPath);
        if (!fieldViols.isEmpty()) {
            throw new InputFieldValidationException(
                    formatter.formatFieldViolations(fieldViols));
        }
    }

    private String readInput(String explanation,
                             String message) {
        if (explanation != null && !inputIsRepeated) {
            printer.printlnIfOn(explanation);
        }
        printer.printIfOn(message + " > ");
        try {
            String inputString = reader.readLine();
            if (inputString == null) {
                if (reader instanceof FileReader) {
                    throw new ExecuteScriptValidateException(
                            "Неожиданное количество строк данных в файле"
                    );
                }
                inputString = "";
                printer.forcePrintln("");
            }
            return InputTextHandler.stripOrNullField(inputString);
        } catch (IOException e) {
            throw new ExecuteScriptValidateException(
                    "Файл с указанным названием не найден или к нему нет доступа"
            );
        }
    }

    private Map<String, Supplier<String>> buildMapOfReadActions() {
        Map<String, Supplier<String>> actions = new LinkedHashMap<>();

        actions.put(Vehicle.FieldNames.NAME.getTitle(),
                () ->
                        readInput(
                                null,
                                "Введите название транспорта"
                        )
        );
        actions.put(Vehicle.FieldNames.X.getTitle(),
                () ->
                        readInput(
                                "x-координата транспорта - целое число больше -482",
                                "Введите координату x"
                        )
        );
        actions.put(Vehicle.FieldNames.Y.getTitle(),
                () ->
                        readInput(
                                "y-координата транспорта - целое число",
                                "Введите координату y"
                        )
        );
        actions.put(Vehicle.FieldNames.ENGINE_POWER.getTitle(),
                () ->
                        readInput(
                                "Мощность двигателя - вещественное число больше 0",
                                "Введите мощность двигателя"
                        )
        );
        actions.put(Vehicle.FieldNames.DISTANCE_TRAVELLED.getTitle(),
                () ->
                        readInput(
                                "Пробег - вещественное число больше 0",
                                "Введите пробег транспорта"
                        )
        );
        StringBuilder typeExplanation = new StringBuilder();
        typeExplanation.append("Возможные типы транспорта:\n");
        for (VehicleType vehicleType : VehicleType.values()) {
            typeExplanation.append("- ")
                    .append(vehicleType)
                    .append("\n");
        }
        actions.put(Vehicle.FieldNames.TYPE.getTitle(),
                () ->
                        readInput(
                                typeExplanation.toString(),
                                "Введите тип транспортного средства"
                        )
        );
        StringBuilder fuelTypeExplanation = new StringBuilder();
        fuelTypeExplanation.append("Возможные типы энергии двигателя:\n");
        for (FuelType fuelType : FuelType.values()) {
            fuelTypeExplanation.append("- ")
                    .append(fuelType)
                    .append("\n");
        }
        actions.put(Vehicle.FieldNames.FUEL_TYPE.getTitle(),
                () ->
                        readInput(
                                fuelTypeExplanation.toString(),
                                "Введите тип энергии двигателя"
                        )
        );
        return actions;
    }

    private Map<String, Consumer<String>> buildMapOfInputTypeValidateMethods() {
        Map<String, Consumer<String>> validateMethods = new HashMap<>();

        validateMethods.put(
                Vehicle.FieldNames.X.getTitle(),
                validatorProvider.getDataValidator()::validateXCoordType);
        validateMethods.put(
                Vehicle.FieldNames.Y.getTitle(),
                validatorProvider.getDataValidator()::validateYCoordType);
        validateMethods.put(
                Vehicle.FieldNames.ENGINE_POWER.getTitle(),
                validatorProvider.getDataValidator()::validateEnginePowerType);
        validateMethods.put(
                Vehicle.FieldNames.DISTANCE_TRAVELLED.getTitle(),
                validatorProvider.getDataValidator()::validateDistanceTravelledType);
        validateMethods.put(
                Vehicle.FieldNames.TYPE.getTitle(),
                validatorProvider.getDataValidator()::validateRusVehicleType
        );
        validateMethods.put(
                Vehicle.FieldNames.FUEL_TYPE.getTitle(),
                validatorProvider.getDataValidator()::validateRusFuelType
        );
        return validateMethods;
    }

    private Map<String, Consumer<String>> buildMapOfDtoFieldSetters() {
        Map<String, Consumer<String>> setters = new HashMap<>();

        setters.put(Vehicle.FieldNames.NAME.getTitle(), name -> vehicle.setName(name));
        setters.put(Vehicle.FieldNames.X.getTitle(),
                x -> vehicle.getCoordinates().setX(
                        (x != null) ? Integer.valueOf(x) : null
                )
        );
        setters.put(Vehicle.FieldNames.Y.getTitle(),
                y -> vehicle.getCoordinates().setY(Long.parseLong(y)));
        setters.put(Vehicle.FieldNames.ENGINE_POWER.getTitle(),
                pow -> vehicle.setEnginePower(Double.parseDouble(pow)));
        setters.put(Vehicle.FieldNames.DISTANCE_TRAVELLED.getTitle(),
                dist -> vehicle.setDistanceTravelled(Float.valueOf(dist)));
        setters.put(Vehicle.FieldNames.TYPE.getTitle(),
                type -> vehicle.setType(VehicleType.fromRussianString(type)));
        setters.put(Vehicle.FieldNames.FUEL_TYPE.getTitle(),
                fuel -> vehicle.setFuelType(FuelType.fromRussianString(fuel)));
        return setters;
    }
}
