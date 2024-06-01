// TimeService.java

package az.murad.mallRestaurant.services;

import az.murad.mallRestaurant.Entity.Time;
import az.murad.mallRestaurant.repository.TimeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<Time> getAllTimes() {
        return timeRepository.findAll();
    }

    public Time getTimeById(String id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time not found with id: " + id));
    }

    public Time createTime(Time newTime) {
        return timeRepository.save(newTime);
    }

    public Time updateTime(String id, Time updatedTime) {
        Time existingTime = timeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time not found with id: " + id));

        // Apply updates only to non-null fields
        if (updatedTime.getValue() != null) {
            existingTime.setValue(updatedTime.getValue());
        }

        // Save the updated time
        return timeRepository.save(existingTime);
    }

    public void deleteTime(String id) {
        timeRepository.deleteById(id);
    }

    // Additional methods...
}
