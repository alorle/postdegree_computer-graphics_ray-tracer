package objects;
/**
 *
 * @author MAZ
 */

import java.util.List;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import javax.vecmath.Point2f;

import primitives.Vector3D;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;

public class TriangularMesh extends Object3D {
  
  private final Collection<Triangle> triangles;
   
  public TriangularMesh (final Map<Integer, Point3D> vertices,
                         final List<String> facets,
                         final Color color) {
    
    this(vertices, facets, color, null); 
    
  }
  
  public TriangularMesh (final Map<Integer, Point3D> vertices,
                         final List<String> facets) {
    
    this(vertices, facets, (Color) null, null);
    
  }
  
  private TriangularMesh (final Map<Integer, Point3D> vertices,
                          final List<String> facets,
                          final Color color,
                          final Void diff) {
    
    /* */

  }  

  @Override
  protected Hit _intersects (final Ray ray) {

      return Hit.NOHIT;

  }
  
}