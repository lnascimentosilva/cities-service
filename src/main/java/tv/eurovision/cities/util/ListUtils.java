package tv.eurovision.cities.util;

import java.util.List;

public class ListUtils {

    /**
     * Returns a largest list
     *
     * @param firstList  - A list
     * @param otherLists - A varargs of List
     * @return List<T> - largest list
     */
    public static <T> List<T> pickLargest(List<T> firstList, List<T>... otherLists) {

        List<T> biggestList = firstList;
        for (List<T> otherList : otherLists) {
            if (biggestList.size() < otherList.size()) {
                biggestList = otherList;
            }
        }
        return biggestList;
    }
}
