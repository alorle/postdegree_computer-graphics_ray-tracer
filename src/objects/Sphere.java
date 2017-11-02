package objects;

import java.awt.Color;

import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

public class Sphere extends Object3D {

  private final Point3D centro;
  private final float radio;
  private final float inv_radio;

  private Sphere(final Point3D centro, final float radio) {
    this.centro = new Point3D(centro);
    this.radio = radio;
    this.inv_radio = (float) (1.0 / radio);
  }

  public Sphere(final Point3D centro, final float radio, final Color color) {
    this(centro, radio);
    this.color = color;
  }

  @Override
  protected Hit _intersects(final Ray ray) {

    return Hit.NOHIT;

  }

}
