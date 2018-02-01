package shaders.densityfunctions;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import primitives.Vector3D;

public class Torrance1 extends MicrofacetDensityFunction {
  
  static private final double SQRTLOG2 = Math.sqrt(Math.log(2));

  private final float b;
  private final float normalizationFactor;
  
  public Torrance1 (final float beta) {
    if ((Math.signum(beta) < 0) || (beta > 90))
      throw new IllegalArgumentException("beta debe estar en el intervalo [0,90]");    
    this.b = (float) (SQRTLOG2 / beta);
    this.normalizationFactor = 1;
  }
  
  @Override
  public float getProbability (final Vector3D n,
                               final Vector3D h) { 
    final float cosAlpha = h.dot(n);
    final double alpha = Math.acos(cosAlpha);
    final double exponente = (b * alpha) * (b * alpha);
    return (float) Math.exp(-exponente);
  }
  
  @Override
  public float getNormalizationFactor () {
    return normalizationFactor;
  }   
  
}