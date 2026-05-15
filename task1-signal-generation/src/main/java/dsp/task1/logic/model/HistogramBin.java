package dsp.task1.logic.model;

public class HistogramBin {
    private final String label;
    private final int count;

    public HistogramBin(String label, int count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public int getCount() {
        return count;
    }
}
