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

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets,
          final Color color) {

    this(vertices, facets, color, null);

  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets) {

    this(vertices, facets, (Color) null, null);

  }

  private TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets,
          final Color color,
          final Void diff) {

    triangles = new ArrayList<>(facets.size());
    final Random rg = new Random(System.nanoTime());

    facets.stream().map((facet) -> new StringTokenizer(facet)).map((st) -> {
      // Obtenemos los índices de los vertices de la faceta
      final int index0 = Integer.parseInt(st.nextToken());
      final int index1 = Integer.parseInt(st.nextToken());
      final int index2 = Integer.parseInt(st.nextToken());

      // Obtenemos los vertices de la faceta
      final Point3D vertex0 = vertices.get(index0);
      final Point3D vertex1 = vertices.get(index1);
      final Point3D vertex2 = vertices.get(index2);

      // Obtenemos el color de la faceta
      final Color _color = (color != null)
              ? color
              : new Color(rg.nextFloat(), rg.nextFloat(), rg.nextFloat());

      return new Triangle(vertex0, vertex1, vertex2, _color);
    }).forEachOrdered((obj) -> {
      triangles.add(obj);
    });
  }

  @Override
  protected Hit _intersects(final Ray ray) {

    return Hit.NOHIT;

  }

}
