package objects;

import java.util.ArrayList;
import tracer.Hit;
import tracer.Ray;

public final class Group3D extends Object3D {

  private final ArrayList<Object3D> objetos;

  public Group3D() {
    this.objetos = new ArrayList<>();
  }

  public void addObject(final Object3D objeto) {
    objetos.add(objeto);
  }

  public Object3D getObject(final int j) {

    assert ((j >= 0) && (j < objetos.size()));

    return objetos.get(j);
  }

  @Override
  protected Hit _intersects(final Ray ray) {

    Hit closestHit = Hit.NOHIT;

    for (final Object3D objeto : objetos) {

      final Hit lastHit = objeto.intersects(ray);

      if (lastHit.isCloser(closestHit)) {
        closestHit = lastHit;
      }

    }

    return closestHit;

  }

}
