package lights;
/**
 * Clase para manejar y operar con valores de radiancia/irradiancia
 * en los tres canales en los que irradian nuestras fuentes luminosas.
 * 
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

public final class RadianceRGB {
  
  // Objeto estatico -introducido por razones de eficiencia-
  // a devolver cuando la radiancia es cero.
  static public final RadianceRGB NORADIANCE = new RadianceRGB(0.0f, 0.0f, 0.0f); 

  /**
   * Valor de radiancia/irradiancia en canal rojo (700.0 nm)
   */
  public float r;

  /**
   * Valor de radiancia/irradiancia en canal verde (546.1 nm)
   */
  public float g;

  /**
   * Valor de radiancia/irradiancia en canal azul (435.8 nm)
   */
  public float b;

  /**
   * Constructor por defecto.
   */
  public RadianceRGB () {
    this(0.0f, 0.0f, 0.0f);
  }

  /**
   * Copia un valor de radiancia/irradiancia.
   *
   * @param rgb: valores de radiancia a duplicar
   */
  public RadianceRGB (final RadianceRGB rgb) {
    this(rgb.r, rgb.g, rgb.b);
  }

  /**
   * Constructor explícito.
   *
   * @param r
   * @param g
   * @param b
   */
  public RadianceRGB (final float r, final float g, final float b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }

  /**
   * Copia los valores de inRGB a este objeto.
   *
   * @param inRGB
   * @return this
   */
  public RadianceRGB set (final RadianceRGB inRGB) {
    this.r = inRGB.r;
    this.g = inRGB.g;
    this.b = inRGB.b;
    return this;
  }

  /**
   * Establece los valores de este objeto a los proporcionados (r, g, b).
   *
   * @param r
   * @param g
   * @param b
   * @return this
   */
  public RadianceRGB set (final float r, final float g, final float b) {
    this.r = r;
    this.g = g;
    this.b = b;
    return this;
  }

  /**
   * Escala cada componente de este objeto por el factor de escala proporcionado.
   *
   * @param rhs The scale value to use.
   * @return this
   */
  public RadianceRGB scale (final float rhs) {
    this.r *= rhs;
    this.g *= rhs;
    this.b *= rhs;
    return this;
  }

  /**
   * Acumula sobre este objeto valores de radiancia/irradiancia.
   *
   * @param rhs
   * @return this
   */
  public RadianceRGB add (final RadianceRGB rhs) {
    this.r += rhs.r;
    this.g += rhs.g;
    this.b += rhs.b;
    return this;
  }
  
  /**
   * Multiplica los valores de radiancia por el escalar y acumula sobre este objeto. 
   *
   * @param x
   * @param rhs
   * @return this
   */
  public RadianceRGB add (final float x, final RadianceRGB rhs) {
    this.r += x * rhs.r;
    this.g += x * rhs.g;
    this.b += x * rhs.b;
    return this;
  }
  

  /**
   * @return @see Object#toString()
   */
  @Override
  public String toString() {
    return "radiancia: {" + this.r + ", " + this.g + ", " + this.b + "}";
  }
  
}