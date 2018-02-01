package lights;
/**
 * Clase para establecer la potencia irradiada
 * por cada canal (700.0nm, 546.1, 535.8)
 * de acuerdo a la distribucion de porcentajes
 * indicada.
 * 
 * La potencia en cada canal se ajusta de acuerdo
 * a la cantidad de vatios necesarios para conseguir
 * un incremento de 1 unidad de luminancia en el canal
 * correspondiente. Es decir, se prepara la conversion
 * de potencia radiante a potencia luminosa.
 * 
 * Por ejemplo: para conseguir una fuente de luz de color magenta
 * se pueden establecer los coeficientes (0.8f, 0.0f, 0.2f) que
 * indican que el 80% de la potencia luminosa se irradia a traves
 * del canal rojo y el 20% a traves del canal azul.
 * 
 * La definición de esta clase está completa
 *
 * @author MAZ
 */

public final class SpectrumRGB {
  
  private static final float RADIANT_POWER_RATIO_R = +72.0962f; // ratio at 700.0 nm
  private static final float RADIANT_POWER_RATIO_G =  +1.3791f; // ratio at 546.1 nm
  private static final float RADIANT_POWER_RATIO_B =  +1.0000f; // ratio at 435.8 nm
  private static final float POWER_RATIO =
          RADIANT_POWER_RATIO_R +
          RADIANT_POWER_RATIO_G +
          RADIANT_POWER_RATIO_B;
  private static final float RELATIVE_RADIANT_POWER_RATIO_R = RADIANT_POWER_RATIO_R / POWER_RATIO;
  private static final float RELATIVE_RADIANT_POWER_RATIO_G = RADIANT_POWER_RATIO_G / POWER_RATIO;
  private static final float RELATIVE_RADIANT_POWER_RATIO_B = RADIANT_POWER_RATIO_B / POWER_RATIO;  

  private final float xr;
  private final float xg;
  private final float xb;
  
  public SpectrumRGB (final float xr, final float xg, final float xb) {
    if ((Math.signum(xr) < 0) || (Math.signum(xg) < 0) || (Math.signum(xb) < 0) || (Math.abs((xr + xg + xb) - 1.0f) > 0.5e-6f))
      throw new IllegalArgumentException("Bad spectrum");
    this.xr = xr * RELATIVE_RADIANT_POWER_RATIO_R;
    this.xg = xg * RELATIVE_RADIANT_POWER_RATIO_G;
    this.xb = xb * RELATIVE_RADIANT_POWER_RATIO_B;
  }  
  
  // A emplear al construir una fuente luminosa.
  // Ejemplo: sea power una cantida en vatios y sea spectrum un objeto de esta
  // clase; entonces
  //         spectrum.distribute(power)
  // devuelve un objeto que indica la irradiación en vatios por cada canal
  // que corresponde al espectro dado.
  // 
  public RadianceRGB distribute (final float power) {
    return new RadianceRGB(power * xr, power * xg, power * xb);
  }
  
}