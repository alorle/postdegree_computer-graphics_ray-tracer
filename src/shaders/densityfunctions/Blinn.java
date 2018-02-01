package shaders.densityfunctions;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import primitives.Vector3D;


public final class Blinn extends MicrofacetDensityFunction {
  
  private final float b;
  private final float normalizationFactor;
  
  public Blinn (final float beta) {
    if ((Math.signum(beta) < 0) || (beta > 90))
      throw new IllegalArgumentException("beta debe estar en el intervalo [0,90]");
    this.b = (float) (-Math.log(2) / Math.log(Math.cos(Math.toRadians(beta))));
    this.normalizationFactor = (float) ((b + 2) / (2 * Math.PI));
  }
  
  @Override
  public float getProbability (final Vector3D n,
                               final Vector3D h) {
    final float x = (float) Math.pow(n.dot(h), b);
    return (Math.signum(x) > 0) ? x : 0.0f;
  }
  
  @Override
  public float getNormalizationFactor () {
    return normalizationFactor;
  }  

}