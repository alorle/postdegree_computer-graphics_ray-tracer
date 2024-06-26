package objects.boundingboxes;

import tracer.Ray;

/**
 * @author Álvaro Orduna León
 */
public abstract class BoundingBox {

  public abstract boolean intersect(final Ray ray);
}
