package dsp.task1.logic;

import org.knowm.xchart.*;

import java.util.List;

public class SignalPlotter {

    public static void plot(List<Sample> samples) {

        double[] xData = new double[samples.size()];
        double[] yData = new double[samples.size()];

        for (int i = 0; i < samples.size(); i++) {
            xData[i] = samples.get(i).getTime();
            yData[i] = samples.get(i).getValue();
        }

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .title("Signal")
                .xAxisTitle("Time")
                .yAxisTitle("Amplitude")
                .build();

        chart.addSeries("signal", xData, yData);

        new SwingWrapper<>(chart).displayChart();
    }
}
