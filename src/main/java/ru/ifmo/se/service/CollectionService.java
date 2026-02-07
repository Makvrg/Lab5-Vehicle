package ru.ifmo.se.service;

import lombok.RequiredArgsConstructor;
import ru.ifmo.se.entity.Vehicle;
import ru.ifmo.se.entity.VehicleType;
import ru.ifmo.se.event.ShutdownListener;
import ru.ifmo.se.io.output.formatter.OutputStringFormatter;
import ru.ifmo.se.repository.CollectionRepository;
import ru.ifmo.se.service.exceptions.*;

import java.util.*;

@RequiredArgsConstructor
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final OutputStringFormatter formatter;
    private final List<ShutdownListener> listeners = new ArrayList<>();

    public boolean exit() {
        shutdown();
        return true;
    }

    public String info() {
        return formatter.formatCollectionWithInfoFields(
                collectionRepository.getCollectionType(),
                collectionRepository.getInitializationDate(),
                collectionRepository.getElementsType(),
                collectionRepository.getCountOfElements()
        );
    }

    public boolean add(Vehicle vehicle) {
        vehicle.setId(createNewId());
        vehicle.setCreationDate(new Date());
        return collectionRepository.add(vehicle);
    }

    public boolean addInitVehicle(Vehicle vehicle) {
        if (collectionRepository.existsById(vehicle.getId())) {
            throw new NonUniqueIdException("Передан уже существующий id");
        }
        if (vehicle.getCreationDate().after(new Date())) {
            throw new CreationDateIsAfterNowException(
                    "Передана дата и время создания объекта Vehicle из будущего");
        }
        return collectionRepository.add(vehicle);
    }

    private long createNewId() {
        try {
            return collectionRepository.findMaxId() + 1L;
        } catch (MaxIdNotExistException e) {
            return 1L;
        }
    }

    public boolean addIfMin(Vehicle vehicle) {
        Optional<Vehicle> minVehicle = collectionRepository.findMinVehicle();
        if (minVehicle.isPresent() && vehicle.compareTo(minVehicle.get()) < 0) {
            return collectionRepository.add(vehicle);
        } else {
            return false;
        }
    }

    public boolean updateById(Vehicle vehicle,
                              ParamTypedData paramTypedData) {
        return collectionRepository.updateById(paramTypedData.getId(), vehicle);
    }

    public Collection<Vehicle> show() {
        return collectionRepository.findAll();
    }

    public boolean removeById(long id) {
        try {
            return collectionRepository.deleteById(id);
        } catch (IllegalStateException e) {
            throw new RemoveByIdIllegalStateException(e.getMessage());
        }
    }

    public boolean clear() {
        return collectionRepository.deleteAll();
    }

    public Collection<Vehicle> getVehiclesForSave() {
        return collectionRepository.findAll();
    }

    public boolean removeGreater(Vehicle vehicle) {
        boolean returned = false;
        for (Iterator<Vehicle> itr = collectionRepository.findAll().iterator(); itr.hasNext(); ) {
            if (itr.next().compareTo(vehicle) > 0) {
                itr.remove();
                returned = true;
            }
        }
        return returned;
    }

    public boolean removeLower(Vehicle vehicle) {
        boolean returned = false;
        for (Iterator<Vehicle> itr = collectionRepository.findAll().iterator(); itr.hasNext(); ) {
            if (itr.next().compareTo(vehicle) < 0) {
                itr.remove();
                returned = true;
            }
        }
        return returned;
    }

    public Optional<Vehicle> maxByEnginePower() {
        double maxEnginePower;
        try {
            maxEnginePower = collectionRepository.findMaxEnginePower();
        } catch (MaxEnginePowerNotExistException e) {
            return Optional.empty();
        }
        return collectionRepository.findVehicleByEnginePower(maxEnginePower);
    }

    public Map<Float, Integer> groupCountingByDistanceTravelled() {
        Map<Float, List<Vehicle>> groups = collectionRepository.groupByDistanceTravelled();
        HashMap<Float, Integer> counts = new HashMap<>();
        for (Map.Entry<Float, List<Vehicle>> entry : groups.entrySet()) {
            counts.put(entry.getKey(), entry.getValue().size());
        }
        return counts;
    }

    public long countLessThanType(VehicleType vehicleType) {
        return collectionRepository.findAll().stream()
                .filter(vehicle -> vehicle.getType().compareTo(vehicleType) < 0)
                .count();
    }

    public void addShutdownListener(ShutdownListener listener) {
        listeners.add(listener);
    }

    private void shutdown() {
        listeners.forEach(ShutdownListener::onShutdown);
    }
}
