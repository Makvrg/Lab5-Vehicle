package ru.ifmo.se.validator;

import ru.ifmo.se.entity.FuelType;
import ru.ifmo.se.entity.VehicleType;
import ru.ifmo.se.io.input.dto.ParamRawData;
import ru.ifmo.se.validator.exceptions.*;

public class DataValidator {

    public void validateXCoordType(String x) {
        if (x != null) {
            try {
                Integer.parseInt(x);
            } catch (NumberFormatException e) {
                throw new InputFieldValidationException(
                        ValidatorMessages.X_COORD_MUST_BE_INTEGER);
            }
        }
    }

    public void validateYCoordType(String y) {
        if (y == null) {
            throw new InputFieldValidationException(
                    ValidatorMessages.Y_COORD_MUST_BE_NOT_EMPTY);
        }
        try {
            Long.parseLong(y);
        } catch (NumberFormatException e) {
            throw new InputFieldValidationException(
                    ValidatorMessages.Y_COORD_MUST_BE_INTEGER);
        }
    }

    public void validateEnginePowerType(String enginePower) {
        if (enginePower == null) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ENGINE_POWER_MUST_BE_NOT_EMPTY);
        }
        double pow;
        try {
            pow = Double.parseDouble(enginePower);
        } catch (NumberFormatException e) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ENGINE_POWER_MUST_BE_REAL_NUM);
        }
        if (Double.isNaN(pow)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ENGINE_POWER_MUST_BE_REAL_NUM);
        }
        if (Double.isInfinite(pow)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ABS_ENGINE_POWER_MUST_BE_LESS_MAX);
        }
    }

    public void validateTypedEnginePower(double pow) {
        if (Double.isNaN(pow)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ENGINE_POWER_MUST_BE_REAL_NUM);
        }
        if (Double.isInfinite(pow)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ABS_ENGINE_POWER_MUST_BE_LESS_MAX);
        }
    }

    public void validateDistanceTravelledType(String distanceTravelled) {
        if (distanceTravelled == null) {
            throw new InputFieldValidationException(
                    ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_NOT_NULL);
        }
        float dist;
        try {
            dist = Float.parseFloat(distanceTravelled);
        } catch (NumberFormatException e) {
            throw new InputFieldValidationException(
                    ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_REAL_NUM);
        }
        if (Float.isNaN(dist)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_REAL_NUM);
        }
        if (Float.isInfinite(dist)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ABS_DISTANCE_TRAVELLED_MUST_BE_LESS_MAX);
        }
    }

    public void validateTypedDistanceTravelled(Float dist) {
        if (dist == null) {
            throw new InputFieldValidationException(
                    ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_NOT_NULL);
        }
        if (Float.isNaN(dist)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.DISTANCE_TRAVELLED_MUST_BE_REAL_NUM);
        }
        if (Float.isInfinite(dist)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.ABS_DISTANCE_TRAVELLED_MUST_BE_LESS_MAX);
        }
    }

    public void validateRusVehicleType(String rusVehicleType) {
        if (rusVehicleType == null
                || !VehicleType.containsRussianString(rusVehicleType)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.VEHICLE_TYPE_MUST_BE_IN_ENUM);
        }
    }

    public void validateRusFuelType(String rusFuelType) {
        if (rusFuelType == null
                || !FuelType.containsRussianString(rusFuelType)) {
            throw new InputFieldValidationException(
                    ValidatorMessages.FUEL_TYPE_MUST_BE_IN_ENUM);
        }
    }

    public void validateParamRawData(ParamRawData paramRawData) {
        if (paramRawData.getId() != null) {
            try {
                Long.parseLong(paramRawData.getId());
            } catch (NumberFormatException e) {
                throw new ParamRawDataValidationException(
                        ValidatorMessages.ARGUMENT_ID_MUST_BE_INTEGER
                );
            }
            if (Long.parseLong(paramRawData.getId()) < 1) {
                throw new ParamRawDataValidationException(
                        ValidatorMessages.ARGUMENT_ID_MUST_BE_MORE_ZERO
                );
            }
        }
    }

    public void validateRemoveById(String id) {
        if (id == null) {
            throw new RemoveByIdValidationException(
                    ValidatorMessages.PARAMETER_ID_NOT_PASSED);
        }
        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new RemoveByIdValidationException(
                    ValidatorMessages.ARGUMENT_ID_MUST_BE_INTEGER);
        }
        if (Long.parseLong(id) < 1) {
            throw new ParamRawDataValidationException(
                    ValidatorMessages.ARGUMENT_ID_MUST_BE_MORE_ZERO
            );
        }
    }

    public void validateCountLessType(String rusVehicleType) {
        if (rusVehicleType == null) {
            throw new CountLessThanTypeValidationException(
                    ValidatorMessages.PARAMETER_TYPE_NOT_PASSED);
        }
        try {
            validateRusVehicleType(rusVehicleType);
        } catch (InputFieldValidationException e) {
            throw new CountLessThanTypeValidationException(e.getMessage());
        }
    }

    public void validateExecuteScript(String fileName) {
        if (fileName == null) {
            throw new ExecuteScriptValidateException(
                    ValidatorMessages.PARAMETER_FILE_NAME_NOT_PASSED);
        }
    }
}
