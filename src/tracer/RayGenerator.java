package tracer;

import view.Camera;

public abstract class RayGenerator {
	
  final protected Camera camera; // Camara para la que se generan los rayos
  final protected float w; // Anchura de la ventana de proyeccion
  final protected float h; // Altura de la ventana de proyeccion
  final protected int W; // Numero de columnas de la imagen raster
  final protected int H; // Numero de filas de la imagen raster
  final protected float wW; // Relación entre w y W
  final protected float hH; // Relación entre h y H
  final protected float whalf; // w / 2
  final protected float hhalf; // h / 2

  protected RayGenerator (final Camera c, final int W, final int H) {

    this.camera = new Camera(c);
    this.w = c.getProjection().getWidth();
    this.h = c.getProjection().getHeight();
    this.W = W;
    this.H = H;
    this.wW = w / W;
    this.hH = h / H;
    this.whalf = w * 0.5f;
    this.hhalf = h * 0.5f;

  }

  /**
   *
   * @param m
   * @param n
   * @return
   */
  public abstract Ray getRay (final int m, final int n);
	
}