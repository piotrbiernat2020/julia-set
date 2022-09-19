package com.example.juliasetgui.julia_set.domain;

import java.awt.image.BufferedImage;

public interface JuliaSetCalculator {
    BufferedImage calculate(JuliaSetConfig config);
}
