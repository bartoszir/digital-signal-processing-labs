package dsp.task1.logic.operations;

public enum SignalOperationType {
    ADD("dodawanie"),
    SUBTRACT("odejmowanie"),
    MULTIPLY("mnożenie"),
    DIVIDE("dzielenie");

    private final String name;

    SignalOperationType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
