package com.tsystems.javaschool.tasks.subsequence;

import com.tsystems.javaschool.tasks.pyramid.CannotBuildPyramidException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        // TODO: Implement the logic here
            if (x == null || y == null) {
                throw new IllegalArgumentException();
            }
        int newJ = 0;
        for (int i = 0; i < x.size(); i++) {
            for (int j = newJ; j < y.size(); j++) {
                if(x.get(i) == y.get(j)) {
                    x.remove(i);
                    i--;
                    newJ = j;
                    break;
                }
            }
        }

        if (x.isEmpty()) {
            return true;
        }
        return false;
    }
}
