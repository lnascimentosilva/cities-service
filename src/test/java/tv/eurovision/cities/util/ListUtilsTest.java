package tv.eurovision.cities.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ListUtilsTest {

    @Test
    void shouldReturnLargestList() {
        // Given
        List<String> firstList = Arrays.asList("ABC", "DEF");
        List<String> secondList = Arrays.asList("ABC", "DEF", "GHI");
        List<String> thirdList = Arrays.asList("ABC", "DEF", "GHI", "JLM");

        // When
        List<String> actualLargestList1 = ListUtils.pickLargest(firstList, secondList, thirdList);
        List<String> actualLargestList2 = ListUtils.pickLargest(secondList, firstList, thirdList);
        List<String> actualLargestList3 = ListUtils.pickLargest(thirdList, firstList, secondList);
        List<String> actualLargestList4 = ListUtils.pickLargest(firstList, secondList);

        //Then
        assertThat(actualLargestList1).isEqualTo(thirdList);
        assertThat(actualLargestList2).isEqualTo(thirdList);
        assertThat(actualLargestList3).isEqualTo(thirdList);
        assertThat(actualLargestList4).isEqualTo(secondList);
    }
}