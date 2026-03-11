package dsp.task1.logic;

import dsp.task1.logic.signal.DiscreteSignal;
import dsp.task1.logic.signal.Signal;
import org.knowm.xchart.*;

import java.util.List;

public class SignalPlotter {

    public static void plot(Signal signal, List<Sample> samples) {

        double[] xData = new double[samples.size()];
        double[] yData = new double[samples.size()];

        for (int i = 0; i < samples.size(); i++) {
            xData[i] = samples.get(i).getTime();
            yData[i] = samples.get(i).getValue();
        }

        XYChart chart = new XYChartBuilder()
                .width(1000)
                .height(600)
                .title("Signal")
                .xAxisTitle("Time")
                .yAxisTitle("Amplitude")
                .build();

//        chart.addSeries("signal", xData, yData);
        XYSeries series = chart.addSeries("signal", xData, yData);

        if (signal instanceof DiscreteSignal) {
            series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        } else {
            series.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        }

        new SwingWrapper<>(chart).displayChart();
    }
}
