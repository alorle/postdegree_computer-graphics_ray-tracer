package objects;
/**
 *
 * @author MAZ
 */

import javax.vecmath.Point2f;

import primitives.Vector3D;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;

public class MeshTriangle extends Triangle {
  
  private final Vector3D normalAtA;
  private final Vector3D normalAtB;
  private final Vector3D normalAtC;
  private final Point2f vtA;
  private final Point2f vtB;
  private final Point2f vtC;
 
  MeshTriangle (final Triangle base,
                final Vector3D normalAtA,
                final Vector3D normalAtB,
                final Vector3D normalAtC) {
    super(base);
    this.normalAtA = normalAtA;
    this.normalAtB = normalAtB;
    this.normalAtC = normalAtC;
    this.vtA = this.vtB = this.vtC = null;
  }
    
  @Override
  protected Hit _intersects (final Ray r) {
        
    return Hit.NOHIT;
  
  }

}