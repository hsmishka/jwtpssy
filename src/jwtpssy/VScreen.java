package jwtpssy;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class VScreen extends JFrame {
  private int w, h;
  private int[] raster;
  private ColorModel cm;
  private DataBuffer buffer;
  private SampleModel sm;
  private WritableRaster wrRaster;
  private BufferedImage backBuffer;
  private Graphics g;

  public VScreen(String title, int width, int height) {
    w = width;
    h = height;

    setTitle(title);
    setSize(w, h);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    raster = new int[w * h];

    cm = new DirectColorModel(24, 255, 255, 255);
    buffer = new DataBufferInt(raster, raster.length);
    sm = cm.createCompatibleSampleModel(w, h);
    wrRaster = Raster.createWritableRaster(sm, buffer, null);
    backBuffer = new BufferedImage(cm, wrRaster, false, null);

    createBufferStrategy(1);
    g = getBufferStrategy().getDrawGraphics();
  }

  public void drawArray(int[] data) {
    assert data.length == w * h;
    System.arraycopy(data, 0, raster, 0, w * h);
    g.drawImage(backBuffer, 0, 0, null);
  }
}
