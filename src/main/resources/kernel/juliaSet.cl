__kernel void computeJuliaSet(
    __global uint *output,
    int sizeX, int sizeY,
    float cx, float cy,
    int maxIterations,
    __global uint *colorMap,
    int colorMapSize
    )
{
    unsigned int ix = get_global_id(0);
    unsigned int iy = get_global_id(1);

    float x = 0;
    float y = 0;

    x = (1.5 * (ix - sizeX * 1.0 / 2)) / (0.5 * sizeX);
    y = (iy - sizeY * 1.0 / 2) / (0.5 * sizeY);

    float magnitudeSquared = 0;
    int iteration = 0;
    while (iteration<maxIterations && magnitudeSquared<4)
    {
        float xx = x*x;
        float yy = y*y;
        y = 2*x*y+cy;
        x = xx-yy+cx;
        magnitudeSquared=xx+yy;
        iteration++;
    }
    if (iteration == maxIterations)
    {
        output[iy*sizeX+ix] = 0;
    }
    else
    {
        float alpha = (float)iteration/maxIterations;
        int colorIndex = (int)(alpha * colorMapSize);
        output[iy*sizeX+ix] = colorMap[colorIndex];
	}
}