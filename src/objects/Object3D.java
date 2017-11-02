package objects;

import java.awt.Color;
import java.util.List;
import tracer.Hit;
import tracer.Ray;

public abstract class Object3D {

  protected Color color;
  //protected Shader material;
  protected boolean avoided = false;

  public Object3D() {
    color = Color.black;
  }

  /**
   * Este constructor permite definir el color del objeto a crear
   *
   * @param color
   */
  public Object3D(final Color color) {
    this.color = color;
  }

  /**
   * Devuelve el color del objeto
   *
   * @return this.color
   */
  public final Color getColor() {
    return color;
  }

  public final void avoid() {
    avoided = true;
  }

  public final boolean isAvoided() {
    return avoided;
  }

  /**
   * Determina si el rayo intersecta con el objeto
   *
   * @param ray Rayo para estudio
   * @return
   */
  public final Hit intersects(final Ray ray) {
    if (isAvoided()) {
      avoided = false;
      return Hit.NOHIT;
    } else {
      return _intersects(ray);
    }
  }

  protected abstract Hit _intersects(final Ray ray);

}
