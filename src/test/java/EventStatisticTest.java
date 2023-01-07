import org.junit.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class EventStatisticTest {

    void assertStatistic(Map<String, Double> expected, Map<String, Double> actual) {
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, Double> entry : expected.entrySet()) {
            assertEquals(entry.getValue(), actual.get(entry.getKey()), 0.0001);
        }
    }

    @Test
    public void simpleTest() {
        SetableClock clock = new SetableClock(Instant.now());
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        eventsStatistic.incEvent("a");
        eventsStatistic.incEvent("a");
        eventsStatistic.incEvent("b");
        eventsStatistic.incEvent("b");
        eventsStatistic.incEvent("b");
        eventsStatistic.incEvent("c");
        eventsStatistic.incEvent("c");
        eventsStatistic.incEvent("c");
        eventsStatistic.incEvent("c");
        assertStatistic(eventsStatistic.getAllEventStatistic(),
                Map.of("a", 0.03333333333333333, "b", 0.05, "c", 0.06666666666666667));
        clock.setNow(Instant.now().plusSeconds(3600));
        assertEquals(eventsStatistic.getEventStatisticByName("d"), 0.0, 0.0001);
        assertStatistic(eventsStatistic.getAllEventStatistic(), new HashMap<>());
    }

    @Test
    public void randomTest() {
        SetableClock clock = new SetableClock(Instant.now());
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        HashMap<String, Double> expected = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String name = String.valueOf((int) (Math.random() * 100));
            eventsStatistic.incEvent(name);
            expected.put(name, expected.getOrDefault(name, 0.0) + 1.0 / 60.0);
        }
        assertStatistic(eventsStatistic.getAllEventStatistic(), expected);
        clock.setNow(Instant.now().plusSeconds(3600));
        assertStatistic(eventsStatistic.getAllEventStatistic(), new HashMap<>());
    }


    @Test
    public void randomTest2() {
        EventsStatistic eventsStatistic = new EventsStatisticImpl(new NormalClock());
        HashMap<String, Double> expected = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String name = String.valueOf((int) (Math.random() * 100));
            eventsStatistic.incEvent(name);
            expected.put(name, expected.getOrDefault(name, 0.0) + 1.0 / 60.0);
        }
        assertStatistic(eventsStatistic.getAllEventStatistic(), expected);
    }


    @Test
    public void bigTest() {
        SetableClock clock = new SetableClock(Instant.now());
        EventsStatistic eventsStatistic = new EventsStatisticImpl(clock);
        HashMap<String, Double> expected = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            String name = String.valueOf((int) (Math.random() * 100));
            eventsStatistic.incEvent(name);
            expected.put(name, expected.getOrDefault(name, 0.0) + 1.0 / 60.0);
        }
        assertStatistic(eventsStatistic.getAllEventStatistic(), expected);
        clock.setNow(Instant.now().plusSeconds(3600));
        assertStatistic(eventsStatistic.getAllEventStatistic(), new HashMap<>());
    }
}

