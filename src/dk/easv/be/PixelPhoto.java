package dk.easv.be;

public class PixelPhoto {

    private int red;
    private int blue;
    private int green;
    private int mixed;

    public PixelPhoto(int red, int blue, int green, int mixed) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.mixed = mixed;

    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getMixed() {
        return mixed;
    }

    public void setMixed(int mixed) {
        this.mixed = mixed;
    }
}
