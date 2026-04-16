package devops.tp1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoatServiceTest {

    @Mock
    private GoatRepository goatRepository;

    @Mock
    private HealthRecordRepository healthRecordRepository;

    @InjectMocks
    private GoatService goatService;

    @Test
    void testCalculateRation() {
        double weight = 50.0;
        String expected = "1.25 kg of hay per day";

        String result = goatService.calculateRation(weight);

        assertEquals(expected, result);
    }

    @Test
    void testIsReproductiveAge_YoungGoat() {
        LocalDate birthDate = LocalDate.now().minusMonths(3); // 3 months old

        boolean result = goatService.isReproductiveAge(birthDate);

        assertFalse(result);
    }

    @Test
    void testIsReproductiveAge_OldGoat() {
        LocalDate birthDate = LocalDate.now().minusMonths(8); // 8 months old

        boolean result = goatService.isReproductiveAge(birthDate);

        assertTrue(result);
    }

    @Test
    void testAverageMilkProduction_NoGoats() {
        when(goatRepository.findAll()).thenReturn(Arrays.asList());

        double result = goatService.averageMilkProduction();

        assertEquals(0.0, result);
    }

    @Test
    void testAverageMilkProduction_WithGoats() {
        Goat goat1 = new Goat("Goat1", "Alpine", LocalDate.now(), 45.0, "Field A");
        Goat goat2 = new Goat("Goat2", "Saanen", LocalDate.now(), 50.0, "Field A");
        when(goatRepository.findAll()).thenReturn(Arrays.asList(goat1, goat2));

        double result = goatService.averageMilkProduction();

        assertEquals(4.0, result); // 2 goats * 2 liters each
    }

    @Test
    void testGetGoatsToVaccinateThisMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        HealthRecord record1 = new HealthRecord();
        record1.setGoat(new Goat("Goat1", "Alpine", LocalDate.now(), 45.0, "Field A"));
        record1.setLastVaccineDate(oneMonthAgo.minusDays(1));

        when(healthRecordRepository.findByLastVaccineDateBefore(oneMonthAgo)).thenReturn(Arrays.asList(record1));

        List<Goat> result = goatService.getGoatsToVaccinateThisMonth();

        assertEquals(1, result.size());
        assertEquals("Goat1", result.get(0).getName());
    }

    @Test
    void testSaveGoat() {
        Goat goat = new Goat("TestGoat", "TestBreed", LocalDate.now(), 40.0, "TestEnclosure");
        when(goatRepository.save(goat)).thenReturn(goat);

        Goat result = goatService.saveGoat(goat);

        assertEquals(goat, result);
        verify(goatRepository, times(1)).save(goat);
    }

    @Test
    void testFindGoatById() {
        Goat goat = new Goat("TestGoat", "TestBreed", LocalDate.now(), 40.0, "TestEnclosure");
        when(goatRepository.findById(1L)).thenReturn(Optional.of(goat));

        Optional<Goat> result = goatService.findGoatById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestGoat", result.get().getName());
    }

    @Test
    void testTransferGoat() {
        Goat goat = new Goat("TestGoat", "TestBreed", LocalDate.now(), 40.0, "OldEnclosure");
        when(goatRepository.findById(1L)).thenReturn(Optional.of(goat));

        goatService.transferGoat(1L, "NewEnclosure");

        assertEquals("NewEnclosure", goat.getEnclosure());
        verify(goatRepository, times(1)).save(goat);
    }

    @Test
    void testTransferGoat_NotFound() {
        when(goatRepository.findById(1L)).thenReturn(Optional.empty());

        goatService.transferGoat(1L, "NewEnclosure");

        verify(goatRepository, never()).save(any());
    }
}