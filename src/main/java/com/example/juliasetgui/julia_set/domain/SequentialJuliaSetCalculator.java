//package com.example.juliasetgui.julia_set.domain;
//
//public class SequentialJuliaSetCalculator implements JuliaSetCalculator {
//
//    @Override
//    public int[][] calculate(JuliaSetConfig config) {
//        int width = config.getWidth();
//        int height = config.getHeight();
//        int[][] iterations = new int[width][height];
//        double zoom = config.getZoom();
//        double moveX = config.getMoveX();
//        double moveY = config.getMoveY();
//        int maxIterations = config.getMaxIterations();
//        double cx = config.getCx();
//        double cy = config.getCy();
//
//        long startTime = System.currentTimeMillis();
//
//        for (int x = 0; x < width; x++) {
//            double zx, zy;
//            for (int y = 0; y < height; y++) {
//                zx = 1.5 * (x - width / 2) / (0.5 * zoom * width) + moveX;
//                zy = (y - height / 2) / (0.5 * zoom * height) + moveY;
//                int i = 0 ;
//
//                while (zx * zx + zy * zy < 4 && i <= maxIterations) {
//                    double tmp = zx * zx - zy * zy + cx;
//                    zy = 2.0 * zx * zy + cy;
//                    zx = tmp;
//                    i++;
//                }
//                iterations[x][y] = i;
//            }
//        }
//
//        long endTime = System.currentTimeMillis();
//
//        System.out.println(endTime - startTime);
//
//        return iterations;
//    }
//}
