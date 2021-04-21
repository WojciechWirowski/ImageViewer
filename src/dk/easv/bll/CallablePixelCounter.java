package dk.easv.bll;

import dk.easv.be.PixelPhoto;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

public class CallablePixelCounter implements Callable<PixelPhoto> {

    private BufferedImage image;
    private int red = 0;
    private int blue = 0;
    private int green = 0;
    private int mixed = 0;

    public CallablePixelCounter(Image img) {
        this.image = SwingFXUtils.fromFXImage(img, null);
    }

    @Override
    public PixelPhoto call() throws Exception {
        for(int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                Color color = new Color(pixel, true);

                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                if(r>g && r>b) red++;
                if(g>r && g>b) green++;
                if(b>r && b>g) blue++;
                else mixed++;
            }
        }
        return new PixelPhoto(red, blue, green, mixed);
    }
}
