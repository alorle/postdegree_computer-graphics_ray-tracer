package objects.boundingboxes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import objects.boundingboxes.BoundingFace.EclideanPlane;
import primitives.Point3D;
import tracer.Ray;

/**
 * @author Álvaro Orduna León
 */
public final class OrthoedricBoundingBox extends BoundingBox {

  private final Point3D center;
  private final float Xlen;
  private final float Ylen;
  private final float Zlen;

  private final ArrayList<BoundingFace> faces = new ArrayList();

  public OrthoedricBoundingBox(Collection<Point3D> vertices) {
    float X_max = 0;
    float X_min = 0;
    float Y_max = 0;
    float Y_min = 0;
    float Z_max = 0;
    float Z_min = 0;

    for (Point3D v : vertices) {
      if (X_max < v.x) {
        X_max = v.x;
      }
      if (X_min > v.x) {
        X_min = v.x;
      }
      if (Y_max < v.y) {
        Y_max = v.y;
      }
      if (Y_min > v.y) {
        Y_min = v.y;
      }
      if (Z_max < v.z) {
        Z_max = v.z;
      }
      if (Z_min > v.z) {
        Z_min = v.z;
      }
    }

    center = new Point3D(
            (X_max + X_min) * 0.5f,
            (Y_max + Y_min) * 0.5f,
            (Z_max + Z_min) * 0.5f);

    Xlen = X_max - X_min;
    Ylen = Y_max - Y_min;
    Zlen = Z_max - Z_min;

    faces.add(new BoundingFace(this, EclideanPlane.YZ, X_max, true));
    faces.add(new BoundingFace(this, EclideanPlane.YZ, X_min, false));
    faces.add(new BoundingFace(this, EclideanPlane.ZX, Y_max, true));
    faces.add(new BoundingFace(this, EclideanPlane.ZX, Y_min, false));
    faces.add(new BoundingFace(this, EclideanPlane.XY, Z_max, true));
    faces.add(new BoundingFace(this, EclideanPlane.XY, Z_min, false));
  }

  public Point3D getCenter() {
    return center;
  }

  public float getXlen() {
    return Xlen;
  }

  public float getYlen() {
    return Ylen;
  }

  public float getZlen() {
    return Zlen;
  }

  @Override
  public boolean intersect(Ray ray) {
    // Si el rayo intersecta con alguna de las caras, devolvemos verdadero
    return faces.stream().anyMatch((face) -> (face.intersects(ray).hits()));
  }

}
