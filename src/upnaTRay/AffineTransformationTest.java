package upnaTRay;

/**
 *
 * @author MAZ
 */
import gui.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import objects.transformations.AffineTransformation;
import objects.Group3D;
import objects.TriangularMesh;
import primitives.Point3D;
import primitives.Vector3D;
import view.Camera;
import view.Perspective;

public final class AffineTransformationTest {

  public static void main(final String[] args) {

    final int numReplicas = Integer.parseInt(args[0]);
    final float minBaseScale = Float.parseFloat(args[1]);
    final float maxBaseScale = Float.parseFloat(args[2]);
    final int roundScale = (int) (numReplicas * 0.1f);

    // Definición de vértices y facetas del modelo
    final float w = (float) Math.sqrt(3);
    final Point3D A = new Point3D(0.0f, -1.0f, w - 1);
    final Point3D B = new Point3D(-1.0f, -1.0f, -1.0f);
    final Point3D C = new Point3D(+1.0f, -1.0f, -1.0f);
    final Point3D D = new Point3D(0.0f, w - 1, 0.0f);

    final HashMap<Integer, Point3D> vertex = new HashMap<>();
    vertex.put(0, A);
    vertex.put(1, B);
    vertex.put(2, C);
    vertex.put(3, D);

    final ArrayList<String> facets = new ArrayList<>();
    facets.add("0 1 2");
    facets.add("3 1 0");
    facets.add("3 2 1");
    facets.add("3 0 2");

    /*
    final Triangle ABC = new Triangle(A, B, C, Color.YELLOW);
    final Triangle DBA = new Triangle(D, B, A, Color.RED);
    final Triangle DCB = new Triangle(D, C, B, Color.BLUE);
    final Triangle DAC = new Triangle(D, A, C, Color.GREEN);


    // Modelo
    final Group3D tetraedro = new Group3D();
    tetraedro.addObject(ABC);
    tetraedro.addObject(DBA);
    tetraedro.addObject(DCB);
    tetraedro.addObject(DAC);
     */
    final TriangularMesh tetraedro = new TriangularMesh(vertex, facets);

    // Composición de la escena
    final Group3D scene = new Group3D();

    // Curva de Lissajoux
    final float nx = 4.0f;
    final float ny = 5.0f;
    final float nz = 9.0f;
    final float fy = 0.75f;
    final float fz = 0.50f;

    // Eje de rotación
    final Vector3D axis = new Vector3D(0.0f, 1.0f, 0.0f);
    //axis.normalize();

    // Factor de escala base
    final float baseScale = (maxBaseScale - minBaseScale) / roundScale;

    // Paso para generar la curva de Lissajoux
    final double incrPi = 2 * Math.PI / numReplicas;
    for (int t = 0; t < numReplicas; ++t) {

      // Traslación
      final float x = (float) (35.0 * Math.cos(nx * incrPi * t));
      final float y = (float) (35.0 * Math.sin(ny * incrPi * t + 2 * Math.PI * fy));
      final float z = (float) (35.0 * Math.sin(nz * incrPi * t + 2 * Math.PI * fz));
      final Vector3D d = new Vector3D(x, y, z);

      // Cambio de escala
      final float scaleFactor = minBaseScale + (t % roundScale) * baseScale;
      final Vector3D s = new Vector3D(scaleFactor, scaleFactor, scaleFactor);

      final AffineTransformation transformation = new AffineTransformation(s, /*axis, (float) (3.0 * incrPi * t),*/ d, tetraedro);

      scene.addObject(transformation);

    }

    // Cámara y proyección
    final Point3D V = new Point3D(0.0f, 0.0f, +100.0f);
    final Point3D CC = new Point3D(0.0f, 0.0f, 0.0f);
    final Vector3D up = new Vector3D(0.0f, +1.0f, 0.0f);
    final Camera camera = new Camera(V, CC, up);

    final Perspective projection = new Perspective(60.0f, 1.0f);
    camera.setProjection(projection);

    // Síntesis
    final Image image = new Image("AffineTransformationTest", 800, 800, Color.LIGHT_GRAY);
    image.synthesis(camera, scene);
    image.show();

  }

}
