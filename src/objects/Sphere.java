package objects;

import java.awt.Color;

import primitives.Point3D;
import primitives.Vector3D;
import shaders.Material;
import tracer.Hit;
import tracer.Ray;

public class Sphere extends Object3D {

  private final Point3D centro;
  private final float radio;
  private final float inv_radio;

  private Sphere(final Point3D centro, final float radio) {
    this.centro = new Point3D(centro);
    this.radio = radio;
    this.inv_radio = (float) (1.0 / radio);
  }

  public Sphere(final Point3D centro, final float radio, final Color color) {
    this(centro, radio);
    this.color = color;
  }

  public Sphere(final Point3D centro, final float radio, final Material material) {
    this(centro, radio);
    this.material = material;
  }

  @Override
  protected Hit _intersects(final Ray ray) {
    final float r = this.radio;
    final Point3D C = this.centro;
    final Point3D R = ray.getStartingPoint();
    final Vector3D v = ray.getDirection();

    final Vector3D RC = new Vector3D(R, C);

    final float c = RC.dot(RC) - r * r;
    if (Math.signum(c) > 0) {
      // Punto origen del rayo, R, fuera de la esfera
      final float b = RC.dot(v);
      final float bSquare = b * b;

      if (Math.signum(b) > 0) {
        // Centro C de la esfera en semiespacio posterior, puede existir
        // intersección
        final float d = (float) Math.sqrt(bSquare - c);
        final float dSign = Math.signum(d);

        if (Math.signum(dSign) == 0) {
          // Rayo tangente a la esfera
          final float t = b;
          final Point3D P = ray.pointAtParameter(t);
          final Vector3D normal = new Vector3D(C, P);
          normal.normalize();

          return new Hit(t, P, normal, this);
        } else if (Math.signum(dSign) > 0) {
          // Rayo secante a la esfera
          final float tp = b + d;
          final float tm = c / tp;

          final Point3D P = ray.pointAtParameter(tm);
          final Vector3D normal = new Vector3D(C, P);
          normal.normalize();

          return new Hit(tm, P, normal, this);
        }

        // Rayo no corta a la esfera
        return Hit.NOHIT;
      }

      // Centro C de la esfera en semiespacio anterior
      return Hit.NOHIT;
    } else if (c < 0) {
      // Punto de origen dentro de la esfera
      final float b = RC.dot(v);
      final float t = b + (float) Math.sqrt(4 * b * b - 4 * c) * 0.5f;
      final Point3D P = ray.pointAtParameter(t);
      final Vector3D normal = new Vector3D(C, P);
      normal.normalize();

      return new Hit(t, P, normal, this);
    }

    // Punto de origen en la superficie de la esfera
    return new Hit(0, R, RC, this);
  }

}
