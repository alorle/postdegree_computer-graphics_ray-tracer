package objects;

/**
 *
 * @author MAZ
 */
import java.awt.Color;
import primitives.Vector3D;
import primitives.Point3D;
import shaders.Material;
import tracer.Hit;
import tracer.Ray;

public final class Plane extends Object3D {

  private final Point3D P;
  private final Vector3D normal;
  private final Vector3D normalOpposite;

  public Plane(final Point3D p, final Vector3D n, final Color color) {
    super(color);
    this.P = new Point3D(p);
    this.normal = n;
    this.normal.normalize();
    this.normalOpposite = normal.opposite();
  }

  public Plane(Point3D p, Vector3D n, Material material) {
    super(material);
    this.P = new Point3D(p);
    this.normal = n;
    this.normal.normalize();
    this.normalOpposite = normal.opposite();
  }

  public Plane(final Point3D a, final Point3D b, final Point3D c, final Color color) {
    super(color);
    this.P = new Point3D(a);
    final Vector3D BA = new Vector3D(a, b);
    final Vector3D CA = new Vector3D(a, c);
    this.normal = (BA.cross(CA));
    this.normal.normalize();
    this.normalOpposite = normal.opposite();
  }

  public Plane(final Point3D a, final Point3D b, final Point3D c, final Material material) {
    super(material);
    this.P = new Point3D(a);
    final Vector3D BA = new Vector3D(a, b);
    final Vector3D CA = new Vector3D(a, c);
    this.normal = (BA.cross(CA));
    this.normal.normalize();
    this.normalOpposite = normal.opposite();
  }

  @Override
  protected Hit _intersects(final Ray ray) {
    final float a = ray.getDirection().dot(normal);

    if (Math.signum(a) != 0) {
      // Rayo no paralelo al plano
      final Vector3D QP = new Vector3D(ray.getStartingPoint(), P);

      // Intersección por un lado del plano
      final float t = QP.dot(normal) / a;
      if (Math.signum(t) >= 0) {
        // Intersección en semiespacio posterior
        return new Hit(t, ray.pointAtParameter(t),
                (a > 0) ? normalOpposite : normal,
                this);
      }
    }

    return Hit.NOHIT;

  }

}
