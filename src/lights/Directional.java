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
    direction = lookAt.sub(S);
    direction.normalize();
    squareRadius = radius * radius;
    final float s = (float) (squareRadius * Math.PI);
    puntualIrradiance = emissionSpectrum.distribute(power / s);
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {
    final Point3D P = hit.getPoint();
    final Vector3D PS = getPosition().sub(P);
    final Vector3D I = new Vector3D(PS);
    I.normalize();

    // Comprobar que el punto de incidencia no queda
    // fuera del cilindro infinito definido por la
    // la fuente luminosa direccional
    final float PSdotD = PS.dot(direction);
    final float d2 = PS.dot(PS) - (PSdotD * PSdotD);
    if (Math.signum(d2 - squareRadius) > 0) {
      return RadianceRGB.NORADIANCE;
    }

    // Comprobar que el punto de incidencia no queda
    // oculto por alguno de los elementos de la escena
    final Ray ray = new Ray(getPosition(), PS);
    if (scene.intersectsAnyCloser(ray, hit, PS.lengthSquared())) {
      return RadianceRGB.NORADIANCE;
    }

    final float nDotI = hit.getNormal().dot(I);
    return puntualIrradiance.scale(nDotI);
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
