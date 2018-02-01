package lights;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import objects.Group3D;
import tracer.Hit;

public class Ambiental extends Light {
  
  final RadianceRGB radiantIntensity;
  
  public Ambiental (final SpectrumRGB spectrum, final float power) {
    super(null, spectrum, power);
    radiantIntensity = spectrum.distribute(power);
  }  
  
  @Override
  public RadianceRGB irradianceAt (final Hit hit, final Group3D scene) {
    return radiantIntensity;
  }
  
}