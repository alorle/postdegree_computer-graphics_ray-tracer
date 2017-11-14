package view;

/**
 *
 * @author MAZ
 */
import primitives.Point3D;
import tracer.RayGenerator;
import tracer.Ray;

public class Angular extends Projection {

  private final float omega;

  public Angular(final float omega) {
    super((float) (2.0 * Math.sin(Math.toRadians(omega * 0.5f))),
            (float) (2.0 * Math.sin(Math.toRadians(omega * 0.5f))));

    this.omega = (float) Math.toRadians(omega);
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new AngularRayGenerator(c, W, H, omega);
  }

  static private class AngularRayGenerator extends RayGenerator {

    private final float omega;
    private final float omegaHalf;
    private final float omegaHalfCos;
    private final float radio;
    private final float radioSquare;

    private AngularRayGenerator(Camera c, int W, int H, float omega) {
      super(c, W, H);

      this.omega = omega;
      omegaHalf = omega * 0.5f;
      omegaHalfCos = (float) Math.cos(omegaHalf);
      radio = (float) Math.sin(omega * 0.5f);
      radioSquare = radio * radio;
    }

    @Override
    public Ray getRay(int m, int n) {
      final float x = (float) (m + 0.5) * wW - whalf;
      final float y = (float) (n + 0.5) * hH - hhalf;

      // Calculamos el cuadrado de la distancia del pixel a colorear al centro
      // de la circunferencia que define el viewport, para saber si está fuera
      // o dentro del circulo
      final float dSquare = x * x + y * y;

      Point3D Q;
      if (dSquare > radioSquare) {
        // Como el pixel está fuera de la circunferencia de proyección,
        // definimos Q de manera que el rayo que obtenemos no sea correcto.
        Q = camera.getPosition();
      } else {
        // Definimos z en función de la ecuación de la esfera que define el
        // casquete de proyección
        final float z = (float) (omegaHalfCos - Math.sqrt(1 - dSquare));

        Q = new Point3D(x, y, z);
        camera.toSceneCoord(Q);
      }

      return new Ray(camera.getPosition(), Q);
    }

  }

}
