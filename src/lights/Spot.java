package lights;

/**
 * Fuente luminosa spot
 *
 * @author MAZ
 */

import primitives.Point3D;
import primitives.Vector3D;
import objects.Group3D;
import tracer.Hit;
import tracer.Ray;

public class Spot extends Light {

  /**
   * Vector de orientación de la fuente
   */
  private final Vector3D direction;

  /**
   * Exponente de atenuación.
   */
  protected float attenuationExponent = 0;

  /**
   * Coseno del ángulo de apertura
   */
  protected final float apertureIndex;

  /**
   * Intensidad radiante en cada dirección
   */
  private final RadianceRGB radiantIntensity;

  /**
   * Constructor:
   *
   * @param position Emplazamiento de la fuente
   * @param lookAt Pundo de referencia, hacia donde "enfoca" la fuente
   * @param aperture Ángulo de apertura EN GRADOS [0-90]
   * @param spectrum Espectro de radiación en canales RGB
   * @param power Potencia (intensidad) de emisión
   * @throws java.lang.IllegalArgumentException Sí
   */
  public Spot(
          final Point3D position,
          final Point3D lookAt,
          final float aperture,
          final SpectrumRGB spectrum,
          final float power) throws IllegalArgumentException {
    super(position, spectrum, power);
    if (aperture > 90.0f) {
      throw new IllegalArgumentException("El ángulo de apertura proporcionado (" + aperture + "º) es inválido, debe estar comprendido entre 0º y 90º.");
    }
    direction = null;
    apertureIndex = 0;
    radiantIntensity = null;
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {

    return RadianceRGB.NORADIANCE;
  }

  /**
   * @return the attenuationIndex
   */
  public float getAttenuationExponent() {
    return attenuationExponent;
  }

  /**
   * @param attenuationExponent the attenuationExponent to set
   */
  public void setAttenuationExponent(float attenuationExponent) {
    this.attenuationExponent = attenuationExponent;
  }

}
