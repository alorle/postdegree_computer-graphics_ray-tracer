package view;

import tracer.RayGenerator;

public abstract class Projection {

  private final float width;  // Anchura ventana de proyección
  private final float height; // Altura ventana de proyección

  protected Projection (final float width, final float height) {
    this.width = width;
    this.height = height;
  }  
  
  public final float getWidth () {
    return width;
  }

  public final float getHeight () {
    return height;
  }

  public abstract RayGenerator getRayGenerator (final Camera c, final int W, final int H);

}