package controller;

import view.Canvas;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SaveController {
    public void save(Canvas canvas, File file) throws IOException {
        BufferedImage image = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas.paint(image.getGraphics());
        ImageIO.write(image, "png", file);
    }
}
