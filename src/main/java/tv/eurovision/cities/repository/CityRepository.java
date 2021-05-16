package tv.eurovision.cities.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import tv.eurovision.cities.entity.CityEntity;

/**
 * Database Access Object for cities table.
 */
public interface CityRepository extends PagingAndSortingRepository<CityEntity, Long> {

}
