package lights;

/**
 * Representa una fuente de luz direccional de sección circular.
 *
 * @author MAZ
 */
import primitives.Point3D;
import primitives.Vector3D;
import objects.Group3D;
import tracer.Hit;
import tracer.Ray;

public class Directional extends Light {

  /**
   * Vector de orientación de la fuente
   */
  private final Vector3D direction;

  /**
   * Exponente de atenuación
   */
  private float attenuationExponent = 0;

  /**
   * Radio al cuadrado de la sección circular
   */
  private final float squareRadius;

  /**
   * Radiosidad (vatios/m^2) en cada punto de la superficie luminosa
   */
  private final RadianceRGB puntualIrradiance;

  /**
   * Constructor.
   *
   * @param position Ubicación del centro de la fuente
   * @param lookAt Punto de referencia hacia donde apunta la fuente
   * @param spectrum Espectro de radiación en canales RGB
   * @param power Potencia (intensidad) de emisión
   * @param radius Radio de la sección circular
   */
  public Directional(
          final Point3D position,
          final Point3D lookAt,
          final float radius,
          final SpectrumRGB spectrum,
          final float power) {
    super(position, spectrum, power);
    direction = null;
    squareRadius = 0;
    puntualIrradiance = null;
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {

    return RadianceRGB.NORADIANCE;
  }

  /**
   * @return the attenuationInder
   */
  public float getAttenuationExponent() {
    return attenuationExponent;
  }

  /**
   * @param attenuationExponent the attenuationErponent to set
   */
  public void setAttenuationExponent(float attenuationExponent) {
    this.attenuationExponent = attenuationExponent;
  }

}
