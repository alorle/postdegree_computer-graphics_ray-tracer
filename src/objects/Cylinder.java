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

public final class Cylinder extends Object3D {
  
  private final Point3D B;
  private final Vector3D u;
  private final Vector3D u_opposite;
  private final float halfL;
  private final float r;
  private final float r2;
  private final float rinv;
  private final float d2max;

  public Cylinder (final Point3D B, final Vector3D u, final float r, final float L) { 
    this.B = B;
    this.u = u;
    this.u_opposite = u.opposite();
    this.halfL = L * 0.5f;
    this.r = r;
    this.r2 = r * r;
    this.rinv = (float) (1.0 / r);
    this.d2max = r2 + halfL * halfL;
  }
  
  public Cylinder (final Point3D B,
                   final Vector3D u, 
                   final float r,
                   final float L,
                   final Color color) {
    super(color);
    this.B = B;
    this.u = u;
    this.u_opposite = u.opposite();    
    this.halfL = L * 0.5f;
    this.r = r;    
    this.r2 = r * r;
    this.rinv = (float) (1.0 / r);
    this.d2max = r2 + halfL * halfL;    
  } 
  
  @Override
  protected Hit _intersects (final Ray ray) {  

    return Hit.NOHIT;
    
  }
  
}