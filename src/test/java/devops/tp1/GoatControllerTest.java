package devops.tp1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoatController.class)
class GoatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoatService goatService;

    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

    @Test
    void testRegisterBirth() throws Exception {
        Goat goat = new Goat("Bessie", "Alpine", LocalDate.of(2024, 1, 15), 45.5, "Field A");
        when(goatService.saveGoat(any(Goat.class))).thenReturn(goat);

        mockMvc.perform(post("/goats/birth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(goat)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bessie"))
                .andExpect(jsonPath("$.breed").value("Alpine"));

        verify(goatService, times(1)).saveGoat(any(Goat.class));
    }

    @Test
    void testGetGoatsToVaccinate() throws Exception {
        Goat goat = new Goat("Bessie", "Alpine", LocalDate.now(), 45.0, "Field A");
        when(goatService.getGoatsToVaccinateThisMonth()).thenReturn(Arrays.asList(goat));

        mockMvc.perform(get("/goats/to-vaccinate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bessie"));

        verify(goatService, times(1)).getGoatsToVaccinateThisMonth();
    }

    @Test
    void testTransferGoat() throws Exception {
        mockMvc.perform(put("/goats/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"enclosure\":\"New Field\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transferred"));

        verify(goatService, times(1)).transferGoat(1L, "New Field");
    }

    @Test
    void testTransferGoat_BadRequest() throws Exception {
        mockMvc.perform(put("/goats/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Enclosure required"));
    }

    @Test
    void testGetAllGoats() throws Exception {
        Goat goat = new Goat("Bessie", "Alpine", LocalDate.now(), 45.0, "Field A");
        when(goatService.getAllGoats()).thenReturn(Arrays.asList(goat));

        mockMvc.perform(get("/goats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Bessie"));

        verify(goatService, times(1)).getAllGoats();
    }

    @Test
    void testGetRation() throws Exception {
        when(goatService.calculateRation(50.0)).thenReturn("1.25 kg of hay per day");

        mockMvc.perform(get("/goats/ration/50.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("1.25 kg of hay per day"));

        verify(goatService, times(1)).calculateRation(50.0);
    }

    @Test
    void testIsReproductive() throws Exception {
        Goat goat = new Goat("Bessie", "Alpine", LocalDate.now().minusMonths(8), 45.0, "Field A");
        when(goatService.findGoatById(1L)).thenReturn(Optional.of(goat));
        when(goatService.isReproductiveAge(goat.getBirthDate())).thenReturn(true);

        mockMvc.perform(get("/goats/reproductive/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(goatService, times(1)).findGoatById(1L);
    }

    @Test
    void testIsReproductive_GoatNotFound() throws Exception {
        when(goatService.findGoatById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/goats/reproductive/1"))
                .andExpect(status().isNotFound());

        verify(goatService, times(1)).findGoatById(1L);
    }

    @Test
    void testGetAverageMilkProduction() throws Exception {
        when(goatService.averageMilkProduction()).thenReturn(6.0);

        mockMvc.perform(get("/goats/milk-production"))
                .andExpect(status().isOk())
                .andExpect(content().string("6.0"));

        verify(goatService, times(1)).averageMilkProduction();
    }
}