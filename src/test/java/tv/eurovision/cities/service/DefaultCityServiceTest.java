package tv.eurovision.cities.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import tv.eurovision.cities.entity.CityEntity;
import tv.eurovision.cities.repository.CityRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static tv.eurovision.cities.service.DefaultCityService.DEFAULT_SORT_FIELD;

@ExtendWith(MockitoExtension.class)
class DefaultCityServiceTest {

    @Mock
    CityRepository repository;

    @InjectMocks
    DefaultCityService cityService;

    @Test
    void shouldListCities() {
        // Given
        CityEntity rioDeJaneiro = CityEntity.builder().id(1).name("Rio de Janeiro").build();
        CityEntity saoPaulo = CityEntity.builder().id(2).name("Sao Paulo").build();
        CityEntity espiritoSanto = CityEntity.builder().id(3).name("Espirito Santo").build();
        CityEntity minasGerais = CityEntity.builder().id(4).name("Minas Gerais").build();
        CityEntity natal = CityEntity.builder().id(5).name("Natal").build();
        Page<CityEntity> page = new PageImpl<>(Arrays.asList(espiritoSanto, minasGerais, natal, rioDeJaneiro, saoPaulo));

        when(repository.findAll(PageRequest.of(1, 30, Sort.by(DEFAULT_SORT_FIELD))))
                .thenReturn(page);

        // When
        Page<CityEntity> sortedCitiesFound = cityService.findByPage(1, 30);

        // Then
        assertThat(sortedCitiesFound).isEqualTo(page);
    }

    private static Stream<Arguments> largestCitiesSequenceProvider() {
        CityEntity abadan = CityEntity.builder().id(233).name("Abadan").build();
        CityEntity abidjan = CityEntity.builder().id(67).name("Abidjan").build();
        CityEntity accra = CityEntity.builder().id(157).name("Accra").build();
        CityEntity adDammam = CityEntity.builder().id(262).name("Ad-Dammam").build();
        CityEntity adana = CityEntity.builder().id(255).name("Adana").build();
        CityEntity addisAbaba = CityEntity.builder().id(75).name("Addis Ababa").build();
        CityEntity adelaide = CityEntity.builder().id(297).name("Adelaide").build();
        CityEntity agadir = CityEntity.builder().id(124).name("Agadir").build();
        CityEntity agra = CityEntity.builder().id(197).name("Agra").build();
        CityEntity ahmadabad = CityEntity.builder().id(43).name("Ahmadabad").build();
        CityEntity ahvaz = CityEntity.builder().id(317).name("Ahvaz").build();
        CityEntity alJizah = CityEntity.builder().id(87).name("Al-Jizah").build();
        CityEntity alKhartumBahri = CityEntity.builder().id(147).name("Al-Khartum Bahri").build();
        CityEntity alMadinah = CityEntity.builder().id(227).name("Al-Madinah").build();
        CityEntity aleppo = CityEntity.builder().id(103).name("Aleppo").build();
        CityEntity alexandria = CityEntity.builder().id(47).name("Alexandria").build();
        CityEntity algiers = CityEntity.builder().id(176).name("Algiers").build();
        CityEntity allahabad = CityEntity.builder().id(307).name("Allahabad").build();
        CityEntity almaty = CityEntity.builder().id(302).name("Almaty").build();
        CityEntity amman = CityEntity.builder().id(260).name("Amman").build();
        CityEntity amritsar = CityEntity.builder().id(290).name("Amritsar").build();
        CityEntity ankara = CityEntity.builder().id(50).name("Ankara").build();
        CityEntity anshan = CityEntity.builder().id(230).name("Anshan").build();
        CityEntity antananarivo = CityEntity.builder().id(160).name("Antananarivo").build();
        CityEntity aurangabad = CityEntity.builder().id(326).name("Aurangabad").build();
        CityEntity baghdad = CityEntity.builder().id(27).name("Baghdad").build();
        CityEntity baku = CityEntity.builder().id(130).name("Baku").build();
        CityEntity bamako = CityEntity.builder().id(234).name("Bamako").build();
        CityEntity bandung = CityEntity.builder().id(108).name("Bandung").build();
        CityEntity bangalore = CityEntity.builder().id(29).name("Bangalore").build();

        return Stream.of(
                Arguments.of(Collections.emptyList(), Collections.emptyList()),
                Arguments.of(Collections.singletonList(abadan), Collections.singletonList(abadan)),
                Arguments.of(Arrays.asList(abadan, abidjan, accra), Arrays.asList(abidjan, accra)),
                Arguments.of(Arrays.asList(abadan, abidjan, accra, adDammam, adana, addisAbaba, adelaide, agadir, agra, ahmadabad,
                        ahvaz, alJizah, alKhartumBahri, alMadinah, aleppo, alexandria, algiers, allahabad, almaty, amman, amritsar,
                        ankara, anshan, antananarivo, aurangabad, baghdad, baku, bamako, bandung, bangalore),
                        Arrays.asList(abidjan, addisAbaba, alJizah, aleppo, algiers, amman, amritsar, aurangabad))
        );
    }

    @ParameterizedTest
    @MethodSource("largestCitiesSequenceProvider")
    void shouldReturnLargestSequenceOfCities(List<CityEntity> citiesFromDatabase, List<CityEntity> expectedCities) {
        // Given
        Page<CityEntity> page = new PageImpl<>(citiesFromDatabase);

        when(repository.findAll(PageRequest.of(1, 30, Sort.by(DEFAULT_SORT_FIELD))))
                .thenReturn(page);

        // When
        List<CityEntity> sortedCitiesFound = cityService.findLargestSequenceByPage(1, 30);

        // Then
        assertThat(sortedCitiesFound).containsExactlyElementsOf(expectedCities);
    }
}