package upnaTRay;

import java.io.File;
import java.io.FileNotFoundException;

import view.Camera;
import gui.Image;
import java.util.Scanner;
import objects.Group3D;
import parser.Parser;

public final class UpnaTRay {

  /**
   * Main.
   *
   * @param args
   * @throws FileNotFoundException
   * @throws Exception
   */
  public static void main(final String[] args)
          throws FileNotFoundException, Exception {

    final String path = System.getProperty("user.dir") + File.separator
            + "scenes" + File.separator;

    String inputFileName;
    if (args.length < 1) {
      final Scanner input = new Scanner(System.in);
      System.out.print("Input file name?: ");
      inputFileName = input.nextLine() + ".xml";
    } else {
      inputFileName = args[0];
    }

    final File inputFile = new File(path + inputFileName);
    final Parser parser = new Parser(inputFile);

    final Image image = parser.getViewport();
    final Camera camera = parser.getCamera();
    final Group3D scene = parser.getScene();

    System.out.println("Rendering " + inputFile.getName());

    final long t0 = System.nanoTime();
    image.synthesis(camera, scene);
    final float t1 = (float) ((System.nanoTime() - t0) / 1E+9);

    System.out.println("Rendering time: " + t1 + " seconds");

    image.show();

  }

}
