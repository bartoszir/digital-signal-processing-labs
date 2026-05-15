package dsp.task1.logic.signal;

public enum SignalType {

    UNIFORM_NOISE("szum o rozkładzie jednostajnym"),
    GAUSSIAN_NOISE("szum gaussowski"),
    SINUSOIDAL_SIGNAL("sygnał sinusoidalny"),
    ONE_HALF_RECTIFIED_SINUSOIDAL_SIGNAL("sygnał sinusoidalny wyprostowany jednopołówkowo"),
    TWO_HALF_RECTIFIED_SINUSOIDAL_SIGNAL("sygnał sinusoidalny wyprostowany dwupołówkowo"),
    RECTANGULAR_SIGNAL("sygnał prostokątny"),
    SYMMETRIC_RECTANGULAR_SIGNAL("sygnał prostokątny symetryczny"),
    TRIANGULAR_SIGNAL("sygnał trójkątny"),
    UNIT_JUMP_SIGNAL("skok jednostkowy"),
    UNIT_IMPULSE_SIGNAL("impuls jednostkowy"),
    IMPULSE_NOISE_SIGNAL("szum impulsowy"),
    OPERATION_RESULT("wynik operacji");

    private final String name;

    SignalType(String name) {
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
