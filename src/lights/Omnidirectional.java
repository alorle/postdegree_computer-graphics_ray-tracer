package lights;

/**
 * Representa una fuente de luz omnidireccional
 */
import primitives.Vector3D;
import primitives.Point3D;
import objects.Group3D;
import tracer.Hit;
import tracer.Ray;

public class Omnidirectional extends Light {

  /**
   * Intensidad radiante en cada canal de irradiación
   */
  private final RadianceRGB radiantIntensity;

  /**
   * Crea una nueva luz puntual.
   *
   * @param position Ubicación de la fuente de luz
   * @param spectrum Espectro de radiación en canales RGB
   * @param power Potencia (intensidad) de emisión
   */
  public Omnidirectional(
          final Point3D position,
          final SpectrumRGB spectrum,
          final float power) {
    super(position, spectrum, power);
    radiantIntensity = null;
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {

    return RadianceRGB.NORADIANCE;
  }

  @Override
  public String toString() {
    return "OmnidirectionalLight -> position = " + this.S;
  }

}
