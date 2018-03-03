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
    final float w = (float) (power / ((2 * Math.PI) * (2 * Math.PI)));
    radiantIntensity = emissionSpectrum.distribute(w);
  }

  @Override
  public RadianceRGB irradianceAt(final Hit hit, final Group3D scene) {
    final Point3D P = hit.getPoint();
    final Vector3D PS = getPosition().sub(P);
    final Vector3D I = new Vector3D(PS);
    I.normalize();

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

  @Override
  public String toString() {
    return "OmnidirectionalLight -> position = " + this.S;
  }

}
