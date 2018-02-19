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
  protected Hit _intersects(final Ray r) {
    Hit superHit = super._intersects(r);
    if (!superHit.hits()) {
      return superHit;
    }

    Point3D P = superHit.getPoint();
    float beta = P.sub(A).dot(AB) / AB.length();
    float gamma = P.sub(A).dot(AC) / AC.length();

    Vector3D hitNormal = normalAtA.multiplyByScalar(1 - beta - gamma)
            .add(normalAtB.multiplyByScalar(beta))
            .add(normalAtC.multiplyByScalar(gamma));
    hitNormal.normalize();

    return new Hit(superHit.getT(), P, hitNormal, this);
  }
}
