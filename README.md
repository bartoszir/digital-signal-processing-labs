# Digital Signal Processing
_Cyfrowe przetwarzanie sygnału_

This repository contains assignments and implementations developed as part of the **Digital Signal Processing** course at Lodz University of Technology.

## Contents

| Task | Description |
|------|-------------|
| [Task 1 — Signal Generation](./task1-signal-generation) | Desktop application for generating, analyzing and operating on signals and noise |

## Task 1 — Signal and Noise Generation

A JavaFX desktop application for generating and analyzing digital signals.

### Features
- Generate 11 types of signals and noise (sinusoidal, rectangular, triangular, Gaussian noise, impulse noise and more)
- Configure signal parameters (amplitude, duration, sampling frequency, period, duty cycle etc.)
- Visualize signals as time-domain plots and histograms
- Compute signal statistics: mean, absolute mean, RMS, variance, average power
- Perform arithmetic operations on signals (add, subtract, multiply, divide)
- Save and load signals in a custom binary format (`.bin`)

### Screenshots

**Signal generation — sinusoidal signal**
<img src="./docs/screenshots/generate-sin-signal.png" width="800"/>

**Histogram**
<img src="./docs/screenshots/files-histogram.png" width="800"/>

**Signal operations**
<img src="./docs/screenshots/signal-operation.png" width="800"/>

### Tech Stack
- **Java 23**
- **JavaFX** — UI and charts
- **Maven** — build tool

### Requirements
- Java 23+
- Maven 3.8+

### Run
```bash
cd task1-signal-generation
mvn javafx:run
```

## Technologies
- Language: `Java`
- Academic year: `2025/2026`
- Course: *Digital Signal Processing*
- University: *Lodz University of Technology*