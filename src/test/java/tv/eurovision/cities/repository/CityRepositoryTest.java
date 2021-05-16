package tv.eurovision.cities.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tv.eurovision.cities.entity.CityEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CityRepositoryTest {

    @Autowired
    CityRepository cityRepository;

    @Test
    void shouldPaginateResults() {
        // Given
        CityEntity rioDeJaneiro = CityEntity.builder().id(1).name("Rio de Janeiro").build();
        cityRepository.save(rioDeJaneiro);

        CityEntity saoPaulo = CityEntity.builder().id(2).name("Sao Paulo").build();
        cityRepository.save(saoPaulo);

        // When
        Page<CityEntity> firstPageFoundCities = cityRepository.findAll(PageRequest.of(0, 1, Sort.by("name")));
        Page<CityEntity> secondPageFoundCities = cityRepository.findAll(PageRequest.of(1, 1, Sort.by("name")));

        // Then
        assertThat(firstPageFoundCities.get()).containsExactly(rioDeJaneiro);
        assertThat(secondPageFoundCities.get()).containsExactly(saoPaulo);
    }

    @Test
    void shouldPaginateEmptyResults() {
        // Given

        // When
        Page<CityEntity> firstPageFoundCities = cityRepository.findAll(PageRequest.of(0, 1, Sort.by("name")));
        Page<CityEntity> secondPageFoundCities = cityRepository.findAll(PageRequest.of(1, 1, Sort.by("name")));

        // Then
        assertThat(firstPageFoundCities.get()).isEmpty();
        assertThat(secondPageFoundCities.get()).isEmpty();
    }

}