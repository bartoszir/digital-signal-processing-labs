package dsp.task3.logic.filter;

public enum FilterType {
    LOW_PASS("filtr dolnoprzepustowy"),
    HIGH_PASS("filtr górnoprzepustowy");

    private final String name;

    FilterType(String name) { this.name = name; }
    public String getName() { return name; }

    @Override
    public String toString() { return name; }
}
