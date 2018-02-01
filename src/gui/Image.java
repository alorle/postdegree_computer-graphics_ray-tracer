package gui;

/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import view.Camera;
import lights.LightGroup;
import objects.Group3D;
import objects.Object3D;
import primitives.Point3D;
import shaders.Material;
import tracer.Hit;
import tracer.Ray;
import tracer.RayGenerator;

public class Image {

  static private final int HEIGHT = 128;
  static private final int WIDTH = 128;
  static private final Color BACKGROUND_COLOR = Color.black;

  // ATRIBUTOS
  private final BufferedImage mosaic;
  private final Color backgroundColor;
  private final int height;
  private final int width;
  private final String tag;

  public Image(final String tag) {
    this(tag, HEIGHT, WIDTH, BACKGROUND_COLOR);
  }

  public Image(final String tag, final int h, final int w, final Color c) {
    this.tag = tag;
    this.height = h;
    this.width = w;
    this.backgroundColor = c;
    mosaic = new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
  }

  // OPERACIONES
  public String getTag() {
    return tag;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public Color getBackgroundColor() {
    return backgroundColor;
  }

  public BufferedImage getMosaic() {
    return mosaic;
  }

  public void synthesis(final Camera camera, final Group3D scene) {

    final int W = getWidth();
    final int H = getHeight();
    final Color background = getBackgroundColor();

    final RayGenerator rg = camera.getRayGenerator(W, H);

    for (int m = 0; m < W; ++m) {

      for (int n = 0; n < H; ++n) {

        final Ray ray = rg.getRay(m, n);

        if (ray.isOperative()) {

          final Hit hit = scene.intersects(ray);

          if (hit.hits()) {

            final Object3D object = hit.getObject();
            // Obtiene el color para el pixel directamente del objeto
            putPixel(m, n, object.getColor());

          } else {
            putPixel(m, n, background);
          }

        } else {
          putPixel(m, n, Color.WHITE);
        }

      }

    }

  }

  public void synthesis(final Camera camera, final Group3D scene, final LightGroup lights) {

    final int W = getWidth();
    final int H = getHeight();
    final Color background = getBackgroundColor();

    final RayGenerator rg = camera.getRayGenerator(W, H);

    for (int m = 0; m < W; ++m) {

      for (int n = 0; n < H; ++n) {

        // Calcula radiancia directa desde fuentes de luz
        // Calcula haz de direcciones para radiancia indirecta
        // Calcula radiancia reflejada desde haz de direcciones
        // Suma componentes de radiancia y calcula color
        final Ray ray = rg.getRay(m, n);

        if (ray.isOperative()) {

          final Hit hit = scene.intersects(ray);

          if (hit.hits()) {

            final Point3D P = ray.getStartingPoint();
            final Material material = hit.getMaterial();
            putPixel(m, n, material.getColor(scene, lights, hit, P));

          } else {
            putPixel(m, n, background);
          }

        } else {
          putPixel(m, n, Color.WHITE);
        }

      }

    }

  }

  public void putPixel(final int m, final int n, final Color c) {
    mosaic.setRGB(m, height - 1 - n, c.getRGB());
  }

  public Color getColor(final int m, final int n) {
    return new Color(mosaic.getRGB(m, height - 1 - n));
  }

  public void show() {

    final JFrame frame = new JFrame(this.getTag());
    frame.getContentPane().setLayout(new FlowLayout());

    frame.getContentPane().add(new JLabel(new ImageIcon(mosaic)));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.repaint();
    frame.setVisible(true);

  }

}
