package objects;

/**
 *
 * @author MAZ
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import primitives.Vector3D;
import primitives.Point3D;
import shaders.Material;
import tracer.Hit;
import tracer.Ray;

public class Triangle extends Object3D {

  protected final Point3D A;
  protected final Point3D B;
  protected final Point3D C;
  protected final Vector3D normal;
  protected final Vector3D AB;
  protected final Vector3D AC;
  protected final double factorM;

  protected Triangle(final Triangle x) {
    A = x.A;
    B = x.B;
    C = x.C;
    AB = x.AB;
    AC = x.AC;
    normal = x.normal;
    factorM = x.factorM;
    color = x.color;
  }

  private Triangle(final Point3D a, final Point3D b, final Point3D c) {
    A = new Point3D(a);
    B = new Point3D(b);
    C = new Point3D(c);
    AB = new Vector3D(A, B);
    AC = new Vector3D(A, C);
    normal = (AB.cross(AC));
    factorM = 1.0 / normal.length();
    normal.normalize();
  }

  public Triangle(final Point3D a, final Point3D b, final Point3D c, final Color color) {
    this(a, b, c);
    this.color = color;
  }

  public Triangle(final Point3D a, final Point3D b, final Point3D c, final Material material) {
    this(a, b, c);
    this.material = material;
  }

  @Override
  protected Hit _intersects(final Ray ray) {
    final Point3D R = ray.getStartingPoint();
    final Vector3D v = ray.getDirection();

    final float c = v.dot(normal);
    if (Math.signum(c) < 0) {
      // Rayo entra por la cara exterior
      final Vector3D AR = new Vector3D(A, R);
      final float b = AR.dot(normal);

      if (Math.signum(b) >= 0) {
        // Intersección en semiespacio posterior (visible)
        final float dd = (float) (factorM / c);
        final float bb = AC.dot(v.cross(AR)) * dd;

        if (Math.signum(bb) >= 0 && bb <= 1) {
          final float cc = -(AB.dot(v.cross(AR)) * dd);

          if (Math.signum(cc) >= 0 && cc <= 1 && (bb + cc) <= 1) {
            final float a = -b / c;
            return new Hit(a, ray.pointAtParameter(a), normal, this);
          }
        }
      }
    }

    // Llegados aquí:
    // - Rayo entra por la cara posterior del triángulo
    // - Rayo discurre paralelo al plano definido por el triángulo
    // - El cruce se produce en el semiespacio anterior (oculto)
    // - Rayo no intersecta por los valores de alpha, beta y gamma
    return Hit.NOHIT;
  }

  public final Vector3D getNormal() {
    return this.normal;
  }

  protected final Point3D getA() {
    return this.A;
  }

  protected final Point3D getB() {
    return this.B;
  }

  protected final Point3D getC() {
    return this.C;
  }

}
