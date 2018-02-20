package shaders;

import shaders.densityfunctions.MicrofacetDensityFunction;
import lights.RadianceRGB;
import lights.LightGroup;
import objects.Group3D;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

public final class PhongLafortune extends BRDF {

  private final MicrofacetDensityFunction densityFunction;
  private final FilterRGB ambient;

  public PhongLafortune(final FilterRGB ambient,
          final FilterRGB diffuse,
          final FilterRGB specular,
          final MicrofacetDensityFunction densityFunction) {
    super(diffuse, specular);
    this.ambient = ambient;
    this.densityFunction = densityFunction;
  }

  @Override
  protected RadianceRGB getDirectRadiance(final LightGroup lights,
          final Group3D scene,
          final Hit hit,
          final Point3D V) {
    // Direccion de salida
    final Point3D P = hit.getPoint();
    final Vector3D n = hit.getNormal();
    final Vector3D v = V.sub(P);
    v.normalize();

    // Radiancia debida a iluminacion ambiental
    // Esta ilimunación ambiental solo se emplea
    // en el modelo de reflectancia de Phong-Lafortune.
    // En otrs modelos (Cook-Torrance-Sparrow, Ashikmin-Shirley, etc)
    // no se incluye (en esos modelos tampoco hay coeficientes asociados
    // a iluminación ambiental).
    final RadianceRGB ambientalIrradiance
            = lights.getAmbiental().irradianceAt(hit, scene);
    final RadianceRGB outgoingRadiance
            = ambient.filter(ambientalIrradiance);

    // En general, la radiancia de salida es suma de:
    //  * la radiancia saliente debida a la reflexión difusa
    //  * la radiancia saliente debida a la reflexión especular (glossy)
    final RadianceRGB outgoingDiffuseRadiance = new RadianceRGB();
    final RadianceRGB outgoingSpecularRadiance = new RadianceRGB();
    lights.getCollection().forEach((light) -> {

      // Consulta de radiancia incidente desde la fuente luminosa
      // sobre el punto contenido en el objeto Hit
      final RadianceRGB lightIrradiance = light.irradianceAt(hit, scene);

      // Dirección de incidencia
      final Point3D S = light.getPosition();
      final Vector3D I = S.sub(P);
      I.normalize();

      // Vector mediocamino (v es la direccion de salida)
      final Vector3D h = I.add(v);
      h.normalize();

      // Porporcion de microfacetas con normal orientada hacia el vector mediocamino
      final float x = densityFunction.getProbability(n, h);

      // Acumulación de radiancia directa (despues se filtras con el coeficiente de reflectancia difusa)
      outgoingDiffuseRadiance.add(lightIrradiance);
      // Acumulación de radiancia directa que es reflejada especularmente hacia punto V
      outgoingSpecularRadiance.add(x, lightIrradiance);

    });

    final float LWfactor = densityFunction.getNormalizationFactor();

    // Aplicación del filtro debido a los coeficientes de reflexión difusa y caumulación
    outgoingRadiance.add(INV_PI, diffuse.filter(outgoingDiffuseRadiance));
    // Aplicación del filtro debido a los coeficientes de reflexión especular y acumulación
    outgoingRadiance.add(LWfactor, specular.filter(outgoingSpecularRadiance));

    // Radiancia saliente hacia el punto V
    return outgoingRadiance;
  }

}
