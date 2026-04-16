package com.earthquake.backend;

import com.earthquake.model.Earthquake;
import com.earthquake.repository.EarthquakeRepository;
import com.earthquake.service.EarthquakeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakeServiceTest {

    @Mock
    private EarthquakeRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EarthquakeService service;

    private static final String USGS_URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    private Map<String, Object> buildFakeResponse(String id, double mag) {
        Map<String, Object> properties = Map.of(
                "mag", mag,
                "magType", "ml",
                "place", "Test place",
                "title", "Test title",
                "time", 1700000000000L
        );

        Map<String, Object> feature = Map.of(
                "id", id,
                "properties", properties
        );

        return Map.of("features", List.of(feature));
    }

    // Тест 1: Успешен fetch и зачувување
    @Test
    void fetchAndStore_shouldSave_whenValidData() {
        when(restTemplate.getForObject(USGS_URL, Map.class))
                .thenReturn(buildFakeResponse("eq1", 3.0));

        when(repository.saveAll(anyList()))
                .thenAnswer(i -> i.getArgument(0));

        List<Earthquake> result = service.fetchAndStoreEarthquakes();

        assertEquals(1, result.size());
        assertEquals("eq1", result.get(0).getId());

        verify(repository).deleteAll();
        verify(repository).saveAll(anyList());
    }

    // Тест 2: Земјотреси под 2.0 се филтрираат
    @Test
    void fetchAndStore_shouldFilterSmallMagnitudes() {
        when(restTemplate.getForObject(USGS_URL, Map.class))
                .thenReturn(buildFakeResponse("eq2", 1.5));

        List<Earthquake> result = service.fetchAndStoreEarthquakes();

        assertEquals(0, result.size());
        verify(repository).deleteAll();
    }

    // Тест 3: API недостапно — фрла исклучок
    @Test
    void fetchAndStore_shouldThrow_whenApiFails() {
        when(restTemplate.getForObject(USGS_URL, Map.class))
                .thenThrow(new RuntimeException("API down"));

        assertThrows(RuntimeException.class,
                () -> service.fetchAndStoreEarthquakes());
    }
        // Тест 4: Null одговор од API — фрла исклучок
        @Test
        void fetchAndStore_shouldThrow_whenResponseIsNull() {
        when(restTemplate.getForObject(USGS_URL, Map.class))
                .thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> service.fetchAndStoreEarthquakes());
        }
    // Тест 4: Филтер по време — враќа само поновите
    @Test
    void getEarthquakesAfter_shouldReturnOnlyNewerEarthquakes() {
        Earthquake old = new Earthquake();
        old.setTime(1000L);

        Earthquake recent = new Earthquake();
        recent.setTime(3000L);

        when(repository.findAll()).thenReturn(List.of(old, recent));

        List<Earthquake> result = service.getEarthquakesAfter(2000L);

        assertEquals(1, result.size());
        assertEquals(3000L, result.get(0).getTime());
    }

    // Тест 5: Успешно бришење
    @Test
    void deleteEarthquake_shouldCallRepository() {
        when(repository.existsById("eq1")).thenReturn(true);

        service.deleteEarthquake("eq1");

        verify(repository).deleteById("eq1");
    }

    // Тест 6: Бришење на непостоечки запис — фрла исклучок
    @Test
    void deleteEarthquake_shouldThrow_whenIdNotFound() {
        when(repository.existsById("invalid")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> service.deleteEarthquake("invalid"));

        verify(repository, never()).deleteById(any());
    }
}