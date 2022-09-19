package com.example.juliasetgui.julia_set.domain;

import java.awt.image.BufferedImage;

public class JuliaSetService {

    private final JuliaSegGpuCalculator juliaSetCalculator;

    public JuliaSetService() {
        this.juliaSetCalculator = new JuliaSegGpuCalculator();
    }

    public BufferedImage generate(JuliaSetConfig juliaSetConfig) {
        return juliaSetCalculator.calculate(juliaSetConfig);
    }
}
