package objects;
/**
 *
 * @author MAZ
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import primitives.Vector3D;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;

public class Triangle extends Object3D {
  
  protected final Point3D A;
  protected final Point3D B;
  protected final Point3D C;
  protected final Vector3D normal;
  protected final Vector3D AB;
  protected final Vector3D AC;  
  protected final double factorM;
  
  protected Triangle (final Triangle x) {
    A = x.A;
    B = x.B;
    C = x.C;
    AB = x.AB;
    AC = x.AC;
    normal = x.normal;
    factorM = x.factorM;
    color = x.color;
  }
  
  private Triangle (final Point3D a, final Point3D b, final Point3D c) {
    A = new Point3D(a);
    B = new Point3D(b);
    C = new Point3D(c);
    AB = new Vector3D(A, B);
    AC = new Vector3D(A, C);
    normal = (AB.cross(AC));
    factorM = 1.0 / normal.length();
    normal.normalize();
  }
  
  public Triangle (final Point3D a, final Point3D b, final Point3D c, final Color color) {
    this(a, b, c);
    this.color = color;
  }
    
  @Override
  protected Hit _intersects (final Ray r) {
 
    return Hit.NOHIT;
  
  }
  
  public final Vector3D getNormal () {
    return this.normal;
  }
  
  protected final Point3D getA () {
    return this.A;
  }
  
  protected final Point3D getB () {
    return this.B;
  }
  
  protected final Point3D getC () {
    return this.C;
  }

}