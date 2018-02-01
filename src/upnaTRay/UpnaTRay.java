package upnaTRay;

/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */
import java.io.File;
import java.io.FileNotFoundException;

import view.Camera;
import gui.Image;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import objects.Group3D;
import lights.LightGroup;
import org.xml.sax.SAXException;
import parser.Parser;

public final class UpnaTRay {

  /**
   * Main.
   *
   * @param args
   * @throws FileNotFoundException
   * @throws IOException
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   */
  public static void main(final String[] args)
          throws FileNotFoundException, IOException, ParserConfigurationException, SAXException {

    final String path = System.getProperty("user.dir") + File.separator
            + "scenes" + File.separator;
    final File inputFile = new File(path + args[0]);
    final Parser parser = new Parser(inputFile);

    final Image image = parser.getViewport();
    final Camera camera = parser.getCamera();
    final Group3D scene = parser.getScene();
    final LightGroup lights = parser.getLights();

    final long t0 = System.nanoTime();
    if (lights.size() > 0) {
      image.synthesis(camera, scene, lights);
    } else {
      image.synthesis(camera, scene);
    }
    final float t1 = (float) ((System.nanoTime() - t0) / 1E+9);

    System.out.println("Rendering time: " + t1 + " seconds");

    image.show();

  }

}
