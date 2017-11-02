package primitives;

import javax.vecmath.Vector3f;

public final class Vector3D extends Vector3f {

  public Vector3D(final float x, final float y, final float z) {
    super(x, y, z);
  }

  public Vector3D(final Vector3D v) {
    this(v.getX(), v.getY(), v.getZ());
  }

  public Vector3D(final Point3D P, final Point3D Q) {
    this(Q.getX() - P.getX(), Q.getY() - P.getY(), Q.getZ() - P.getZ());
  }

  public Vector3D() {
    this(0.0f, 0.0f, 0.0f);
  }

  public Vector3D multiplyByScalar(final float f) {
    return new Vector3D(f * getX(), f * getY(), f * getZ());
  }

  public Vector3D opposite() {
    return new Vector3D(-getX(), -getY(), -getZ());
  }

  public Vector3D add(final Vector3D v) {
    return new Vector3D(getX() + v.getX(), getY() + v.getY(), getZ() + v.getZ());
  }

  public Vector3D add(final float alpha, final Vector3D v) {
    final float x_ = getX() + alpha * v.getX();
    final float y_ = getY() + alpha * v.getY();
    final float z_ = getZ() + alpha * v.getZ();
    return new Vector3D(x_, y_, z_);
  }

  public Vector3D sub(final Vector3D v) {
    return new Vector3D(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
  }

  public float dot(final Vector3D v) {
    final double xa = getX();
    final double ya = getY();
    final double za = getZ();
    final double xb = v.getX();
    final double yb = v.getY();
    final double zb = v.getZ();
    return (float) (xa * xb + ya * yb + za * zb);
  }

  public Vector3D cross(final Vector3D v) {
    final double xa = getX();
    final double ya = getY();
    final double za = getZ();
    final double xb = v.getX();
    final double yb = v.getY();
    final double zb = v.getZ();
    final float x_ = (float) (ya * zb - za * yb); //getY() * v.getZ() - getZ() * v.getY();
    final float y_ = (float) (za * xb - xa * zb); //getZ() * v.getX() - getX() * v.getZ();
    final float z_ = (float) (xa * yb - ya * xb); //getX() * v.getY() - getY() * v.getX();
    return new Vector3D(x_, y_, z_);
  }

  public float triple(final Vector3D v, final Vector3D w) {
    final double xa = v.getX();
    final double ya = v.getY();
    final double za = v.getZ();
    final double xb = w.getX();
    final double yb = w.getY();
    final double zb = w.getZ();
    final double x_ = ya * zb - za * yb; // v.getY() * w.getZ() - v.getZ() * w.getY();
    final double y_ = za * xb - xa * zb; // v.getZ() * w.getX() - v.getX() * w.getZ();
    final double z_ = xa * yb - ya * xb; // v.getX() * w.getY() - v.getY() * w.getX();
    return (float) (getX() * x_ + getY() * y_ + getZ() * z_);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public float getZ() {
    return z;
  }

  @Override
  public String toString() {
    return "(" + getX() + "," + getX() + "," + getZ() + ")";
  }

}
