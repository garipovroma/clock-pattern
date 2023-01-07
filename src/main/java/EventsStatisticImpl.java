import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class EventsStatisticImpl implements EventsStatistic {
    private final HashMap<String, LinkedList<Instant>> events;
    private final Clock clock;

    EventsStatisticImpl(Clock clock) {
        this.clock = clock;
        this.events = new HashMap<>();
    }

    @Override
    public void incEvent(String name) {
        if (events.containsKey(name)) {
            events.get(name).add(clock.now());
        } else {
            LinkedList<Instant> list = new LinkedList<>();
            list.add(clock.now());
            events.put(name, list);
        }
    }

    @Override
    public double getEventStatisticByName(String name) {
        return events.getOrDefault(name, new LinkedList<>()).size() / 60.0;
    }

    @Override
    public Map<String, Double> getAllEventStatistic() {
        Instant now = clock.now();
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, LinkedList<Instant>> entry : events.entrySet()) {
            if (!entry.getValue().isEmpty() && entry.getValue().peekLast().isAfter(now.minusSeconds(3600))) {
                result.put(entry.getKey(), getEventStatisticByName(entry.getKey()));
            }

        }
        return result;
    }

    @Override
    public void printStatistic() {
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, LinkedList<Instant>> entry : events.entrySet()) {
            result.put(entry.getKey(), getEventStatisticByName(entry.getKey()));
        }
        System.out.println(result);
    }
}
