package shaders;

import shaders.densityfunctions.MicrofacetDensityFunction;
import lights.RadianceRGB;
import lights.LightGroup;
import objects.Group3D;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

public final class PhongLafortune extends BRDF {

  private final MicrofacetDensityFunction densityFunction;
  private final FilterRGB ambient;

  public PhongLafortune(final FilterRGB ambient,
          final FilterRGB diffuse,
          final FilterRGB specular,
          final MicrofacetDensityFunction densityFunction) {
    super(diffuse, specular);
    this.ambient = ambient;
    this.densityFunction = densityFunction;
  }

  @Override
  protected RadianceRGB getDirectRadiance(final LightGroup lights,
          final Group3D scene,
          final Hit hit,
          final Point3D V) {
    return null;
  }

}
