package primitives;

import javax.vecmath.Point3f;

public class Point3D extends Point3f {

  public Point3D(final float x, final float y, final float z) {
    super(x, y, z);
  }

  public Point3D(final Point3D p) {
    this(p.getX(), p.getY(), p.getZ());
  }

  public Point3D() {
    this(0.0f, 0.0f, 0.0f);
  }

  public Point3D add(final Vector3D v) {
    final float x_ = v.getX() + getX();
    final float y_ = v.getY() + getY();
    final float z_ = v.getZ() + getZ();
    return new Point3D(x_, y_, z_);
  }

  public Point3D add(final float alpha, final Vector3D v) {
    final float x_ = getX() + alpha * v.getX();
    final float y_ = getY() + alpha * v.getY();
    final float z_ = getZ() + alpha * v.getZ();
    return new Point3D(x_, y_, z_);
  }

  public Vector3D sub(final Point3D p) {
    final float x_ = getX() - p.getX();
    final float y_ = getY() - p.getY();
    final float z_ = getZ() - p.getZ();
    return new Vector3D(x_, y_, z_);
  }

  @Override
  public String toString() {
    return "<" + getX() + "," + getY() + "," + getZ() + ">";
  }

}
