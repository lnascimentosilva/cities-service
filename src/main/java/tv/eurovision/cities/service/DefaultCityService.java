package tv.eurovision.cities.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tv.eurovision.cities.entity.CityEntity;
import tv.eurovision.cities.repository.CityRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static tv.eurovision.cities.util.ListUtils.pickLargest;

/**
 * Service to encapsulate the link between Repository and Controller
 * and to have business logic for some city specific things.
 */
@Service
@RequiredArgsConstructor
public class DefaultCityService implements CityService {

    public static final String DEFAULT_SORT_FIELD = "name";
    private final CityRepository repository;

    /**
     * Returns a page containing a list of cities sorted by name
     *
     * @param page - Zero-based page of choice
     * @param size - number of items per page
     * @return Page<CityEntity> - Requested page containing the cities
     */
    @Override
    public Page<CityEntity> findByPage(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by(DEFAULT_SORT_FIELD)));
    }

    /**
     * Returns a largest possible sequence of cities which are sorted by name and id (not necessarily adjacent)
     *
     * @param page - Zero-based page of choice
     * @param size - number of items per page
     * @return Page<CityEntity> - Requested page containing the cities
     */
    @Override
    public List<CityEntity> findLargestSequenceByPage(int page, int size) {
        return findLargestSequence(repository.findAll(PageRequest.of(page, size, Sort.by(DEFAULT_SORT_FIELD))).toList());
    }

    private List<CityEntity> findLargestSequence(List<CityEntity> cities) {
        if (cities.isEmpty() || cities.size() == 1) {
            return cities;
        }
        Map<Integer, List<CityEntity>> largestSequenceByCityId = new HashMap<>();
        List<CityEntity> largestSequence = Collections.emptyList();
        //starts backwards so largestSequenceByCityId will slowly cache the results
        for (int i = cities.size() - 2; i >= 0; i--) {
            List<CityEntity> temporaryLargestSequence = computeLargestSequenceOf(i, i + 1, cities, largestSequenceByCityId);
            //avoids extra iteration to find the result
            if (temporaryLargestSequence.size() > largestSequence.size()) {
                largestSequence = temporaryLargestSequence;
            }
        }

        return largestSequence;
    }

    private List<CityEntity> computeLargestSequenceOf(int currentCityIndex,
                                                      int nextCityIndex,
                                                      List<CityEntity> cities,
                                                      Map<Integer, List<CityEntity>> largestSequenceByCityId) {

        //returns current city in case of arrayIndexOutOfBounds
        if (cities.size() - 1 < nextCityIndex) {
            return Collections.singletonList(cities.get(currentCityIndex));
        }

        //fetches already calculated cities, avoiding extra computation
        CityEntity currentCity = cities.get(currentCityIndex);
        if (largestSequenceByCityId.containsKey(currentCity.getId())) {
            return largestSequenceByCityId.get(currentCity.getId());
        }

        CityEntity nextCity = cities.get(nextCityIndex);
        List<CityEntity> trunkLargestSequence = new ArrayList<>();
        if (currentCity.getId() < nextCity.getId()) {
            //adds current city on the top of the largest list for the next city
            trunkLargestSequence.add(currentCity);
            //follow natural branch
            trunkLargestSequence.addAll(computeLargestSequenceOf(nextCityIndex, nextCityIndex + 1, cities, largestSequenceByCityId));
        }

        //creates a branch enforcing all paths calculation
        List<CityEntity> branchLargestSequence = computeLargestSequenceOf(currentCityIndex, nextCityIndex + 1, cities, largestSequenceByCityId);

        //ultimately decides for the largest sequence for a given city
        largestSequenceByCityId.computeIfPresent(currentCity.getId(),
                (key, value) -> pickLargest(value, trunkLargestSequence, branchLargestSequence));
        largestSequenceByCityId.computeIfAbsent(currentCity.getId(),
                (key) -> pickLargest(trunkLargestSequence, branchLargestSequence));

        return largestSequenceByCityId.get(currentCity.getId());

    }
}
