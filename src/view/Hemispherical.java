package view;

import primitives.Vector3D;
import primitives.Point3D;
import tracer.RayGenerator;
import tracer.Ray;

public class Hemispherical extends Projection {

  public Hemispherical(final float factor) {
    super(2.0f * factor, 2.0f * factor);
  }

  @Override
  public RayGenerator getRayGenerator(final Camera c, final int W, final int H) {
    return new HemisphericalFisheyeRayGenerator(c, W, H);
  }

  static private class HemisphericalFisheyeRayGenerator extends RayGenerator {

  }

}
