package com.example.juliasetgui.julia_set.view_controller;

import com.example.juliasetgui.julia_set.domain.JuliaSetConfig;
import com.example.juliasetgui.julia_set.domain.JuliaSetService;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;

public class JuliaSetViewController {

    @FXML
    private ImageView juliaImageView;

    public void setJuliaSetConfig(JuliaSetConfig juliaSetConfig) {
        var juliaService = new JuliaSetService();
        var juliaSet = juliaService.generate(juliaSetConfig);

        var width = juliaSetConfig.getWidth();
        var height = juliaSetConfig.getHeight();
        WritableImage wi = new WritableImage(width, height);
        PixelWriter pw = wi.getPixelWriter();


        juliaImageView.setImage(convertToFxImage(juliaSet));
        System.out.println(juliaSetConfig.getHeight());
    }

    private Color calculateColor(Integer val, Integer maxIterations) {
        if (val == maxIterations) {
            return Color.color(0, 0, 0);
        }
        var fval = Math.sqrt(val * 1.0 / maxIterations * 1.0);
        var base = Math.PI / 2.0 * fval;
        Double r = Math.pow(Math.sin(base), 2) * 255;
        Double g = Math.pow(Math.sin(3.0 * base), 2) * 255;
        Double b = Math.pow(Math.sin(7.0 * base), 2) * 255;
        return Color.rgb(r.intValue(), g.intValue(), b.intValue());
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
}
