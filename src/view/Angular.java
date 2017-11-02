package view;

/**
 *
 * @author MAZ
 */
import primitives.Point3D;
import tracer.RayGenerator;
import tracer.Ray;

public class Angular extends Projection {

  public Angular(final float omega) {
    super((float) (2.0 * Math.sin(Math.toRadians(omega * 0.5f))),
            (float) (2.0 * Math.sin(Math.toRadians(omega * 0.5f))));
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new AngularRayGenerator(c, W, H);
  }

  static private class AngularRayGenerator extends RayGenerator {

  }

}
