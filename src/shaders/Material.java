package shaders;
/**
 *
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import java.awt.Color;
import javax.vecmath.Matrix3f;

import lights.RadianceRGB;
import lights.LightGroup;
import objects.Group3D;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

public final class Material {

  // Valores de las funciones r(), g() y b() para igualar colores del espacio CIE-RGB
  // en las longitudes de onda en las que irradian las fuentes luminosas empleadas.
  // Para las frecuencias 546.1 y 435.8 se emplean los valores más cercanos que
  // se pueden encontrar en las tablas.
  private static final float[] CIE_RGB_MATCHING_VALUES = new float[] {  
  // Valores en:  700.0 nm (700.nm)   545.0 nm (546.1 nm)    435.0 nm (435.8 nm)
                      +0.0041f,           -0.0061f,              +0.0004f, // Funcion r()
                      +0.0000f,           +0.2149f,              -0.0002f, // Funcion g()
                      +0.0000f,           +0.0002f,              +0.2901f  // Funcion b()  
  };  
  
  
  // Elementos de la matriz de paso de CIE-RGB a CIE-XYZ
  private static final float[] CIE_RGB_TO_XYZ_ELEMENTS = new float[] {
    +0.4887180f, +0.3106803f, +0.2006017f, // X row
    +0.1762044f, +0.8129847f, +0.0108109f, // Y row
    +0.0000000f, +0.0102048f, +0.9897952f  // Z row
  };  
  
  // Elementos de la matriz de conversión de CIE-XYZ a sRGB D65
  private static final float[] XYZ_TO_sRGB_ELEMENTS = new float[] {
    +3.240479f, -1.537150f, -0.498535f, // R row
    -0.969256f, +1.875991f, +0.041556f, // G row
    +0.055648f, -0.204043f, +1.057311f  // B row
  };  
  
// Elementos de la matrzi de conversión de CIE-XYZ a sRGB D65  
//  private static final float[] XYZ_TO_sRGB_ELEMENTS = new float[] {
//    +3.2404542f, -1.5371385f, -0.4985314f,
//    -0.9692660f, +1.8760108f, +0.0415560f,
//    +0.0556434f, -0.2040259f, +1.0572252f
//  };
  
  private static final Matrix3f TO_LUMINANCE = new Matrix3f(CIE_RGB_MATCHING_VALUES);
  private static final Matrix3f RGB_TO_XYZ   = new Matrix3f(CIE_RGB_TO_XYZ_ELEMENTS);
  private static final Matrix3f XYZ_TO_sRGB  = new Matrix3f(XYZ_TO_sRGB_ELEMENTS);
  
  private static final float LIGHT_EFFICIENCY = 683.0f; // lm por cada vatio en 555 nm
  private static final int DEPTH = 10; // Establece el número máximo de reflejos objeto a objeto
  
  private final BRDF brdf;
  private final boolean specularRecursion;
   
  public Material (final BRDF brdf, final boolean specularRecursion) {
    this.brdf = brdf;
    this.specularRecursion = specularRecursion;
  }

  /**
   * Calculo en tres etapas del valor de color que corresponde a un pixel:
   * - Obtención de radiancia incidente sobre el punto V (proceso recursivo)
   * - Conversión de radiancia incidente en luminancia
   * - Transformación de luminancia a valor de color en espacio sRGB D65
   *
   * @param scene
   * @param lights
   * @param hit informacion del punto de interseccion
   * @param V punto hacia el que se dirige la radiancia saliente 
  * @return 
   */
  public Color getColor (final Group3D scene,
                         final LightGroup lights,
                         final Hit hit,
                         final Point3D V) {  
    
    final RadianceRGB radianceRGB = this.getRadiance(scene, lights, hit, V, 0);
    
    final float luminanceR = LIGHT_EFFICIENCY * (
            TO_LUMINANCE.m00 * radianceRGB.r +
            TO_LUMINANCE.m01 * radianceRGB.g +
            TO_LUMINANCE.m02 * radianceRGB.b);
    final float luminanceG = LIGHT_EFFICIENCY * (
            TO_LUMINANCE.m10 * radianceRGB.r +
            TO_LUMINANCE.m11 * radianceRGB.g +
            TO_LUMINANCE.m12 * radianceRGB.b);
    final float luminanceB = LIGHT_EFFICIENCY * (
            TO_LUMINANCE.m20 * radianceRGB.r +
            TO_LUMINANCE.m21 * radianceRGB.g +
            TO_LUMINANCE.m22 * radianceRGB.b);

    final float X =
            RGB_TO_XYZ.m00 * luminanceR +
            RGB_TO_XYZ.m01 * luminanceG +
            RGB_TO_XYZ.m02 * luminanceB;
    final float Y =
            RGB_TO_XYZ.m10 * luminanceR +
            RGB_TO_XYZ.m11 * luminanceG +
            RGB_TO_XYZ.m12 * luminanceB;
    final float Z =
            RGB_TO_XYZ.m20 * luminanceR +
            RGB_TO_XYZ.m21 * luminanceG +
            RGB_TO_XYZ.m22 * luminanceB;

    final float R = (float) Math.max(0.0, Math.min(1.0f,
            XYZ_TO_sRGB.m00 * X + XYZ_TO_sRGB.m01 * Y + XYZ_TO_sRGB.m02 * Z));            
    final float G = (float) Math.max(0.0, Math.min(1.0f,
            XYZ_TO_sRGB.m10 * X + XYZ_TO_sRGB.m11 * Y + XYZ_TO_sRGB.m12 * Z));            
    final float B = (float) Math.max(0.0, Math.min(1.0f,
            XYZ_TO_sRGB.m20 * X + XYZ_TO_sRGB.m21 * Y + XYZ_TO_sRGB.m22 * Z));
    
    return new Color(R, G, B);  

  }
  
  /**
   * Obtiene el valor de radiancia saliente hacia el punto V
   * desde la intersección proporcionada. Calcula la radiancia
   * directa desde las fuentes de luz proporcionadas y la radiancia
   * indirecta reflejada desde las superficies de los objetos proporcionados.
   * La radiacia saliente se obtiene aplicando la función de reflectancia
   * (shader) proporcionada en construcción al material del puto de intersección.
   * 
   * @param scene
   * @param lights
   * @param hit informacion del punto de interseccion
   * @param V punto hacia el que se dirige la radiancia saliente 
   * @param depth indica el nivel de recursion
  * @return 
   */    
  protected RadianceRGB getRadiance (final Group3D scene,
                                     final LightGroup lights,
                                     final Hit hit,
                                     final Point3D V,
                                     final int depth) {
    
    if (depth <= Material.DEPTH) {
      
      final RadianceRGB outgoingRadiance = brdf.getDirectRadiance(lights, scene, hit, V);
      
      if (specularRecursion) {
        
        // Cálculo de dirección de incidencia en P
        // para que el reflejo se dirija hacia V.
        final Point3D P = hit.getPoint();
        final Vector3D n = hit.getNormal();
        final Vector3D v = P.sub(V);
        v.normalize();
        final Vector3D newI = v.add(-2 * (n.dot(v)), n);
        newI.normalize();
        
        // Rayo secundario para encontrar objeto que refleje hacia P.
        final Ray ray = new Ray(P, newI);
        
        // Se evita la autointersección
        hit.getObject().avoid();
        // Se estudia la intersección con el rayo secundario
        final Hit hitS = scene.intersects(ray);
        if (hitS.hits()) {
          final Material material = hitS.getMaterial();
          // Total de radiancia incidente sobre P reflejada desde la intersección obtenida.
          final RadianceRGB recTerm = material.getRadiance(scene, lights, hitS, P, depth + 1);
          // Fracción de esa radiancia que se refleja de modo especular hacia V
          outgoingRadiance.add(brdf.specularFilter(recTerm));
        }
      }  
    
      return outgoingRadiance;
    
    }
    else
      return RadianceRGB.NORADIANCE;    
    
  }  
  
}