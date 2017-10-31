package objects;
/**
 *
 * @author MAZ
 */

import java.awt.Color;
import primitives.Vector3D;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;

public final class Plane extends Object3D {
  
  private final Point3D P;  
  private final Vector3D normal;
  private final Vector3D normalOpposite;  
  
  public Plane (final Point3D p, final Vector3D n, final Color color) {
    this.P = new Point3D(p);
    this.normal = n;
    this.normal.normalize();
    this.normalOpposite = normal.opposite();    
    this.color = color;
  }  
  
  public Plane (final Point3D a, final Point3D b, final Point3D c, final Color color) {
    this.P = new Point3D(a);
    final Vector3D BA = new Vector3D(a, b);
    final Vector3D CA = new Vector3D(a, c);
    this.normal = (BA.cross(CA));
    this.normal.normalize();
    this.normalOpposite = normal.opposite();    
    this.color = color;
  }  
    
  @Override
  protected Hit _intersects (final Ray ray) {

    return Hit.NOHIT;

  }

}