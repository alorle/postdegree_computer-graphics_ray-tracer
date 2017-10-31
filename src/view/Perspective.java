package view;

import tracer.RayGenerator;
import primitives.Point3D;
import tracer.Ray;

public class Perspective extends Projection {

  public Perspective (final float fov, final float aspect) {
    super((float) (2.0 * Math.tan(Math.toRadians(0.5 * fov))) * aspect,
          (float) (2.0 * Math.tan(Math.toRadians(0.5 * fov))));
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new PerspectiveRayGenerator(c, W, H);
  }

  static private class PerspectiveRayGenerator extends RayGenerator {

  }

}