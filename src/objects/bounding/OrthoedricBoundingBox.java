package objects.bounding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import objects.bounding.BoundingFace.EclideanPlane;
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

  public OrthoedricBoundingBox(Map<Integer, Point3D> vertices) {
    float X_max = 0;
    float X_min = 0;
    float Y_max = 0;
    float Y_min = 0;
    float Z_max = 0;
    float Z_min = 0;

    Point3D v;
    for (Map.Entry<Integer, Point3D> vertex : vertices.entrySet()) {
      v = vertex.getValue();
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
    int visibleFaces = 0;
    for (final BoundingFace face : faces) {
      if (face.visibleFrom(ray)) {
        if (face.intersects(ray).hits()) {
          return true;
        }
        visibleFaces++;

        // Desde el punto de partida del rayo pueden ser visibles, a lo sumo,
        // tres caras, por tanto, si tres caras visibles han sido testadas y
        // ninguna ha devuelto un Hit válido, entonces no merece la pena seguir
        // comprobando el resto.
        if (visibleFaces >= 3) {
          return false;
        }
      }
    }

    return false;
  }

}
