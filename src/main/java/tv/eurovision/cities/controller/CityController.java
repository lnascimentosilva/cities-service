package tv.eurovision.cities.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tv.eurovision.cities.dto.CityDto;
import tv.eurovision.cities.entity.CityEntity;
import tv.eurovision.cities.service.CityService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * All operations with a city will be routed by this controller.
 */
@RestController
@RequestMapping(path = "api/cities", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final ModelMapper modelMapper;

    /**
     * Returns a page containing a list of cities sorted by name
     *
     * @param page - Zero-based page of choice
     * @param size - number of items per page
     * @return Page<CityDto> - Requested page containing the cities
     */
    @GetMapping("/queryByPage")
    public tv.eurovision.cities.domain.Page<CityDto> queryByPage(@RequestParam int page,
                                                                 @RequestParam int size) {

        Page<CityEntity> cityEntityPage = cityService.findByPage(page, size);
        return tv.eurovision.cities.domain.Page.<CityDto>builder()
                .content(cityEntityPage.stream()
                        .map(city -> modelMapper.map(city, CityDto.class))
                        .collect(Collectors.toList()))
                .totalElements(cityEntityPage.getTotalElements())
                .totalPages(cityEntityPage.getTotalPages())
                .build();
    }

    /**
     * Returns a list representing the largest sequence of cities sorted alphabetically and then by id,
     * by the selected page
     *
     * @param page - Zero-based page of choice
     * @param size - number of items per page
     * @return List<CityDto> - Cities within the requested page representing the largest sequence
     */
    @GetMapping("/queryLargestSequenceByPage")
    public List<CityDto> queryLargestSequenceByPage(@RequestParam int page,
                                                    @RequestParam int size) {

        List<CityEntity> cityEntityPage = cityService.findLargestSequenceByPage(page, size);
        return cityEntityPage.stream()
                .map(city -> modelMapper.map(city, CityDto.class))
                .collect(Collectors.toList());
    }

}