package shaders.densityfunctions;
/**
 *
 * @author MAZ
 */

import primitives.Vector3D;

public abstract class MicrofacetDensityFunction {
  
  // Devuelve la fracción de microfacetas que cuya normal
  // está orientada en la dirección del vector h.
  // n es la normal geométrica
  public abstract float getProbability (final Vector3D n,
                                        final Vector3D h);
  
  // Devuelve el factor de normalización a aplicar
  public abstract float getNormalizationFactor ();
  
}