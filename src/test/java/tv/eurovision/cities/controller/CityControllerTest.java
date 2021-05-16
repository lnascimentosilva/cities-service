package tv.eurovision.cities.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tv.eurovision.cities.CitiesApplication;
import tv.eurovision.cities.domain.Page;
import tv.eurovision.cities.dto.CityDto;
import tv.eurovision.cities.entity.CityEntity;
import tv.eurovision.cities.repository.CityRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CitiesApplication.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class CityControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    CityRepository repository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldPaginateResponse() throws Exception {
        // Given
        CityEntity rioDeJaneiro = CityEntity.builder().id(1).name("Rio de Janeiro").build();
        CityEntity saoPaulo = CityEntity.builder().id(2).name("Sao Paulo").build();
        CityEntity espiritoSanto = CityEntity.builder().id(3).name("Espirito Santo").build();
        CityEntity minasGerais = CityEntity.builder().id(4).name("Minas Gerais").build();
        CityEntity natal = CityEntity.builder().id(5).name("Natal").build();
        List<CityEntity> cityEntities = Arrays.asList(espiritoSanto, minasGerais, natal, rioDeJaneiro, saoPaulo);

        repository.saveAll(cityEntities);

        CityDto espiritoSantoDto = CityDto.builder().id(3).name("Espirito Santo").build();
        CityDto minasGeraisDto = CityDto.builder().id(4).name("Minas Gerais").build();
        Page<CityDto> firstPage = Page.<CityDto>builder()
                .content(Arrays.asList(espiritoSantoDto, minasGeraisDto))
                .totalPages(3)
                .totalElements(5)
                .build();

        CityDto natalDto = CityDto.builder().id(5).name("Natal").build();
        CityDto rioDeJaneiroDto = CityDto.builder().id(1).name("Rio de Janeiro").build();
        Page<CityDto> secondPage = Page.<CityDto>builder()
                .content(Arrays.asList(natalDto, rioDeJaneiroDto))
                .totalPages(3)
                .totalElements(5)
                .build();

        // When
        ResultActions resultFromFirstPage = mvc.perform(get("/api/cities/queryByPage")
                .param("page", "0")
                .param("size", "2"));

        ResultActions resultFromSecondPage = mvc.perform(get("/api/cities/queryByPage")
                .param("page", "1")
                .param("size", "2"));

        // Then
        resultFromFirstPage.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(firstPage), true));

        resultFromSecondPage.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(secondPage), true));
    }

    @Test
    void shouldPaginateResponseWithSecondPageEmpty() throws Exception {
        // Given
        CityEntity rioDeJaneiro = CityEntity.builder().id(1).name("Rio de Janeiro").build();
        CityEntity saoPaulo = CityEntity.builder().id(2).name("Sao Paulo").build();
        List<CityEntity> cityEntities = Arrays.asList(rioDeJaneiro, saoPaulo);

        repository.saveAll(cityEntities);

        CityDto rioDeJaneiroDto = CityDto.builder().id(1).name("Rio de Janeiro").build();
        CityDto saoPauloDto = CityDto.builder().id(2).name("Sao Paulo").build();
        Page<CityDto> firstPage = Page.<CityDto>builder()
                .content(Arrays.asList(rioDeJaneiroDto, saoPauloDto))
                .totalPages(1)
                .totalElements(2)
                .build();


        Page<CityDto> secondPage = Page.<CityDto>builder()
                .content(Collections.emptyList())
                .totalPages(1)
                .totalElements(2)
                .build();

        // When
        ResultActions resultFromFirstPage = mvc.perform(get("/api/cities/queryByPage")
                .param("page", "0")
                .param("size", "2"));

        ResultActions resultFromSecondPage = mvc.perform(get("/api/cities/queryByPage")
                .param("page", "1")
                .param("size", "2"));

        // Then
        resultFromFirstPage.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(firstPage), true));

        resultFromSecondPage.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(secondPage), true));
    }

    @Test
    void shouldListLargestSequenceOfCities() throws Exception {
        // Given
        CityEntity rioDeJaneiro = CityEntity.builder().id(1).name("Rio de Janeiro").build();
        CityEntity saoPaulo = CityEntity.builder().id(2).name("Sao Paulo").build();
        CityEntity espiritoSanto = CityEntity.builder().id(3).name("Espirito Santo").build();
        CityEntity minasGerais = CityEntity.builder().id(4).name("Minas Gerais").build();
        CityEntity natal = CityEntity.builder().id(5).name("Natal").build();
        List<CityEntity> cityEntities = Arrays.asList(espiritoSanto, minasGerais, natal, rioDeJaneiro, saoPaulo);

        repository.saveAll(cityEntities);

        CityDto espiritoSantoDto = CityDto.builder().id(3).name("Espirito Santo").build();
        CityDto minasGeraisDto = CityDto.builder().id(4).name("Minas Gerais").build();
        CityDto natalDto = CityDto.builder().id(5).name("Natal").build();
        List<CityDto> largestSequence = Arrays.asList(espiritoSantoDto, minasGeraisDto, natalDto);

        // When
        ResultActions resultFromFirstPage = mvc.perform(get("/api/cities/queryLargestSequenceByPage")
                .param("page", "0")
                .param("size", "5"));


        // Then
        resultFromFirstPage.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(largestSequence), true));

    }

}