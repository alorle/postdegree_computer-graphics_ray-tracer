package tracer;

import primitives.Point3D;
import primitives.Vector3D;
import objects.Object3D;

public final class Hit {

  static public final Hit NOHIT = new Hit();

  // ATRIBUTOS
  private float t;
  private Object3D object;
  private Vector3D normal;
  private Point3D P;
  private boolean isInput = false;
  private boolean isOutput = false;

  // CONSTRUCTORES
  private Hit() {
    t = Float.POSITIVE_INFINITY;
  }

  public Hit(final float t,
          final Point3D P,
          final Vector3D n,
          final Object3D object) {
    this.t = t;
    this.P = P;
    this.normal = n;
    this.object = object;
  }

  public Hit(final Hit h) {
    this.t = h.getT();
    this.normal = h.getNormal();
    this.P = h.getPoint();
    this.object = h.getObject();
  }

  public Vector3D getNormal() {
    return normal;
  }

  public void setNormal(final Vector3D normal) {
    this.normal = normal;
  }

  public Point3D getPoint() {
    return P;
  }

  public void setPoint(final Point3D p) {
    this.P = p;
  }

  public float getT() {
    return t;
  }

  public void setT(final float t) {
    this.t = t;
  }

  public Object3D getObject() {
    return object;
  }

  public void setObject(final Object3D object) {
    this.object = object;
  }

  public boolean isCloser(final Hit h) {
    return getT() < h.getT();
  }

  public boolean hits() {
    return (this.t != Float.POSITIVE_INFINITY);
  }

  public boolean isInput() {
    return isInput;
  }

  public boolean isOutput() {
    return isOutput;
  }

  public void setAsInput() {
    isInput = true;
  }

  public void setAsOutput() {
    isOutput = true;
  }

}
