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
            final float dIn = PinB.dot(u);

            if (Math.signum(halfL - Math.abs(dIn)) > 0) {
              return new Hit(tIn, Pin,
                      new Vector3D(Pin, ray.pointAtParameter(dIn)), this);
            }

            // Aún puede intersectar por las tapas. En este caso, tendríamos que
            // punto de salida del rayo está al otro lado de la tapa con la que
            // intersecte, por lo que bastaría con mirar que el punto del rayo
            // con alpha = p + s está al otro lado del plano que define la tapa.
            final float tOut = p + s;
            final Point3D Pout = ray.pointAtParameter(tOut);

            final Vector3D PoutB = new Vector3D(Pout, B);
            final float dOut = PoutB.dot(u);

            if (Math.signum(halfL + dOut) > 0 && Math.signum(dIn) < 0) {
              // Rayo intersecta con tapa delantera (siguiendo el sentido de u)
              final Vector3D RTapa = new Vector3D(R, B.add(halfL, u));
              final float t = RTapa.dot(u) / v.dot(u);

              if (Math.signum(t) >= 0) {
                return new Hit(t, ray.pointAtParameter(t), u, this);
              }
            } else if (Math.signum(halfL - dOut) > 0 && Math.signum(dIn) > 0) {
              // Rayo intersecta con tapa trasera (siguiendo el sentido de u)
              final Vector3D RTapa = new Vector3D(R, B.add(halfL, u_opposite));
              final float t = RTapa.dot(u_opposite) / v.dot(u_opposite);

              if (Math.signum(t) >= 0) {
                return new Hit(t, ray.pointAtParameter(t), u_opposite, this);
              }
            }
          }
        }
      }
    } else {
      // Punto de partida del rayo en el interior del cilindro infinito

      if (Math.signum(Math.abs(RBdotU) - halfL) > 0) {
        // Punto de partida del rayo fuera del cilindro finito, por lo que
        // solo podrá intersectar con una tapa
        final Vector3D n = Math.signum(RBdotU) < 0 ? u : u_opposite;
        final Point3D c = B.add(halfL, n);
        final Plane p = new Plane(c, n, color);
        final Hit hit = p.intersects(ray);

        if (Math.signum(r2 - hit.getPoint().distanceSquared(c)) >= 0) {
          return hit;
        }
      } else {
        // Punto de partida del rayo dentro del cilindro infinito, por lo que
        // puede intersectar con la pared o con una tapa
        final Vector3D crossVU = v.cross(u);
        final float crossVU2 = crossVU.dot(crossVU);

        if (Math.signum(crossVU2) != 0) {
          // Rayo no paralelo al eje del cilindro
          final float crossVUMag = crossVU.length();
          final float crossVUMagInv = 1 / crossVUMag;
          final float d = crossVUMagInv * RB.dot(crossVU);

          final float k2 = r2 - d * d;
          final float p = RB.cross(u).dot(crossVU) / crossVU2;
          final float s = (float) Math.sqrt(k2 / crossVU2);
          final float tOut = p + s;
          final Point3D Pout = ray.pointAtParameter(tOut);

          final Vector3D PoutB = new Vector3D(Pout, B);
          final float dOut = PoutB.dot(u);

          if (Math.signum(halfL - Math.abs(dOut)) > 0) {
            // Rayo sale del cilindro por la pared del cilindro
            return new Hit(tOut, Pout,
                    new Vector3D(ray.pointAtParameter(dOut), Pout), this);
          } else {
            // Rayo sale del cilindro por una tapa
            final Vector3D n = Math.signum(RBdotU) > 0 ? u : u_opposite;
            final Vector3D RTapa = new Vector3D(R, B.add(halfL, n));
            final float t = RTapa.dot(n) / v.dot(n);

            if (Math.signum(t) >= 0) {
              return new Hit(t, ray.pointAtParameter(t), n, this);
            }
          }
        } else {
          // Rayo paralelo al eje del cilindro
          final Vector3D n = Math.signum(RBdotU) < 0 ? u : u_opposite;
          final Vector3D RTapa = new Vector3D(R, B.add(halfL, n));
          final float t = RTapa.dot(n) / v.dot(n);

          if (Math.signum(t) >= 0) {
            return new Hit(t, ray.pointAtParameter(t), n, this);
          }
        }
      }
    }

    return Hit.NOHIT;

  }

}
