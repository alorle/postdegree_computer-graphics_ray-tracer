package view;

import tracer.RayGenerator;
import primitives.Point3D;
import tracer.Ray;

public class Orthographic extends Projection {

  public Orthographic(final float h, final float aspect) {
    super(h * aspect, h);
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new OrtographicRayGenerator(c, W, H);
  }

  static private class OrtographicRayGenerator extends RayGenerator {

    private OrtographicRayGenerator(final Camera c, final int W, final int H) {
      super(c, W, H);
    }

    @Override
    public Ray getRay(final int m, final int n) {
      // Obtenemos las coordenadas del punto central de la celda a colorear
      final float x = (float) (m + 0.5) * wW - whalf;
      final float y = (float) (n + 0.5) * hH - hhalf;
      final float z = 0.0f;

      // A partir de las coordenadas, construimos el punto de partida del
      // rayo (R)
      final Point3D R = new Point3D(x, y, z);

      // Cambiamos el punto de partida del rayo al sistema de coordenadas
      // de la escena
      camera.toSceneCoord(R);

      // Creamos el rayo desde el punto (R) de partida en la dirección de
      // la vista
      return new Ray(R, camera.getLook());
    }

  }

}
