package com.example.juliasetgui.julia_set.domain;

public class JuliaSetConfig {

    private int width;
    private int height;
    private int maxIterations;
    private float cx;
    private float cy;

    public JuliaSetConfig(
            final int width,
            final int height,
            final int maxIterations,
            final float cx,
            final float cy
    ) {

        this.width = width;
        this.height = height;
        this.maxIterations = maxIterations;
        this.cx = cx;
        this.cy = cy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

}
