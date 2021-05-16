package tv.eurovision.cities.service;

import org.springframework.data.domain.Page;
import tv.eurovision.cities.entity.CityEntity;

import java.util.List;

public interface CityService {

    Page<CityEntity> findByPage(int page, int size);

    List<CityEntity> findLargestSequenceByPage(int page, int size);
}
