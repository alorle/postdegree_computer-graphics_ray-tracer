package objects;

/**
 *
 * @author MAZ
 */
import javax.vecmath.Point2f;

import primitives.Vector3D;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;

public class MeshTriangle extends Triangle {

  private final Vector3D normalAtA;
  private final Vector3D normalAtB;
  private final Vector3D normalAtC;
  private final Point2f vtA;
  private final Point2f vtB;
  private final Point2f vtC;

  MeshTriangle(final Triangle base,
          final Vector3D normalAtA,
          final Vector3D normalAtB,
          final Vector3D normalAtC) {
    super(base);
    this.normalAtA = normalAtA;
    this.normalAtB = normalAtB;
    this.normalAtC = normalAtC;
    this.vtA = this.vtB = this.vtC = null;
  }

  MeshTriangle(final Triangle base,
          final Vector3D normalAtA,
          final Vector3D normalAtB,
          final Vector3D normalAtC,
          final Point2f vtA,
          final Point2f vtB,
          final Point2f vtC) {
    super(base);
    this.normalAtA = normalAtA;
    this.normalAtB = normalAtB;
    this.normalAtC = normalAtC;
    this.vtA = vtA;
    this.vtB = vtB;
    this.vtC = vtC;
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
            Vector3D n = normalAtA.multiplyByScalar(1 - bb - cc)
                    .add(normalAtB.multiplyByScalar(bb))
                    .add(normalAtC.multiplyByScalar(cc));
            n.normalize();
            return new Hit(a, ray.pointAtParameter(a), n, this);
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
}
