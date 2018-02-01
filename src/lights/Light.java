package lights;
/**
 * Clase genérica para fuente luminosa
 *  
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import primitives.Point3D;
import objects.Group3D;
import tracer.Hit;

public abstract class Light {

  /**
   * Posición de la fuente en la escena.
   */
  protected final Point3D S;

  /**
   * Potencia de emisión.
   *
   */
  protected final float power;

  /**
   * Fraccion de la potencia de emisión en cada canal RGB.
   */
  protected final SpectrumRGB emissionSpectrum;

  protected Light (final Point3D position, final SpectrumRGB spectrum, final float power) {
    this.S = position;
    this.emissionSpectrum = spectrum;
    this.power = power;
  }

  /**
   * Nivel de irradiancia que aporta esta fuente al punto de impacto indicado.
   *
   * @param scene La escena
   * @param hit Punto de impacto en forma de objeto Hit
   * @return Cero si el punto de impacto no recibe irradiancia desde esta fuente
   */
  public abstract RadianceRGB irradianceAt (final Hit hit, final Group3D scene);

  /**
   * Concultor de punto de emplazamiento S
   *
   * @return
   */
  public Point3D getPosition () {
    return S;
  }
  
}