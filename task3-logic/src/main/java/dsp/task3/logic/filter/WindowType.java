package dsp.task3.logic.filter;

public enum WindowType {
    RECTANGULAR("okno prostokątne"),
    HAMMING("okno Hamminga");

    private final String name;

    WindowType(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
