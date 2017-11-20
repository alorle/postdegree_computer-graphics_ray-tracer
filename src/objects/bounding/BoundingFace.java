package objects.bounding;

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

  private final Plane plane;

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

    plane = new Plane(center, normal, null);

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
    final float angle = normal.angle(ray.getDirection());

    if (angle > Math.PI * 0.5f && angle < Math.PI) {
      // Rayo forma angulo obtuso con la cara, por lo que intersecta por su lado
      // visible
      final Hit hit = plane.intersects(ray);

      if (hit.hits()) {
        // Rayo intersecta con el plano que contiene la cara
        final Point3D P = hit.getPoint();

        // Debemos revisar si el punto de intersección está dentro de los
        // límites de la cara
        return (contains(P)) ? hit : Hit.NOHIT;
      }
    }

    return Hit.NOHIT;
  }

}