package com.tsystems.javaschool.tasks.pyramid;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PyramidBuilder {
    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {
        // TODO : Implement your solution here
        int base = testPyramidForTriangle(inputNumbers);
        testPyramidForNull(inputNumbers);

        int cols = base + base - 1;
        int rows = base;
        int[][] array = new int[rows][cols];
        List<Integer> integerList = inputNumbers.stream().sorted().collect(Collectors.toList());
        for (int[] row : array) {
            Arrays.fill(row, 0);
        }
        int center = (cols / 2);
        int count = 1;
        int arrIdx = 0;

        for (int i = 0, offset = 0; i < rows; i++, offset++, count++) {
            int start = center - offset;
            for (int j = 0; j < count * 2; j += 2, arrIdx++) {
                array[i][start + j] = integerList.get(arrIdx);
            }
        }
        return array;
    }

    public int testPyramidForTriangle(List<Integer> inputNumbers) {
        int num = 0;
        int increment = 0;
        for (int i = 1; i < inputNumbers.size(); i++) {
            num = i + increment;
            int base = num - increment;
            increment = num;
            if (inputNumbers.size() == increment) {
                return base;
            }
        }
        throw new CannotBuildPyramidException();
    }

    public void testPyramidForNull(List<Integer> inputNumbers) {
        Iterator iter = inputNumbers.iterator();
        while (iter.hasNext()) {
            if (iter.next() == null) {
                throw new CannotBuildPyramidException();
            }
        }
    }


}
