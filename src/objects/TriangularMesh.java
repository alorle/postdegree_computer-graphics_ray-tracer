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
import objects.boundingboxes.BoundingBox;
import objects.boundingboxes.OrthoedricBoundingBox;

import primitives.Vector3D;
import primitives.Point3D;
import shaders.Material;
import tracer.Hit;
import tracer.Ray;

public class TriangularMesh extends Object3D {

  private final Collection<MeshTriangle> meshTriangles;
  private final BoundingBox bb;

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final Map<Integer, Point2f> textureVertices,
          final List<String> facets) {
    this(vertices, textureVertices, facets, (Color) null);
  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final Map<Integer, Point2f> textureVertices,
          final List<String> facets,
          final Color color) {
    final Collection<Point3D> points = vertices.values();
    bb = new OrthoedricBoundingBox(points);
    meshTriangles = new ArrayList<>(facets.size());

    // Inicializar el mapa de vértices donde se asocia cada vértice con las
    // normales de las facetas en las que el vértice participa
    Map<Point3D, List<Vector3D>> verticesConNormales = new HashMap<>();
    points.stream().forEach((vertex) -> {
      verticesConNormales.put(vertex, new ArrayList<>());
    });

    final Random rg = new Random(System.nanoTime());
    Collection<Triangle> triangles = new ArrayList<>(facets.size());
    facets.stream()
            .map((facet) -> new StringTokenizer(facet))
            .map((st) -> {
              // Arreglamos los tokens, quedandonos únicmanete con el primer número
              final StringTokenizer st0 = new StringTokenizer(st.nextToken(), "/");
              final StringTokenizer st1 = new StringTokenizer(st.nextToken(), "/");
              final StringTokenizer st2 = new StringTokenizer(st.nextToken(), "/");

              // Obtenemos los índices de los vertices de la faceta
              final int index0 = Integer.parseInt(st0.nextToken());
              final int index1 = Integer.parseInt(st1.nextToken());
              final int index2 = Integer.parseInt(st2.nextToken());

              // Obtenemos los vertices de la faceta
              final Point3D vertex0 = vertices.get(index0);
              final Point3D vertex1 = vertices.get(index1);
              final Point3D vertex2 = vertices.get(index2);

              // Obtenemos el color de la faceta
              final Color _color = (color == null)
                      ? new Color(rg.nextFloat(), rg.nextFloat(), rg.nextFloat())
                      : color;

              final Triangle triangle = new Triangle(vertex0, vertex1, vertex2, _color);

              // Añadir la normal del triángulo obtenido a la lista de normales
              // relacionadas con los vectores que conforman el triángulo
              Vector3D tNormal = triangle.getNormal();
              verticesConNormales.get(vertex0).add(tNormal);
              verticesConNormales.get(vertex1).add(tNormal);
              verticesConNormales.get(vertex2).add(tNormal);

              return triangle;
            }).forEach(triangles::add);

    // Construir el conjunto de MeshTriangles a partir de los triángulos
    // obtenidos y las normales de sus vértices
    triangles.forEach((triangle) -> {
      final List<Vector3D> normalsAtA = verticesConNormales.get(triangle.A);
      final List<Vector3D> normalsAtB = verticesConNormales.get(triangle.B);
      final List<Vector3D> normalsAtC = verticesConNormales.get(triangle.C);

      final Vector3D normalAtA = new Vector3D();
      final Vector3D normalAtB = new Vector3D();
      final Vector3D normalAtC = new Vector3D();

      normalsAtA.forEach((vector) -> {
        normalAtA.add(vector);
      });
      normalsAtB.forEach((vector) -> {
        normalAtB.add(vector);
      });
      normalsAtC.forEach((vector) -> {
        normalAtC.add(vector);
      });

      normalAtA.normalize();
      normalAtB.normalize();
      normalAtC.normalize();

      meshTriangles.add(new MeshTriangle(triangle, normalAtA, normalAtB, normalAtC));
    });
  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final Map<Integer, Point2f> textureVertices,
          final List<String> facets,
          final Material material) {
    final Collection<Point3D> points = vertices.values();
    bb = new OrthoedricBoundingBox(points);
    meshTriangles = new ArrayList<>(facets.size());

    // Inicializar el mapa de vértices donde se asocia cada vértice con las
    // normales de las facetas en las que el vértice participa
    Map<Point3D, List<Vector3D>> verticesConNormales = new HashMap<>();
    points.stream().forEach((vertex) -> {
      verticesConNormales.put(vertex, new ArrayList<>());
    });

    final Random rg = new Random(System.nanoTime());
    Collection<Triangle> triangles = new ArrayList<>(facets.size());
    facets.stream()
            .map((facet) -> new StringTokenizer(facet))
            .map((st) -> {
              // Arreglamos los tokens, quedandonos únicmanete con el primer número
              final StringTokenizer st0 = new StringTokenizer(st.nextToken(), "/");
              final StringTokenizer st1 = new StringTokenizer(st.nextToken(), "/");
              final StringTokenizer st2 = new StringTokenizer(st.nextToken(), "/");

              // Obtenemos los índices de los vertices de la faceta
              final int index0 = Integer.parseInt(st0.nextToken());
              final int index1 = Integer.parseInt(st1.nextToken());
              final int index2 = Integer.parseInt(st2.nextToken());

              // Obtenemos los vertices de la faceta
              final Point3D vertex0 = vertices.get(index0);
              final Point3D vertex1 = vertices.get(index1);
              final Point3D vertex2 = vertices.get(index2);

              final Triangle triangle = new Triangle(vertex0, vertex1, vertex2, material);

              // Añadir la normal del triángulo obtenido a la lista de normales
              // relacionadas con los vectores que conforman el triángulo
              Vector3D tNormal = triangle.getNormal();
              verticesConNormales.get(vertex0).add(tNormal);
              verticesConNormales.get(vertex1).add(tNormal);
              verticesConNormales.get(vertex2).add(tNormal);

              return triangle;
            }).forEach(triangles::add);

    // Construir el conjunto de MeshTriangles a partir de los triángulos
    // obtenidos y las normales de sus vértices
    triangles.forEach((triangle) -> {
      final List<Vector3D> normalsAtA = verticesConNormales.get(triangle.A);
      final List<Vector3D> normalsAtB = verticesConNormales.get(triangle.B);
      final List<Vector3D> normalsAtC = verticesConNormales.get(triangle.C);

      final Vector3D normalAtA = new Vector3D();
      final Vector3D normalAtB = new Vector3D();
      final Vector3D normalAtC = new Vector3D();

      normalsAtA.forEach((vector) -> {
        normalAtA.add(vector);
      });
      normalsAtB.forEach((vector) -> {
        normalAtB.add(vector);
      });
      normalsAtC.forEach((vector) -> {
        normalAtC.add(vector);
      });

      normalAtA.normalize();
      normalAtB.normalize();
      normalAtC.normalize();

      meshTriangles.add(new MeshTriangle(triangle, normalAtA, normalAtB, normalAtC));
    });
  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets) {
    this(vertices, facets, (Color) null);
  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets,
          final Color color) {
    this(vertices, null, facets, color);
  }

  public TriangularMesh(final Map<Integer, Point3D> vertices,
          final List<String> facets,
          final Material material) {
    this(vertices, null, facets, material);
  }

  @Override
  protected Hit _intersects(final Ray ray) {
    if (bb.intersect(ray)) {
      Hit closestHit = Hit.NOHIT;

      for (final MeshTriangle x : meshTriangles) {
        final Hit lastHit = x.intersects(ray);
        if (lastHit.isCloser(closestHit)) {
          closestHit = lastHit;
        }
      }

      return closestHit;
    }

    return Hit.NOHIT;
  }

}
