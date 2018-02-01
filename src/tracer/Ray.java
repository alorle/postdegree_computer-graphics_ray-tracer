package tracer;

import primitives.Point3D;
import primitives.Vector3D;

public class Ray {

  private final Vector3D v;
  private final Point3D R;
  private final float evilSeed;
  private final float invEvilSeed;  

  public Ray (final Point3D R, final Vector3D v) {
    this.v = new Vector3D(v);
    this.v.normalize();
    this.evilSeed = v.dot(v);
    this.invEvilSeed = (float) (1.0 / Math.sqrt(this.evilSeed));
    this.R = new Point3D(R);
  }

  public Ray (final Point3D R, final Point3D Q) {
    this(R, Q.sub(R));
//    this.v = new Vector3D(R, Q);
//    this.v.normalize();
//    this.evilSeed = v.dot(v);
//    this.invEvilSeed = (float) (1.0 / this.evilSeed);    
//    this.R = new Point3D(R);
  }

  /**
   * Constructor copia
   *
   * @param r
   */
  public Ray (final Ray r) {
    this.v = r.getDirection();
    this.evilSeed = r.getEvilSeed();
    this.invEvilSeed = (float) (1.0 / this.evilSeed);
    this.R = r.getStartingPoint();
  }

  public Vector3D getDirection () {
    return this.v;
  }

//  public void setDirection (final Vector3D dir) {
//    this.v = dir;
//  }

  public Point3D getStartingPoint () {
    return this.R;
  }

//  public void setStartingPoint (final Point3D R) {
//    this.R = R;
//  }

  public Point3D pointAtParameter (final float t) {
    return R.add(t, v);
  }
  
  public float getEvilSeed () {
    return evilSeed;
  }

  @Override
  public String toString () {
    return "Rayo: Origen " + R.toString() + " Direccion " + v.toString() + "\n";
  }
  
  public boolean isOperative () {
    return (this.getEvilSeed() > 0.5e-7f);
  }  
  
}