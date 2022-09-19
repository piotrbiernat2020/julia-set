package com.example.juliasetgui.julia_set.domain;


import org.jocl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.jocl.CL.*;

public class JuliaSegGpuCalculator implements JuliaSetCalculator {

    private int width = 0;
    private int height = 0;
    private cl_command_queue commandQueue;
    private cl_kernel kernel;
    private cl_mem pixelMem;
    private cl_mem colorMapMem;

    private int colorMap[];

    private void initCL() {
        final int platformIndex = 0;
        final long deviceType = CL_DEVICE_TYPE_ALL;
        final int deviceIndex = 0;

        // Enable exceptions and subsequently omit error checks in this sample
        CL.setExceptionsEnabled(true);

        // Obtain the number of platforms
        int numPlatformsArray[] = new int[1];
        clGetPlatformIDs(0, null, numPlatformsArray);
        int numPlatforms = numPlatformsArray[0];

        // Obtain a platform ID
        cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
        clGetPlatformIDs(platforms.length, platforms, null);
        cl_platform_id platform = platforms[platformIndex];

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);

        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device ID
        cl_device_id devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
        cl_device_id device = devices[deviceIndex];

        // Create a context for the selected device
        cl_context context = clCreateContext(
                contextProperties, 1, new cl_device_id[]{device},
                null, null, null);

        // Create a command-queue for the selected device
        cl_queue_properties properties = new cl_queue_properties();
        commandQueue = clCreateCommandQueueWithProperties(
                context, device, properties, null);

        // Program Setup
        String source =
                readFile("src/main/resources/kernel/juliaSet.cl");

        // Create the program
        cl_program cpProgram = clCreateProgramWithSource(context, 1,
                new String[]{source}, null, null);

        // Build the program
        clBuildProgram(cpProgram, 0, null, "-cl-mad-enable", null, null);

        // Create the kernel
        kernel = clCreateKernel(cpProgram, "computeJuliaSet", null);

        // Create the memory object which will be filled with the
        // pixel data
        pixelMem = clCreateBuffer(context, CL_MEM_WRITE_ONLY,
                width * height * Sizeof.cl_uint, null, null);

        // Create and fill the memory object containing the color map
        initColorMap(32, Color.RED, Color.GREEN, Color.BLUE);
        colorMapMem = clCreateBuffer(context, CL_MEM_READ_WRITE,
                colorMap.length * Sizeof.cl_uint, null, null);
        clEnqueueWriteBuffer(commandQueue, colorMapMem, true, 0,
                colorMap.length * Sizeof.cl_uint, Pointer.to(colorMap), 0, null, null);
    }

    private String readFile(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initColorMap(int stepSize, Color... colors) {
        colorMap = new int[stepSize * colors.length];
        int index = 0;
        for (int i = 0; i < colors.length - 1; i++) {
            Color c0 = colors[i];
            int r0 = c0.getRed();
            int g0 = c0.getGreen();
            int b0 = c0.getBlue();

            Color c1 = colors[i + 1];
            int r1 = c1.getRed();
            int g1 = c1.getGreen();
            int b1 = c1.getBlue();

            int dr = r1 - r0;
            int dg = g1 - g0;
            int db = b1 - b0;

            for (int j = 0; j < stepSize; j++) {
                float alpha = (float) j / (stepSize - 1);
                int r = (int) (r0 + alpha * dr);
                int g = (int) (g0 + alpha * dg);
                int b = (int) (b0 + alpha * db);
                int rgb =
                        (r << 16) |
                                (g << 8) |
                                (b << 0);
                colorMap[index++] = rgb;
            }
        }
    }

    @Override
    public BufferedImage calculate(JuliaSetConfig config) {
        width = config.getWidth();
        height = config.getHeight();
        initCL();
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        long globalWorkSize[] = new long[2];
        globalWorkSize[0] = width;
        globalWorkSize[1] = height;
        clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(pixelMem));
        clSetKernelArg(kernel, 1, Sizeof.cl_uint, Pointer.to(new int[]{width}));
        clSetKernelArg(kernel, 2, Sizeof.cl_uint, Pointer.to(new int[]{height}));
        clSetKernelArg(kernel, 3, Sizeof.cl_float, Pointer.to(new float[]{config.getCx()}));
        clSetKernelArg(kernel, 4, Sizeof.cl_float, Pointer.to(new float[]{config.getCy()}));
        clSetKernelArg(kernel, 5, Sizeof.cl_int, Pointer.to(new int[]{config.getMaxIterations()}));
        clSetKernelArg(kernel, 6, Sizeof.cl_mem, Pointer.to(colorMapMem));
        clSetKernelArg(kernel, 7, Sizeof.cl_int, Pointer.to(new int[]{colorMap.length}));

        clEnqueueNDRangeKernel(commandQueue, kernel, 2, null,
                globalWorkSize, null, 0, null, null);

        DataBufferInt dataBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
        int data[] = dataBuffer.getData();

        clEnqueueReadBuffer(commandQueue, pixelMem, CL_TRUE, 0,
                Sizeof.cl_int * height * width, Pointer.to(data), 0, null, null);


        return image;
    }


}
