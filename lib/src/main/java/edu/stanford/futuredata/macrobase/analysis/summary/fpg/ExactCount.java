package edu.stanford.futuredata.macrobase.analysis.summary.fpg;

import edu.stanford.futuredata.macrobase.analysis.summary.fpg.result.ItemsetWithCount;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class ExactCount {
    private HashMap<Integer, Double> counts = new HashMap<>();

    public HashMap<Integer, Double> getCounts() {
        return counts;
    }

    public ExactCount count(List<Set<Integer>> transactions) {
        for (Set<Integer> txn : transactions) {
            for (int i : txn) {
                Double curVal = counts.get(i);
                if (curVal == null) {
                    curVal = 0.;
                }
                counts.put(i, curVal + 1);
            }
        }

        return this;
    }

    public ExactCount countGrouped(List<ItemsetWithCount> transactions) {
        for (ItemsetWithCount ic : transactions) {
            for (int i : ic.getItems()) {
                Double curVal = counts.get(i);
                if (curVal == null) {
                    curVal = 0.;
                }
                counts.put(i, curVal + ic.getCount());
            }
        }
        return this;
    }
}
