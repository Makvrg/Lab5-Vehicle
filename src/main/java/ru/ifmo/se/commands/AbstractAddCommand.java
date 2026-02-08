package ru.ifmo.se.commands;

import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.entity.Coordinates;
import ru.ifmo.se.entity.VehicleType;
import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.io.input.readers.InputTextHandler;
import ru.ifmo.se.io.input.readers.Reader;
import ru.ifmo.se.io.input.readers.file.FileReader;
import ru.ifmo.se.io.output.print.Printer;
import ru.ifmo.se.service.CollectionService;
import ru.ifmo.se.service.ParamTypedData;
import ru.ifmo.se.typer.DataTyper;
import ru.ifmo.se.validator.CommandValidatorProvider;
import ru.ifmo.se.validator.exceptions.ExecuteScriptValidateException;
import ru.ifmo.se.validator.exceptions.InputFieldValidationException;
import ru.ifmo.se.validator.exceptions.ParamRawDataValidationException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractAddCommand extends Command {

    private Reader reader;

    protected final CollectionService collectionService;
    private final CommandValidatorProvider validatorProvider;
    private final DataTyper dataTyper;
    protected final Printer printer;
    private final Map<String, Supplier<String>> readActions = buildMapOfReadActions();
    private boolean inputIsRepeated = false;
    private Vehicle vehicle;
    private final Map<String, Consumer<String>> inputValidateMethods;
    private final Map<String, Consumer<String>> dtoFieldSetters;

    protected AbstractAddCommand(
            String commandSignature,
            String commandDescription,
            CollectionService collectionService,
            CommandValidatorProvider validatorProvider,
            DataTyper dataTyper,
            Printer printer) {
        super(commandSignature, commandDescription);
        this.collectionService = collectionService;
        this.validatorProvider = validatorProvider;
        this.dataTyper = dataTyper;
        this.printer = printer;
        vehicle = new Vehicle();
        vehicle.setCoordinates(new Coordinates());
        inputValidateMethods = buildMapOfInputValidateMethods();
        dtoFieldSetters = buildMapOfDtoFieldSetters();
    }

    @Override
    public void execute(String[] inputArgs, Reader reader) {
        this.reader = reader;
        ParamRawData paramRawData = makeParamRawData(inputArgs);

        if (!paramRawData.getEmptyFields().isEmpty()) {
            printer.forcePrintln("Отсутствуют аргументы команды: ");
            for (String emptyField : paramRawData.getEmptyFields()) {
                printer.forcePrintln(emptyField);
            }
            return;
        }
        try {
            validatorProvider.getDataValidator().validateParamRawData(paramRawData);
        } catch (ParamRawDataValidationException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }

        vehicle = new Vehicle();
        vehicle.setCoordinates(new Coordinates());
        vehicle.setGovernor(new Human());
        try {
            readManage();
        } catch (ExecuteScriptValidateException e) {
            printer.forcePrintln(e.getMessage());
            return;
        }

        ParamTypedData paramTypedData =
                dataTyper.typifyParamRawData(paramRawData);

        workWithPrintedText(
                useService(
                        vehicle,
                        paramTypedData
                )
        );
    }

    private void readManage() {
        printer.printlnIfOn("Следуя указаниям, введите данные объекта City");
        for (Map.Entry<String, Supplier<String>> actionEntry : readActions.entrySet()) {
            while (true) {
                String input = actionEntry.getValue().get();
                try {
                    inputValidateMethods.get(actionEntry.getKey()).accept(input);
                    dtoFieldSetters.get(actionEntry.getKey()).accept(input);
                } catch (InputFieldValidationException e) {
                    printer.printlnIfOn(e.getMessage() + ", повторите ввод");
                    inputIsRepeated = true;
                    continue;
                }
                inputIsRepeated = false;
                break;
            }
        }
    }

    protected abstract ParamRawData makeParamRawData(String[] inputArgs);

    protected abstract void workWithPrintedText(boolean result);

    protected abstract boolean useService(Vehicle vehicle,
                                          ParamTypedData paramTypedData);

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

        actions.put(Vehicle.FieldNames.ID.getTitle(),
                () ->
                    readInput(
                            null,
                            "Введите название города"
                    )
        );
        actions.put(Vehicle.FieldNames.X.getTitle(),
                () ->
                    readInput(
                            "x-координата города - вещественное число, не превышающее 579",
                            "Введите координату x"
                    )
        );
        actions.put(Vehicle.FieldNames.Y.getTitle(),
                () ->
                    readInput(
                            "y-координата города - вещественное число",
                            "Введите координату y"
                    )
        );
        actions.put(Vehicle.FieldNames.AREA.getTitle(),
                () ->
                    readInput(
                            null,
                            "Введите целочисленную площадь города"
                    )
        );
        actions.put(Vehicle.FieldNames.POPULATION.getTitle(),
                () ->
                    readInput(
                            null,
                            "Введите численность населения города"
                    )
        );
        actions.put(Vehicle.FieldNames.METERS_ABOVE_SEA_LEVEL.getTitle(),
                () ->
                    readInput(
                            "Количество метров над уровнем моря - вещественное число",
                            "Введите количество метров над уровнем моря"
                    )
        );
        actions.put(Vehicle.FieldNames.POPULATION_DENSITY.getTitle(),
                () ->
                    readInput(
                            null,
                            "Введите целочисленную плотность населения города"
                        )
        );
        actions.put(Vehicle.FieldNames.AGGLOMERATION.getTitle(),
                () ->
                    readInput(
                            null,
                            "Введите численность населения агломерации города"
                    )
        );
        StringBuilder governmentExplanation = new StringBuilder();
        governmentExplanation.append("Возможные типы правления города:\n");
        for (VehicleType vehicleType : VehicleType.values()) {
            governmentExplanation.append("- ")
                                 .append(vehicleType)
                                 .append("\n");
        }
        actions.put(Vehicle.FieldNames.GOVERNMENT.getTitle(),
                () ->
                    readInput(
                            governmentExplanation.toString(),
                            "Введите тип правления города"
                    )
        );
        actions.put(Vehicle.FieldNames.HEIGHT.getTitle(),
                () ->
                    readInput(
                            "Рост губернатора города - вещественное число в метрах",
                            "Введите рост губернатора"
                    )
        );
        actions.put(Vehicle.FieldNames.BIRTHDAY.getTitle(),
                () ->
                    readInput(
                            "Дата и время рождения губернатора "
                                    + "города имеют формат дд-ММ-гггг ЧЧ:мм:сс",
                            "Введите дату и время рождения губернатора"
                    )
        );
        return actions;
    }

    private Map<String, Consumer<String>> buildMapOfInputValidateMethods() {
        Map<String, Consumer<String>> validateMethods = new HashMap<>();

        validateMethods.put(
                Vehicle.FieldNames.NAME.getTitle(), dataValidator::validateNameInput);
        validateMethods.put(
                Vehicle.FieldNames.X.getTitle(), dataValidator::validateXCoordInput);
        validateMethods.put(
                Vehicle.FieldNames.Y.getTitle(), dataValidator::validateYCoordInput);
        validateMethods.put(
                Vehicle.FieldNames.AREA.getTitle(), dataValidator::validateAreaInput
        );
        validateMethods.put(
                Vehicle.FieldNames.POPULATION.getTitle(), dataValidator::validatePopulationInput);
        validateMethods.put(
                Vehicle.FieldNames.METERS_ABOVE_SEA_LEVEL.getTitle(),
                dataValidator::validateMetersAboveSeaLevelInput
        );
        validateMethods.put(
                Vehicle.FieldNames.POPULATION_DENSITY.getTitle(),
                dataValidator::validatePopulationDensityInput
        );
        validateMethods.put(
                Vehicle.FieldNames.AGGLOMERATION.getTitle(),
                dataValidator::validateAgglomerationInput
        );
        validateMethods.put(
                Vehicle.FieldNames.GOVERNMENT.getTitle(),
                dataValidator::validateRusGovernmentInput
        );
        validateMethods.put(
                Vehicle.FieldNames.HEIGHT.getTitle(), dataValidator::validateHeightInput);
        validateMethods.put(
                Vehicle.FieldNames.BIRTHDAY.getTitle(),
                dataValidator::validateBirthdayInput
        );
        return validateMethods;
    }

    private Map<String, Consumer<String>> buildMapOfDtoFieldSetters() {
        Map<String, Consumer<String>> setters = new HashMap<>();

        setters.put(Vehicle.FieldNames.NAME.getTitle(), name -> vehicle.setName(name));
        setters.put(Vehicle.FieldNames.X.getTitle(),
                x -> vehicle.getCoordinates().setX(Double.parseDouble(x)));
        setters.put(Vehicle.FieldNames.Y.getTitle(),
                y -> vehicle.getCoordinates().setY(Float.parseFloat(y)));
        setters.put(Vehicle.FieldNames.AREA.getTitle(),
                area -> vehicle.setArea(Long.valueOf(area)));
        setters.put(Vehicle.FieldNames.POPULATION.getTitle(),
                popul -> vehicle.setPopulation(Integer.valueOf(popul)));
        setters.put(Vehicle.FieldNames.METERS_ABOVE_SEA_LEVEL.getTitle(),
                meters -> vehicle.setMetersAboveSeaLevel(
                        (meters != null) ? Float.valueOf(meters) : null
                )
        );
        setters.put(Vehicle.FieldNames.POPULATION_DENSITY.getTitle(),
                popDens -> vehicle.setPopulationDensity(Long.parseLong(popDens)));
        setters.put(Vehicle.FieldNames.AGGLOMERATION.getTitle(),
                aggl -> vehicle.setAgglomeration(
                        (aggl != null) ? Integer.valueOf(aggl) : null
                )
        );
        setters.put(Vehicle.FieldNames.GOVERNMENT.getTitle(),
                gov -> vehicle.setGovernment(VehicleType.fromRussianString(gov)));
        setters.put(Vehicle.FieldNames.HEIGHT.getTitle(),
                height -> vehicle.getGovernor().setHeight(Double.parseDouble(height)));
        setters.put(Vehicle.FieldNames.BIRTHDAY.getTitle(),
                birthday ->
                {
                    if (birthday != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        sdf.setLenient(false);
                        try {
                            Date typedBirth = sdf.parse(birthday);
                            vehicle.getGovernor().setBirthday(typedBirth);
                        } catch (ParseException e) {
                            throw new InputFieldValidationException(
                                    "Дата и время рождения губернатора города "
                                            + "должны иметь формат дд-ММ-гггг ЧЧ:мм:сс"
                            );
                        }
                    } else {
                        vehicle.getGovernor().setBirthday(null);
                    }
                }
        );
        return setters;
    }
}
