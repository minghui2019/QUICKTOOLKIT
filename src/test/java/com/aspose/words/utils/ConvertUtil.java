package com.aspose.words.utils;

public class ConvertUtil {
    private ConvertUtil() {
    }

    public static double pointToPixel(double points) {
        return com.aspose.words.ConvertUtil.pointToPixel(points);
    }

    public static double pointToPixel(double points, double resolution) {
        return com.aspose.words.ConvertUtil.pointToPixel(points, resolution);
    }

    public static double pixelToPoint(double pixels) {
        return com.aspose.words.ConvertUtil.pixelToPoint(pixels);
    }

    public static double pixelToPoint(double pixels, double resolution) {
        return com.aspose.words.ConvertUtil.pixelToPoint(pixels, resolution);
    }

    public static int pixelToNewDpi(double pixels, double oldDpi, double newDpi) {
        return com.aspose.words.ConvertUtil.pixelToNewDpi(pixels, oldDpi, newDpi);
    }

    public static double inchToPoint(double inches) {
        return com.aspose.words.ConvertUtil.inchToPoint(inches);
    }

    public static double pointToInch(double points) {
        return com.aspose.words.ConvertUtil.inchToPoint(points);
    }

    public static double millimeterToPoint(double millimeters) {
        return com.aspose.words.ConvertUtil.millimeterToPoint(millimeters);
    }

    public static double pointToMillimeter(double point) {
        return point / 2.834645669291339D;
    }

    public static double millimeterToInch(double millimeters) {
        return millimeters * 0.03937007874015748d;
    }

    public static double inchToMillimeter(double inch) {
        return inch / 0.03937007874015748d;
    }
}
