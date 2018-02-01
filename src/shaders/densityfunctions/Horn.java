package shaders.densityfunctions;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import primitives.Vector3D;

public final class Horn extends MicrofacetDensityFunction {
  
  private final float q;
  private final float normalizationFactor;
  
  public Horn (final float q) {
    this.q = q;
    this.normalizationFactor = 1;
  }
  
  @Override
  public float getProbability (final Vector3D n,
                               final Vector3D h) {
    final float cosHalfA = n.dot(h);
    final float cosA = 2 * (cosHalfA * cosHalfA) - 1;
    final float x = (float) Math.pow(cosA, q);
    return (Math.signum(x) > 0) ? x : 0.0f;
  }
  
  @Override
  public float getNormalizationFactor () {
    return normalizationFactor;
  }    

}