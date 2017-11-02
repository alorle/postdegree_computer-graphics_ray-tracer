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

  }

}
