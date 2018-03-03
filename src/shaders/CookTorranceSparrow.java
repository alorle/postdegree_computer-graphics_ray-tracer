package shaders;

/**
 *
 * @author MAZ
 */
import lights.LightGroup;
import lights.RadianceRGB;
import objects.Group3D;
import primitives.Point3D;
import primitives.Vector3D;
import shaders.densityfunctions.MicrofacetDensityFunction;
import tracer.Hit;

public final class CookTorranceSparrow extends BRDF {

  private final MicrofacetDensityFunction densityFunction;
  private final boolean geometricAttenuation;

  public CookTorranceSparrow(final FilterRGB diffuse,
          final FilterRGB specular,
          final MicrofacetDensityFunction densityFunction,
          final boolean geometricAttenuation) {
    super(diffuse, specular);
    this.densityFunction = densityFunction;
    this.geometricAttenuation = geometricAttenuation;
  }

  @Override
  protected RadianceRGB getDirectRadiance(final LightGroup lights,
          final Group3D scene,
          final Hit hit,
          final Point3D V) {
    return RadianceRGB.NORADIANCE;
  }

}
