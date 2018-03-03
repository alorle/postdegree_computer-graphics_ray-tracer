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
    direction = lookAt.sub(S);
    direction.normalize();
    apertureIndex = (float) Math.cos(Math.toRadians(aperture));
    final float w = (float) (power / (2 * Math.PI * (1 - apertureIndex)));
    radiantIntensity = emissionSpectrum.distribute(w);
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {
    final Point3D P = hit.getPoint();
    final Vector3D PS = getPosition().sub(P);
    final Vector3D I = new Vector3D(PS);
    I.normalize();

    // Comprobar que el punto de incidencia no queda
    // fuera del cono de iluminación
    if (Math.signum(I.dot(direction) + apertureIndex) > 0) {
      return RadianceRGB.NORADIANCE;
    }

    // Comprobar que el punto de incidencia no queda
    // oculto por alguno de los elementos de la escena
    final Ray ray = new Ray(getPosition(), PS);
    if (scene.intersectsAnyCloser(ray, hit, PS.lengthSquared())) {
      return RadianceRGB.NORADIANCE;
    }

    final float dSquare = PS.dot(PS);
    final float nDotI = hit.getNormal().dot(I);
    return radiantIntensity.scale(nDotI / dSquare);
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
