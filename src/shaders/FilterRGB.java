package shaders;

import lights.RadianceRGB;

/**
 * Clase para representar coeficientes de reflectancia
 * a aplicar en cada canal de radiacion.
 * 
 * Sirve para los tres tipos de coeficiente de reflectancia que han aparecido
 * (ambiental en el modelo de Phong/Lafortune, difusa y especular)
 * 
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

public class FilterRGB {
  
  public final float xr;
  public final float xg;
  public final float xb;
  
  public FilterRGB () {
    this(0.0f, 0.0f, 0.0f);
  }
  
  public FilterRGB (final float xr, final float xg, final float xb) {
    if (!(((0.0f <= xr) && (xr <= 1.0f)) &&
          ((0.0f <= xg) && (xg <= 1.0f)) &&
          ((0.0f <= xb) && (xb <= 1.0f))))
      throw new IllegalArgumentException("Bad filter");
    this.xr = xr;
    this.xg = xg;
    this.xb = xb;
  }
  
  /**
   * Para el valor de radiancia que recibe como argumento, devuel la fracción
   * que no es absorbida (en consecuencia, es reflejada).
   *
   * @param inputRadiance: radiancia de entrada
   * @return radiancia no absorbida
   */  
  public RadianceRGB filter (final RadianceRGB inputRadiance) {
    return new RadianceRGB(xr * inputRadiance.r, xg * inputRadiance.g, xb * inputRadiance.b);
  }
  
  // Método a emplear en la BRDF de Ashikmin-Shirley
  public FilterRGB complement () {
    return new FilterRGB(1.0f - xr, 1.0f - xg, 1.0f - xg);
  }
  
}