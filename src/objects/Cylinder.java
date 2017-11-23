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

  public Cylinder(final Point3D B, final Vector3D u, final float r, final float L) {
    this.B = B;
    this.u = u;
    this.u_opposite = u.opposite();
    this.halfL = L * 0.5f;
    this.r = r;
    this.r2 = r * r;
    this.rinv = (float) (1.0 / r);
    this.d2max = r2 + halfL * halfL;
  }

  public Cylinder(final Point3D B,
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
  protected Hit _intersects(final Ray ray) {
    final Point3D R = ray.getStartingPoint();
    final Vector3D v = ray.getDirection();

    final Vector3D RB = new Vector3D(R, B);
    final float RBdotU = RB.dot(u);

    final float d2 = RB.dot(RB) - (RBdotU * RBdotU);

    if (Math.signum(d2 - r2) > 0) {
      // Punto de partida del rayo en el exterior del cilindro infinito
      final Vector3D crossVU = v.cross(u);
      final float crossVU2 = crossVU.dot(crossVU);
      final float crossVUMag = crossVU.length();
      final float crossVUMagInv = 1 / crossVUMag;

      if (Math.signum(crossVU2) != 0) {
        // Rayo no paralelo al eje del cilindro
        final float d = crossVUMagInv * RB.dot(crossVU);

        final float k2 = r2 - d * d;
        if (Math.signum(k2) >= 0) {
          // Rayo intersecta con el cilindro infinito
          final float p = RB.cross(u).dot(crossVU) / crossVU2;
          final float s = (float) Math.sqrt(k2 / crossVU2);
          final float tIn = p - s;

          if (Math.signum(tIn) > 0) {
            // Intersección en semiespacio posterior
            final Point3D Pin = ray.pointAtParameter(tIn);

            // Debemos comprobar si la distancia paralela a u entre P y B es
            // menor que L/2
            final Vector3D PinB = new Vector3D(Pin, B);
            final float aIn = PinB.dot(u);

            if (Math.signum(halfL - Math.abs(aIn)) > 0) {
              return new Hit(tIn, Pin,
                      new Vector3D(Pin, ray.pointAtParameter(aIn)), this);
            }

            // Aún puede intersectar por las tapas. En este caso, tendríamos que
            // punto de salida del rayo está al otro lado de la tapa con la que
            // intersecte, por lo que bastaría con mirar que el punto del rayo
            // con alpha = p + s está al otro lado del plano que define la tapa.
            final float tOut = p + s;
            final Point3D Pout = ray.pointAtParameter(tOut);

            final Vector3D PoutB = new Vector3D(Pout, B);
            final float aOut = PoutB.dot(u);

            if (Math.signum(halfL - Math.abs(aOut)) > 0) {
              return new Hit(aOut, Pout,
                      new Vector3D(Pout, ray.pointAtParameter(aOut)), this);
            }
          }
        }
      }
    } else {
      // Punto de partida del rayo en el interior del cilindro infinito
    }

    return Hit.NOHIT;

  }

}
