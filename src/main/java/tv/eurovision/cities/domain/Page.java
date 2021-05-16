package tv.eurovision.cities.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Page representation to be exposed by REST API
 */
@Getter
@Builder(toBuilder = true)
public class Page<T> {

    private final List<T> content;
    private final long totalPages;
    private final long totalElements;
}
