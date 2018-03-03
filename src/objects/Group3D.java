package objects;

import java.util.ArrayList;
import primitives.Vector3D;
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

  public boolean intersectsAnyCloser(final Ray ray, final Hit hit, final float squareDistance) {
    return objetos.stream().anyMatch((obj) -> {
      if (!hit.getObject().equals(obj)) {
        final Hit objHit = obj.intersects(ray);
        if (objHit.hits()) {
          final Vector3D v = new Vector3D(ray.getStartingPoint(), objHit.getPoint());
          return Math.signum(squareDistance - v.lengthSquared()) > 0;
        }
      }

      return false;
    });
  }
}
