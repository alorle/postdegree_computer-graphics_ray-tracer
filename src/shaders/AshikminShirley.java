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

public final class AshikminShirley extends BRDF {

  private static final double DIFFUSE_CORRECTION_FACTOR = 28.0 / (23.0 * Math.PI);

  private final FilterRGB specularComplement;
  private final MicrofacetDensityFunction densityFunction;

  public AshikminShirley(final FilterRGB diffuse,
          final FilterRGB specular,
          final MicrofacetDensityFunction densityFunction) {
    super(diffuse, specular);
    this.specularComplement = specular.complement();
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
