package objects.boundingboxes;

import objects.Object3D;
import objects.Plane;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

/**
 * @author Álvaro Orduna León
 */
public class BoundingFace extends Object3D {

  protected static enum EclideanPlane {
    XY,
    YZ,
    ZX
  }

  private final EclideanPlane parallelTo;
  private final Point3D center;
  private final Vector3D normal;

  private final float width;
  private final float heigth;
  private final float widthHalf;
  private final float heigthHalf;

  BoundingFace(final OrthoedricBoundingBox box, final EclideanPlane parallelTo,
          final float offset, final boolean isNormalPositive) {
    this.parallelTo = parallelTo;

    final Point3D boxCenter = box.getCenter();
    switch (parallelTo) {
      case XY:
        center = new Point3D(boxCenter.getX(), boxCenter.getY(), offset);
        normal = new Vector3D(0, 0, (isNormalPositive) ? 1 : -1);
        width = box.getXlen();
        heigth = box.getYlen();
        break;
      case YZ:
        center = new Point3D(offset, boxCenter.getY(), boxCenter.getZ());
        normal = new Vector3D((isNormalPositive) ? 1 : -1, 0, 0);
        width = box.getYlen();
        heigth = box.getZlen();
        break;
      case ZX:
        center = new Point3D(boxCenter.getX(), offset, boxCenter.getZ());
        normal = new Vector3D(0, (isNormalPositive) ? 1 : -1, 0);
        width = box.getZlen();
        heigth = box.getXlen();
        break;
      default:
        throw new RuntimeException("BoundingFace: unkown EclideanPlane");
    }

    widthHalf = width * 0.5f;
    heigthHalf = heigth * 0.5f;
  }

  private boolean contains(final Point3D p) {
    final Vector3D distanceVector = new Vector3D(center, p);
    switch (parallelTo) {
      case XY:
        return Math.abs(distanceVector.getX()) <= widthHalf
                && Math.abs(distanceVector.getY()) <= heigthHalf;
      case YZ:
        return Math.abs(distanceVector.getY()) <= widthHalf
                && Math.abs(distanceVector.getZ()) <= heigthHalf;
      case ZX:
        return Math.abs(distanceVector.getZ()) <= widthHalf
                && Math.abs(distanceVector.getX()) <= heigthHalf;
      default:
        throw new RuntimeException("BoundingFace: unkown EclideanPlane");
    }
  }

  @Override
  protected Hit _intersects(Ray ray) {
    final float a = ray.getDirection().dot(normal);

    if (a < 0) {
      // Rayo forma angulo obtuso con la cara, por lo que intersecta por su lado
      // visible
      final float t = center.sub(ray.getStartingPoint()).dot(normal) / a;

      if (t >= 0) {
        // Intersección en semiespacio posterior
        final Point3D P = ray.pointAtParameter(t);

        // Debemos revisar si el punto de intersección está dentro de los
        // límites de la cara
        return (contains(P))
                ? new Hit(t, P, normal, this)
                : Hit.NOHIT;
      }
    }

    return Hit.NOHIT;
  }

}
