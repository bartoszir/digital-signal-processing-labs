package dsp.task3.logic.filter;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirFilterDesignerTest {

    @Test
    void shouldReturnCorrectSizeList() {
        assertEquals(7, FirFilterDesigner.designLowPass(7, 4).size());
    }

    @Test
    void shouldThrowExceptionWhenEvenM() {
        assertThrows(IllegalArgumentException.class, () -> FirFilterDesigner.designLowPass(2, 4));
    }

    @Test
    void shouldReturnCorrectValueAtMiddleSample() {
        assertEquals(0.25, FirFilterDesigner.designLowPass(7, 8).get(3));
    }

    @Test
    void convertToHighPassTest() {
        List<Double> h = List.of(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        List<Double> result = FirFilterDesigner.convertToHighPass(h);
        for (int n = 0; n < h.size(); n++) {
            if (n % 2 == 0) {
                assertTrue(h.get(n) * result.get(n) > 0);
            } else {
                assertTrue(h.get(n) * result.get(n) < 0);
            }
        }
    }

    @Test
    void applyHammingWindowTest() {
        List<Double> h = List.of(1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);
        List<Double> result = FirFilterDesigner.applyHammingWindow(h);

        // rozmiar listy wynikowej jest taki sam jak h(n)
        assertEquals(h.size(), result.size());
        // końce są blisko zeru
        assertTrue(result.getFirst() < 0.1);
        assertTrue(result.getLast() < 0.1);
        // środek jest największy
        assertTrue(result.get(3) > result.getFirst());
        assertTrue(result.get(3) > result.getLast());
    }
}
