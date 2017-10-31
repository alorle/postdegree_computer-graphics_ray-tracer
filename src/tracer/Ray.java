package tracer;

import primitives.Point3D;
import primitives.Vector3D;

public final class Ray {

  private final Vector3D v;
  private final Point3D R;
  private final float evil;

  public Ray (final Point3D R, final Vector3D v) {
    this.v = new Vector3D(v);
    this.v.normalize();
    this.R = new Point3D(R);
    this.evil = v.dot(v);
  }

  public Ray (final Point3D R, final Point3D Q) {
    this.v = new Vector3D(R, Q);
    this.v.normalize();
    this.R = new Point3D(R);
    this.evil = v.dot(v);
  }

  /**
   * Constructor copia
   *
   * @param r
   */
  public Ray (final Ray r) {
    this.v = r.getDirection();
    this.R = r.getStartingPoint();
    this.evil = r.getEvil();
  }

  public Vector3D getDirection () {
    return this.v;
  }

  public Point3D getStartingPoint () {
    return this.R;
  }

  public Point3D pointAtParameter (final float t) {
    return R.add(t, v);
  }
  
  public float getEvil () {
    return evil;
  }
  
  public boolean isOperative () {
    return (this.getEvil() > 0.5e-6f);
  }    

  @Override
  public String toString () {
    return "Rayo: Origen " + R.toString() + " Direccion " + v.toString() + "\n";
  }

}