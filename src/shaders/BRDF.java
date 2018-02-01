package shaders;
/**
 *
 * @author MAZ
 */

import lights.RadianceRGB;
import lights.LightGroup;
import primitives.Point3D;
import objects.Group3D;
import tracer.Hit;

public abstract class BRDF {
  
  protected static final float INV_PI = (float) (1.0 / Math.PI);  
  
  protected final FilterRGB diffuse;
  protected final FilterRGB specular;

  public BRDF (final FilterRGB diffuse,
               final FilterRGB specular) {
    this.diffuse = diffuse;
    this.specular = specular;
  }
  
  protected RadianceRGB specularFilter (final RadianceRGB radiance) {
    return specular.filter(radiance);
  }

  // Recorre las fuentes de luz calculando la radiancia reflejada
  // hacia el punto V debida a la radiancia incidente desde la fuente.
  protected abstract RadianceRGB getDirectRadiance (final LightGroup lights,
                                                    final Group3D scene,
                                                    final Hit hit,
                                                    final Point3D V);

}