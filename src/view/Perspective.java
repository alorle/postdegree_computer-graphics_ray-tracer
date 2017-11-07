package view;

import tracer.RayGenerator;
import primitives.Point3D;
import tracer.Ray;

public class Perspective extends Projection {

  public Perspective(final float fov, final float aspect) {
    super((float) (2.0 * Math.tan(Math.toRadians(0.5 * fov))) * aspect,
            (float) (2.0 * Math.tan(Math.toRadians(0.5 * fov))));
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new PerspectiveRayGenerator(c, W, H);
  }

  static private class PerspectiveRayGenerator extends RayGenerator {

    private PerspectiveRayGenerator(Camera c, int W, int H) {
      super(c, W, H);
    }

    @Override
    public Ray getRay(final int m, final int n) {
      // Obtenemos las coordenadas del punto central de la celda a colorear
      final float x = (float) (m + 0.5) * wW - whalf;
      final float y = (float) (n + 0.5) * hH - hhalf;
      final float z = -1.0f;

      // A partir de las coordenadas, construimos el punto de partida del
      // rayo (Q)
      final Point3D Q = new Point3D(x, y, z);

      // Cambiamos el punto de partida del rayo al sistema de coordenadas
      // de la escena
      camera.toSceneCoord(Q);

      // Creamos el rayo que pasa por el punto donde se posiciona la
      // cámara y el punto (Q) de partida
      return new Ray(camera.getPosition(), Q);
    }

  }

}
