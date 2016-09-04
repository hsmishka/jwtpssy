package jwtpssy;

import java.io.BufferedInputStream;
import java.io.IOException;

public class CameraReader implements Runnable {
  public static final int IMG_HEIGHT = 480;
  public static final int IMG_WIDTH = 640;
  public static final int TOTAL_PIXELS_GRAYSCALE = IMG_HEIGHT * IMG_WIDTH;
  public static final int TOTAL_PIXELS_YUV = IMG_HEIGHT * IMG_WIDTH * 3;

  public static final int FPS = 30;
  public static final String RASPIVID =
      "./raspividyuv " + "-w " + IMG_WIDTH + " -h " + IMG_HEIGHT + // image
                                                                   // dimension
          " -fps " + FPS + // fps
          " -t 0 " + // no timeout
          " -o -"; // std output

  private byte[] yuvBuf = new byte[TOTAL_PIXELS_YUV];
  private int[] gsBuf = new int[TOTAL_PIXELS_GRAYSCALE];

  @Override
  public void run() {

    try {
      // launch video process
      Process p = Runtime.getRuntime().exec(RASPIVID);
      BufferedInputStream bis = new BufferedInputStream(p.getInputStream());

      System.out.println("start camera");
      long frameCaptureStart = 0;
      long frameCaptureFinish = 0;
      long timeSpent = 0;
      int framesProcessed = 0;
      
      while (true) {
        frameCaptureStart = System.currentTimeMillis();
        if (bis.read(yuvBuf, 0, TOTAL_PIXELS_YUV) < 0) {
          break;
        }
        for (int yuvBufIdx = 0, gsBufIdx =
            0; yuvBufIdx < TOTAL_PIXELS_YUV; yuvBufIdx += 3, gsBufIdx++) {
          gsBuf[gsBufIdx] = yuvBuf[yuvBufIdx];
        }
        framesProcessed++;
        frameCaptureFinish = System.currentTimeMillis();
        
        timeSpent += frameCaptureFinish - frameCaptureStart;
        if (timeSpent > 1000) {
          System.out.println("fps:" + framesProcessed);
          timeSpent -= 1000;
          framesProcessed = 0;
        }
      }

      System.out.println("end camera");
      p.destroy();
      bis.close();
      System.exit(0);

    } catch (IOException ieo) {
      ieo.printStackTrace();
    }
  }

}
