package lights;
/**
 * Clase para agrupar las fuentes luminosas a emplear para sintetizar la imagen.
 * 
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

import java.util.ArrayList;
import java.util.List;

public class LightGroup {

  /**
   * Colección de luces.
   */
  protected final List<Light> collection;
  
  private Ambiental ambiental;

  /**
   * Constructor.
   */
  public LightGroup () {
    this.ambiental = null;
    this.collection = new ArrayList<>();
  }
  
  /**
   * Constructor.
   * @param spectrum
   * @param power
   */
  public LightGroup (final SpectrumRGB spectrum, final int power) {
    this.ambiental = new Ambiental(spectrum, power);
    this.collection = new ArrayList<>();
  }  

  /**
   * Añade una nueva fuente a la colección.
   *
   * @param light
   * @return
   */
  public final LightGroup addLight (final Light light) {
    if (light != null) {
      if (light instanceof Ambiental)
        this.ambiental = (Ambiental) light;
      else
        this.collection.add(light);
    }
    return this;
  }

  /**
   * Retorna la colección de luces.
   *
   * @return
   */
  public final List<Light> getCollection () {
    return this.collection;
  }

  @Override
  public String toString () {
    final StringBuffer stringBuf = new StringBuffer();
    stringBuf.append("LightsGroup ->\n");
    this.collection.forEach((light) -> {
      stringBuf.append("    ");
      stringBuf.append(light);
      stringBuf.append(";\n");
    });
    return stringBuf.toString();
  }

  // Método consultor a emplear sólo con brdf basadas en el modelo de Lafortune (tema07)
  public Ambiental getAmbiental () {
    return ambiental;
  }
  
  public int size () {
    return collection.size() + ((ambiental != null) ? 1 : 0);
  }
  
}