package dsp.task1.logic.service;

import dsp.common.model.Sample;
import dsp.common.model.SignalData;

import java.util.List;

public class SignalComparisonService {

    public record ComparisonResult(double mse, double snr, double psnr, double md) {}

    public ComparisonResult compare(SignalData original, SignalData reconstructed) {
        List<Sample> orig = original.getSamples();
        List<Sample> rec = reconstructed.getSamples();

        int n = Math.min(orig.size(), rec.size());
        if (n == 0) {
            throw new IllegalArgumentException("Sygnały nie zawierają próbek.");
        }

        double mse = 0.0;
        double signalPower = 0.0;
        double md = 0.0;
        double maxOriginal = 0.0;

        for (int i = 0; i < n; i++) {
            double x = orig.get(i).getValue();
            double xHat = rec.get(i).getValue();
            double diff = x - xHat;

            mse += diff * diff;
            signalPower += x * x;
            md = Math.max(md, Math.abs(diff));
            maxOriginal = Math.max(maxOriginal, Math.abs(x));
        }

        mse /= n;
        signalPower /= n;

        double snr = 10.0 * Math.log10(signalPower / mse);
        double psnr = 10.0 * Math.log10((maxOriginal * maxOriginal) / mse);

        return new ComparisonResult(mse, snr, psnr, md);
    }
}
