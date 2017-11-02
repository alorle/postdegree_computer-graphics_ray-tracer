package view;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import tracer.RayGenerator;
import primitives.Point3D;
import primitives.Vector3D;

public class Camera {

  // ATRIBUTOS
  private final Point3D position;
  private final Vector3D up; // Vector Up
  private final Vector3D lookAt; // Vector LookAt
  private final Matrix4f camera2scene;
  private Projection optics;

  /**
   *
   * @param V Point3D de emplazamiento de la cámara
   * @param C Point3D hacia el que se orienta la cámara
   * @param up Vector3D vertical auxiliar
   */
  public Camera(final Point3D V, final Point3D C, final Vector3D up) {
    this.position = V;
    this.up = up;

    // El Vector lookAt es el definido por la posición (V) y el punto hacia ele
    // qu se orienta la cámara (C)
    this.lookAt = new Vector3D(V, C);

    // L amatriz de transformación de vista se puede construir como
    // concatenación de transformaciones elementales determinadas por
    // V, C y up:
    // - Traslación del sistema de modo que su origen ocupe el punto V en el
    //   que se ha emplazado la cámara
    // - Alineamiento del eje Z en la dirección de visión, usando la dirección
    //   del vector up como dirección vertical auxiliar
    this.camera2scene = new Matrix4f();

    final Vector3D w = this.lookAt.opposite();
    w.normalize();

    final Vector3D u = new Vector3D(up);

    final float s = u.dot(w);
    final float t = (float) (1 / Math.sqrt(1 - s * s));

    this.camera2scene.setColumn(0,
            t * (u.getY() * w.getZ() - u.getZ() * w.getY()),
            t * (u.getZ() * w.getX() - u.getX() * w.getZ()),
            t * (u.getX() * w.getY() - u.getY() * w.getX()),
            0);
    this.camera2scene.setColumn(1,
            t * (u.getX() - s * w.getX()),
            t * (u.getY() - s * w.getY()),
            t * (u.getZ() - s * w.getZ()),
            0);
    this.camera2scene.setColumn(2,
            w.getX(),
            w.getY(),
            w.getZ(),
            0);
    this.camera2scene.setColumn(3,
            V.getX(),
            V.getY(),
            V.getZ(),
            1);
  }

  public Camera(final Camera c) {
    this.position = new Point3D(c.position);
    this.up = new Vector3D(c.up);
    this.lookAt = new Vector3D(c.lookAt);
    this.camera2scene = new Matrix4f(c.camera2scene);
    this.optics = c.optics;
  }

  public final void toSceneCoord(final Vector3D v) {
    camera2scene.transform(v);
  }

  public final void toSceneCoord(final Point3D p) {
    camera2scene.transform(p);
  }

  public final Vector3D getLook() {
    return this.lookAt;
  }

  public final Point3D getPosition() {
    return this.position;
  }

  public final void setProjection(final Projection p) {
    this.optics = p;
  }

  public final Projection getProjection() {
    return this.optics;
  }

  public final RayGenerator getRayGenerator(final int W, final int H) {
    return this.optics.getRayGenerator(this, W, H);
  }

}
