package objects.transformations;

import java.awt.Color;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import objects.Object3D;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

/**
 * @author Álvaro Orduna León
 */
public class AffineTransformation extends Object3D {

  static final Vector3f S_IDENTITY = new Vector3f(1, 1, 1);
  static final float R_IDENTITY = 0.0f;
  static final Vector3f D_IDENTITY = new Vector3f(0, 0, 0);

  private final Matrix4f S;
  private final Matrix4f R;
  private final Matrix4f T;

  private final Matrix4f M;
  private final Matrix4f Minv;
  private final Matrix4f N;

  private final Object3D model;

  public AffineTransformation(
          final Vector3f s, // Factores de escala
          final Vector3f axis, // Dirección de eje de rotación
          final float theta, // Ángulo de rotación
          final Vector3f d, // Vector de traslación
          final Object3D object // Objeto sobre el que actuar
  ) {
    S = scaleMatrix(s);
    R = rotationMatrix(axis, theta);
    T = traslationMatrix(d);

    M = new Matrix4f();
    M.setIdentity();
    M.mul(T);
    if (R != null) {
      M.mul(R);
    }
    M.mul(S);

    Minv = new Matrix4f(M);
    Minv.invert();

    N = new Matrix4f();
    N.setIdentity();
    if (R != null) {
      N.mul(R);
    }
    N.mul(S);

    model = object;
  }

  public AffineTransformation(
          final Vector3f s, // Factores de escala
          final Vector3f axis, // Dirección de eje de rotación
          final float theta, // Ángulo de rotación
          final Object3D object // Objeto sobre el que actuar
  ) {
    this(s, axis, theta, S_IDENTITY, object);
  }

  public AffineTransformation(
          final Vector3f axis, // Dirección de eje de rotación
          final float theta, // Ángulo de rotación
          final Vector3f d, // Vector de traslación
          final Object3D object // Objeto sobre el que actuar
  ) {
    this(D_IDENTITY, axis, theta, d, object);
  }

  public AffineTransformation(
          final Vector3f s, // Factores de escala
          final Vector3f d, // Vector de traslación
          final Object3D object // Objeto sobre el que actuar
  ) {
    this(s, null, R_IDENTITY, d, object);
  }

  public AffineTransformation(
          final Vector3f s, // Factores de escala
          final Vector3f axis, // Dirección de eje de rotación
          final float theta, // Ángulo de rotación
          final Object3D object, // Objeto sobre el que actuar
          final Color color
  ) {
    this(s, axis, theta, D_IDENTITY, object);
    this.color = color;
  }

  public AffineTransformation(
          final Vector3f axis, // Dirección de eje de rotación
          final float theta, // Ángulo de rotación
          final Vector3f d, // Vector de traslación
          final Object3D object, // Objeto sobre el que actuar
          final Color color
  ) {
    this(S_IDENTITY, axis, theta, d, object);
    this.color = color;
  }

  public AffineTransformation(
          final Vector3f s, // Factores de escala
          final Vector3f d, // Vector de traslación
          final Object3D object, // Objeto sobre el que actuar
          final Color color
  ) {
    this(s, null, R_IDENTITY, d, object);
    this.color = color;
  }

  private Matrix4f scaleMatrix(final Vector3f s) {
    Matrix4f r = new Matrix4f();

    r.setRow(0, new Vector4f(s.getX(), 0, 0, 0));
    r.setRow(1, new Vector4f(0, s.getY(), 0, 0));
    r.setRow(2, new Vector4f(0, 0, s.getZ(), 0));
    r.setRow(3, new Vector4f(0, 0, 0, 1));

    return r;
  }

  private Matrix4f rotationMatrix(final Vector3f axis, final float theta) {
    Matrix4f r = new Matrix4f();
    r.setIdentity();

    if (axis == null) {
      return null;
    }

    if (Math.signum(theta) == 0) {
      return r;
    }

    final float axisXSign = Math.signum(axis.getX());
    final float axisYSign = Math.signum(axis.getY());
    final float axisZSign = Math.signum(axis.getX());

    final float cosTheta = (float) Math.cos(theta);
    final float sinTheta = (float) Math.sin(theta);

    // Rotación sobre el eje X
    if (axisXSign > 0 && axisYSign == 0 && axisZSign == 0) {
      r.m11 = cosTheta;
      r.m12 = -sinTheta;
      r.m22 = cosTheta;
      r.m21 = -sinTheta;
      return r;
    }

    // Rotación sobre el eje Y
    if (axisXSign == 0 && axisYSign > 0 && axisZSign == 0) {
      r.m00 = cosTheta;
      r.m02 = sinTheta;
      r.m22 = cosTheta;
      r.m20 = -sinTheta;
      return r;
    }

    // Rotación sobre el eje Z
    if (axisXSign == 0 && axisYSign == 0 && axisZSign > 0) {
      r.m00 = cosTheta;
      r.m01 = -sinTheta;
      r.m11 = cosTheta;
      r.m10 = sinTheta;
      return r;
    }

    throw new UnsupportedOperationException("Rotation axis must be x, y or z.");

  }

  private Matrix4f traslationMatrix(final Vector3f d) {
    Matrix4f r = new Matrix4f();
    r.setIdentity();
    r.setColumn(3, d.getX(), d.getY(), d.getZ(), 1);
    return r;
  }

  @Override
  protected Hit _intersects(Ray ray) {
    final Point3D R = new Point3D(ray.getStartingPoint());
    final Vector3D v = new Vector3D(ray.getDirection());

    // Transformar los elementos definitorios del rayo
    Minv.transform(R);
    Minv.transform(v);
    v.normalize();

    // Construimos el nuevo rayo tranformado
    final Ray transformedRay = new Ray(R, v);

    // Obtenemos la intersección entre el objeto donde se aplica la
    // transformación y el rayo transformado
    final Hit hit = model.intersects(transformedRay);

    // Devolvemos los parámteros del hit al espacio de la escena
    if (hit.hits()) {
      // Transformación del punto de intersección
      final Point3D P = hit.getPoint();
      M.transform(P);

      // Transformación del valor del desplazamiento sobre el rayo (alpha)
      final Vector3D RP = new Vector3D(ray.getStartingPoint(), P);
      final float a = RP.length();

      // Transformación del vector normal
      final Vector3D n = hit.getNormal();
      N.transform(n);
      n.normalize();

      return new Hit(a, P, n, hit.getObject());
    }

    return Hit.NOHIT;
  }
}
