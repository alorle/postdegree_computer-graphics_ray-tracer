package shaders.densityfunctions;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import primitives.Vector3D;

public class Torrance2 extends MicrofacetDensityFunction {
  
  static private final double SQRT2 = Math.sqrt(2);
  
  private final float b;
  private final float normalizationFactor;
  
  public Torrance2 (final float beta) {
    if ((Math.signum(beta) < 0) || (beta > 90))
      throw new IllegalArgumentException("beta debe estar en el intervalo [0,90]");    
    final double cosBeta = Math.cos(Math.toRadians(beta));
    final double cosBeta2 = cosBeta * cosBeta;
    this.b = (float) ((cosBeta2 - 1) / (cosBeta2 - SQRT2));
    this.normalizationFactor = 1;
  }
  
  @Override
  public float getProbability (final Vector3D n,
                               final Vector3D h) { 
    final float cosAlpha = h.dot(n);
    final float num = b;
    final float den = cosAlpha * cosAlpha * (b - 1) + 1;
    final float x = num / den;
    return x * x;
  }
  
  @Override
  public float getNormalizationFactor () {
    return normalizationFactor;
  }   
  
}