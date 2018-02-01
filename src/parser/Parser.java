package parser;

/**
 *
 * La definición de esta clase está completa (aunque pueda tener errores)
 *
 * @author MAZ
 */

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import javax.vecmath.Point2f;

import gui.Image;
import lights.Directional;
import lights.Light;
import lights.LightGroup;
import lights.Omnidirectional;
import lights.Spot;
import lights.Ambiental;
import shaders.FilterRGB;
import lights.SpectrumRGB;
import objects.AffineTransformation;
import objects.Group3D;
import objects.Object3D;
import objects.Plane;
import objects.Sphere;
import objects.Triangle;
import objects.TriangularMesh;
import objects.Cylinder;
import primitives.Point3D;
import primitives.Vector3D;
import view.Camera;
import view.Angular;
import view.Orthographic;
import view.Perspective;
import view.Projection;
import view.Hemispherical;
//import objects.normalgenerators.AffineNormalGenerator;
//import objects.normalgenerators.NormalGenerator;
//import objects.normalgenerators.SimpleNormalGenerator;
//import objects.normalgenerators.SubdividedAffineNormalGenerator;
import shaders.densityfunctions.Blinn;
import shaders.densityfunctions.Horn;
import shaders.PhongLafortune;
import shaders.densityfunctions.MicrofacetDensityFunction;
import shaders.AshikminShirley;
import shaders.BRDF;
import shaders.CookTorranceSparrow;
import shaders.Material;
import shaders.densityfunctions.Torrance1;
import shaders.densityfunctions.Torrance2;

public final class Parser {

  private final HashMap<String, Material> materialCollection;
  private final Image viewport;
  private final Camera camera;
  private final Projection projection;
  private final Group3D scene;
  private final LightGroup lights;

  public Parser(final File in) throws ParserConfigurationException, SAXException, IOException {

    final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    final Document doc = dBuilder.parse(in);

    final NodeList rootElementList = doc.getElementsByTagName("image");

    if (rootElementList.getLength() > 0) {

      final Element rootElement = (Element) rootElementList.item(0);
      final String imageName = rootElement.getAttribute("name");

      this.viewport = this.parseViewport(imageName, rootElement);
      this.camera = this.parseCamera(rootElement);
      this.projection = this.parseProjection(rootElement);
      this.lights = this.parseLights(rootElement);
      this.materialCollection = this.parseMaterials(rootElement);
      this.scene = this.parseScene(rootElement);

    } else {
      throw new SAXException("Elemento <image> no encontrado");
    }

  }

  private Image parseViewport(final String tag, final Element doc) throws SAXException {

    final NodeList viewportNodeList = doc.getElementsByTagName("viewport");

    if (viewportNodeList.getLength() > 0) {

      final Element el = (Element) viewportNodeList.item(0);
      final int width = Integer.parseInt(
              el.getElementsByTagName("width").item(0).getTextContent());
      final int height = Integer.parseInt(
              el.getElementsByTagName("height").item(0).getTextContent());
      final Color backgroundColor = this.parseColor(
              el.getElementsByTagName("backgroundColor").item(0).getTextContent());

      return new Image(tag, height, width, backgroundColor);

    } else {
      throw new SAXException("Elemento <viewport> no encontrado");
    }

  }

  private Camera parseCamera(final Element doc) throws SAXException {

    final NodeList cameraNodeList = doc.getElementsByTagName("camera");

    if (cameraNodeList.getLength() > 0) {
      final Element el = (Element) cameraNodeList.item(0);
      final Point3D pos = this.parsePoint3D(
              el.getElementsByTagName("position").item(0).getTextContent());
      final Point3D lookAt = this.parsePoint3D(
              el.getElementsByTagName("lookAt").item(0).getTextContent());
      final Vector3D up = this.parseVector3D(
              el.getElementsByTagName("up").item(0).getTextContent());

      return new Camera(pos, lookAt, up);

    } else {
      throw new SAXException("Elemento <camera> no encontrado");
    }

  }

  private Projection parseProjection(final Element doc) throws SAXException {

    final NodeList projectionNodeList = doc.getElementsByTagName("projection");

    if (projectionNodeList.getLength() > 0) {
      final Element el = (Element) projectionNodeList.item(0);

      final Projection _projection;
      switch (el.getAttribute("type")) {

        case "perspective": {
          final float fov = Float.parseFloat(
                  el.getElementsByTagName("fov").item(0).getTextContent());
          final float aspect = Float.parseFloat(
                  el.getElementsByTagName("aspect").item(0).getTextContent());

          _projection = new Perspective(fov, aspect);
        }
        break;

        case "orthographic": {
          final float height = Float.parseFloat(
                  el.getElementsByTagName("height").item(0).getTextContent());
          final float aspect = Float.parseFloat(
                  el.getElementsByTagName("aspect").item(0).getTextContent());

          _projection = new Orthographic(height, aspect);
        }
        break;

        case "hemispherical": {
          final float factor = Float.parseFloat(
                  el.getElementsByTagName("distortionFactor").item(0).getTextContent());
          _projection = new Hemispherical(factor);
        }
        break;

        case "angular": {
          final float fov = Float.parseFloat(
                  el.getElementsByTagName("fov").item(0).getTextContent());

          _projection = new Angular(fov);
        }
        break;

        default: {
          _projection = null;
        }
      }

      return _projection;

    } else {
      throw new SAXException("Elemento <projection> no encontrado");
    }

  }

  private Group3D parseScene(final Element doc) throws SAXException {

    final HashMap<String, Object3D> map = new HashMap<>();

    final NodeList sceneNodeList = doc.getElementsByTagName("scene");

    if (sceneNodeList.getLength() > 0) {

      final Group3D g = new Group3D();

      final Element sceneElement = (Element) sceneNodeList.item(0);
      final NodeList objects = sceneElement.getElementsByTagName("object");
      for (int j = 0; j < objects.getLength(); ++j) {
        final Element el = (Element) objects.item(j);
        g.addObject(this.parseObject(el, map));
      }

      return g;

    } else {
      throw new SAXException("Elemento <scene> no encontrado");
    }

  }

  private Object3D parseObject(final Element el, final HashMap<String, Object3D> map) throws SAXException {

    final String id = el.getAttribute("id");

    Material material = null;
    Color color = null;

    final NodeList shaderIdList = el.getElementsByTagName("shaderId");
    if (shaderIdList.getLength() > 0) {
      final String shaderId = shaderIdList.item(0).getTextContent();
      material = this.materialCollection.get(shaderId);
    } else {
      final NodeList colorList = el.getElementsByTagName("color");
      if (colorList.getLength() > 0) {
        final String colorElementText = colorList.item(0).getTextContent();
        color = parseColor(colorElementText);
      }
    }

    final Object3D object;
    switch (el.getAttribute("type")) {
      case "sphere": {
        final Point3D center = this.parsePoint3D(
                el.getElementsByTagName("center").item(0).getTextContent());
        final float radius = Float.parseFloat(
                el.getElementsByTagName("radius").item(0).getTextContent());

        if (material != null) {
          object = new Sphere(center, radius, material);
        } else {
          object = new Sphere(center, radius, color);
        }
      }
      break;

      case "plane": {
        final Point3D point = this.parsePoint3D(
                el.getElementsByTagName("point").item(0).getTextContent());
        final Vector3D normal = this.parseVector3D(
                el.getElementsByTagName("normal").item(0).getTextContent());

        if (material != null) {
          object = new Plane(point, normal, material);
        } else {
          object = new Plane(point, normal, color);
        }
      }
      break;

      case "triangle": {
        final Point3D vertex0 = this.parsePoint3D(
                el.getElementsByTagName("vertex0").item(0).getTextContent());
        final Point3D vertex1 = this.parsePoint3D(
                el.getElementsByTagName("vertex1").item(0).getTextContent());
        final Point3D vertex2 = this.parsePoint3D(
                el.getElementsByTagName("vertex2").item(0).getTextContent());

        if (material != null) {
          object = new Triangle(vertex0, vertex1, vertex2, material);
        } else // if (color != null)
        {
          object = new Triangle(vertex0, vertex1, vertex2, color);
        }

      }
      break;

      case "cylinder": {
        final Point3D center = this.parsePoint3D(
                el.getElementsByTagName("center").item(0).getTextContent());
        final Vector3D axedirection = this.parseVector3D(
                el.getElementsByTagName("axe").item(0).getTextContent());
        final float radius = Float.parseFloat(
                el.getElementsByTagName("radius").item(0).getTextContent());
        final float L = Float.parseFloat(
                el.getElementsByTagName("length").item(0).getTextContent());

        axedirection.normalize();
        if (material != null) {
          object = new Cylinder(center, axedirection, radius, L, material);
        } else // if (color != null)
        {
          object = new Cylinder(center, axedirection, radius, L, color);
        }

      }
      break;

      case "triangular": {

        final HashMap<Integer, Point3D> vertices = new HashMap<>();
        final Element verticesElement = (Element) el.getElementsByTagName("vertices").item(0);
        final NodeList vertexList = verticesElement.getElementsByTagName("vertex");
        for (int j = 0; j < vertexList.getLength(); ++j) {
          vertices.put(j + 1, this.parsePoint3D(vertexList.item(j).getTextContent()));
        }

        final HashMap<Integer, Point2f> textureVertices = new HashMap<>();
        if (el.getElementsByTagName("textureCoordinates").getLength() > 0) {
          final Element textureCoordiantesElement
                  = (Element) el.getElementsByTagName("textureCoordinates").item(0);
          final NodeList textureList = textureCoordiantesElement.getElementsByTagName("texture");
          for (int j = 0; j < textureList.getLength(); ++j) {
            textureVertices.put(j + 1, this.parsePoint2f(textureList.item(j).getTextContent()));
          }
        }

        final List<String> facets = new ArrayList<>();
        final Element facetsElement = (Element) el.getElementsByTagName("facets").item(0);
        final NodeList facetList = facetsElement.getElementsByTagName("facet");
        for (int j = 0; j < facetList.getLength(); ++j) {
          facets.add(facetList.item(j).getTextContent());
        }

        if (textureVertices.size() > 0) {
          if (material != null) {
            object = new TriangularMesh(vertices, textureVertices, facets, material);
          } else if (color != null) {
            object = new TriangularMesh(vertices, textureVertices, facets, color);
          } else {
            object = new TriangularMesh(vertices, textureVertices, facets);
          }
        } else {
          if (material != null) {
            object = new TriangularMesh(vertices, facets, material);
          } else if (color != null) {
            object = new TriangularMesh(vertices, facets, color);
          } else {
            object = new TriangularMesh(vertices, facets);
          }
        }

      }
      break;

      case "transform": {

        final String modelId
                = el.getElementsByTagName("model").item(0).getTextContent();

        final Object3D model = map.get(modelId);

        final Vector3D translation;
        final NodeList translationNodeList = el.getElementsByTagName("translation");
        if (translationNodeList.getLength() > 0) {
          translation = this.parseVector3D(translationNodeList.item(0).getTextContent());
        } else {
          translation = null; // new Vector3D(0.0f, 0.0f, 0.0f);
        }
        final Vector3D scaleFactors;
        final NodeList scaleFactorsNodeList = el.getElementsByTagName("scaleFactors");
        if (scaleFactorsNodeList.getLength() > 0) {
          scaleFactors = this.parseVector3D(scaleFactorsNodeList.item(0).getTextContent());
        } else {
          scaleFactors = null; // new Vector3D(1.0f, 1.0f, 1.0f);
        }
        final Vector3D rotationAxis;
        final NodeList rotationAxisNodeList = el.getElementsByTagName("rotationAxis");
        if (rotationAxisNodeList.getLength() > 0) {
          rotationAxis = this.parseVector3D(rotationAxisNodeList.item(0).getTextContent());
        } else {
          rotationAxis = new Vector3D(1.0f, 0.0f, 0.0f);
        }

        final float rotationAngle;
        final NodeList rotationAngleNodeList = el.getElementsByTagName("rotationAngle");
        if (rotationAngleNodeList.getLength() > 0) {
          rotationAngle = Float.parseFloat(rotationAngleNodeList.item(0).getTextContent());
        } else {
          rotationAngle = 0.0f;
        }

        if (scaleFactors != null) {
          if (Math.signum(rotationAngle) != 0) {

            if (translation != null) {
              if (material != null) {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, translation, model, material);
              } else if (color != null) {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, translation, model, color);
              } else {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, translation, model);
              }
            } else { // translation == null
              if (material != null) {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, model, material);
              } else if (color != null) {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, model, color);
              } else {
                object = new AffineTransformation(scaleFactors, rotationAxis, rotationAngle, model);
              }
            }

          } else { // rotationAngle == 0

            if (translation != null) {
              if (material != null) {
                object = new AffineTransformation(scaleFactors, translation, model, material);
              } else if (color != null) {
                object = new AffineTransformation(scaleFactors, translation, model, color);
              } else {
                object = new AffineTransformation(scaleFactors, translation, model);
              }
            } else { // translation == null
              // Se define un vector de desplazamiento nulo
              if (material != null) {
                object = new AffineTransformation(scaleFactors, new Vector3D(0.0f, 0.0f, 0.0f), model, material);
              } else if (color != null) {
                object = new AffineTransformation(scaleFactors, new Vector3D(0.0f, 0.0f, 0.0f), model, color);
              } else {
                object = new AffineTransformation(scaleFactors, new Vector3D(0.0f, 0.0f, 0.0f), model);
              }
            }

          }

        } else { // scaleFactors == null

          if (translation != null) {
            if (material != null) {
              object = new AffineTransformation(rotationAxis, rotationAngle, translation, model, material);
            } else if (color != null) {
              object = new AffineTransformation(rotationAxis, rotationAngle, translation, model, color);
            } else {
              object = new AffineTransformation(rotationAxis, rotationAngle, translation, model);
            }
          } else { // translation == null
            // Se define un vector de desplazamiento nulo
            if (material != null) {
              object = new AffineTransformation(rotationAxis, rotationAngle, new Vector3D(0.0f, 0.0f, 0.0f), model, material);
            } else if (color != null) {
              object = new AffineTransformation(rotationAxis, rotationAngle, new Vector3D(0.0f, 0.0f, 0.0f), model, color);
            } else {
              object = new AffineTransformation(rotationAxis, rotationAngle, new Vector3D(0.0f, 0.0f, 0.0f), model);
            }
          }

        }

      }
      break;

      default: {
        throw new SAXException("Objeto " + el.getAttribute("type") + " de clase desconocida");
      }

    }

    if (id.length() > 0) {
      map.put(id, object);
    }

    return object;

  }

  private LightGroup parseLights(final Element doc) {

    final LightGroup g = new LightGroup();

    final NodeList lightsElementList = doc.getElementsByTagName("lights");
    if (lightsElementList.getLength() > 0) {

      for (int k = 0; k < lightsElementList.getLength(); ++k) {

        final Element lightsElement = (Element) lightsElementList.item(k);
        final NodeList lightsList = lightsElement.getElementsByTagName("light");
        for (int j = 0; j < lightsList.getLength(); ++j) {
          final Element lightElement = (Element) lightsList.item(j);
          g.addLight(this.parseLight(lightElement));
        }

      }

    }

    return g;

  }

  private Light parseLight(final Element el) {

    final SpectrumRGB spectrum = this.parseSpectrum(
            el.getElementsByTagName("spectrum").item(0).getTextContent());
    final float power = Float.parseFloat(
            el.getElementsByTagName("power").item(0).getTextContent());

    final Light light;
    switch (el.getAttribute("type")) {

      case "ambiental": {
        light = new Ambiental(spectrum, power);
      }
      break;

      case "omnidirectional": {
        final Point3D position = this.parsePoint3D(
                el.getElementsByTagName("position").item(0).getTextContent());
        light = new Omnidirectional(position, spectrum, power);
      }
      break;

      case "directional": {
        final Point3D position = this.parsePoint3D(
                el.getElementsByTagName("position").item(0).getTextContent());
        final Point3D lookAt = this.parsePoint3D(
                el.getElementsByTagName("lookAt").item(0).getTextContent());
        final float radius = Float.parseFloat(
                el.getElementsByTagName("radius").item(0).getTextContent());
        final String attenuationExponent
                = el.getElementsByTagName("attenuationExponent").item(0).getTextContent();

        final Directional _light
                = new Directional(position, lookAt, radius, spectrum, power);

        if (attenuationExponent.length() > 0) {
          _light.setAttenuationExponent(Float.parseFloat(attenuationExponent));
        }

        light = _light;
      }
      break;

      case "spot": {
        final Point3D position = this.parsePoint3D(
                el.getElementsByTagName("position").item(0).getTextContent());
        final Point3D lookAt = this.parsePoint3D(
                el.getElementsByTagName("lookAt").item(0).getTextContent());
        final float aperture = Float.parseFloat(
                el.getElementsByTagName("aperture").item(0).getTextContent());
        final String attenuationExponent
                = el.getElementsByTagName("attenuationExponent").item(0).getTextContent();

        final Spot _light
                = new Spot(position, lookAt, aperture, spectrum, power);

        if (attenuationExponent.length() > 0) {
          _light.setAttenuationExponent(Float.parseFloat(attenuationExponent));
        }

        light = _light;
      }
      break;

      default: {
        light = null;
      }

    }

    return light;
  }

  private HashMap<String, Material> parseMaterials(final Element doc) {

    final HashMap<String, Material> map = new HashMap<>();

    final NodeList shadersElementList = doc.getElementsByTagName("materials");
    for (int k = 0; k < shadersElementList.getLength(); ++k) {

      final Element shadersElement = (Element) shadersElementList.item(0);
      final NodeList shaders = shadersElement.getElementsByTagName("material");
      for (int j = 0; j < shaders.getLength(); ++j) {
        final Element el = (Element) shaders.item(j);
        this.parseMaterial(el, map);
      }

    }

    return map;

  }

  private Material parseMaterial(final Element el, final HashMap<String, Material> map) {

    final String id = el.getAttribute("id");

    final FilterRGB diffuse;
    if (el.getElementsByTagName("diffuseFilter").getLength() > 0) {
      diffuse = parseFilter(
              el.getElementsByTagName("diffuseFilter").item(0).getTextContent());
    } else {
      diffuse = new FilterRGB(0.0f, 0.0f, 0.0f);
    }

    final FilterRGB specular;
    if (el.getElementsByTagName("specularFilter").getLength() > 0) {
      specular = parseFilter(
              el.getElementsByTagName("specularFilter").item(0).getTextContent());
    } else {
      specular = new FilterRGB(0.0f, 0.0f, 0.0f);
    }

    final FilterRGB ambient;
    if (el.getElementsByTagName("ambientFilter").getLength() > 0) {
      ambient = parseFilter(
              el.getElementsByTagName("ambientFilter").item(0).getTextContent());
    } else {
      ambient = new FilterRGB(0.0f, 0.0f, 0.0f);
    }

    final float beta;
    if (el.getElementsByTagName("beta").getLength() > 0) {
      beta = Float.parseFloat(el.getElementsByTagName("beta").item(0).getTextContent());
    } else {
      beta = 0.0f;
    }

    final MicrofacetDensityFunction densityFunction;
    if (el.getElementsByTagName("densityFunction").getLength() > 0) {

      final String reflectanceFunctionalName
              = el.getElementsByTagName("densityFunction").item(0).getTextContent();
      switch (reflectanceFunctionalName) {

        case "Horn": {
          densityFunction = new Horn(beta);
        }
        break;

        case "Blinn": {
          densityFunction = new Blinn(beta);
        }
        break;

        case "Torrance1": {
          densityFunction = new Torrance1(beta);
        }
        break;

        case "Torrance2": {
          densityFunction = new Torrance2(beta);
        }
        break;

        default: {
          densityFunction = new Blinn(beta);
        }

      }

    } else {
      densityFunction = new Blinn(beta);
    }

    final boolean recursion
            = el.getElementsByTagName("specularRecursion").item(0).getTextContent().toUpperCase().compareTo("YES") == 0;

    final BRDF shader;
    switch (el.getAttribute("type")) {

      case "Phong-Lafortune": {
        shader = new PhongLafortune(ambient, diffuse, specular, densityFunction);
      }
      break;

      case "Ashikmin-Shirley": {
        shader = new AshikminShirley(diffuse, specular, densityFunction);
      }
      break;

      case "Cook-Torrance-Sparrow": {
        shader = new CookTorranceSparrow(diffuse, specular, densityFunction, true);
      }
      break;

      default: {
        shader = new PhongLafortune(ambient, diffuse, specular, densityFunction);
      }
      break;
    }

    final Material material = new Material(shader, recursion);
    map.put(id, material);

    return material;

  }

  private Color parseColor(final String c) {

    final StringTokenizer st = new StringTokenizer(c);
    float r = Float.parseFloat(st.nextToken());
    float g = Float.parseFloat(st.nextToken());
    float b = Float.parseFloat(st.nextToken());

    r = r > 1.0f ? r / 255f : r;
    g = g > 1.0f ? g / 255f : g;
    b = b > 1.0f ? b / 255f : b;

    return new Color(r, g, b);
  }

  private SpectrumRGB parseSpectrum(final String c) {

    final StringTokenizer st = new StringTokenizer(c);
    final float r = Float.parseFloat(st.nextToken());
    final float g = Float.parseFloat(st.nextToken());
    final float b = Float.parseFloat(st.nextToken());

    return new SpectrumRGB(r, g, b);
  }

  private FilterRGB parseFilter(final String c) {

    final StringTokenizer st = new StringTokenizer(c);
    final float r = Float.parseFloat(st.nextToken());
    final float g = Float.parseFloat(st.nextToken());
    final float b = Float.parseFloat(st.nextToken());

    return new FilterRGB(r, g, b);
  }

  private Vector3D parseVector3D(final String v) {

    final StringTokenizer st = new StringTokenizer(v);
    final float x = Float.parseFloat(st.nextToken());
    final float y = Float.parseFloat(st.nextToken());
    final float z = Float.parseFloat(st.nextToken());

    return new Vector3D(x, y, z);
  }

  private Point3D parsePoint3D(final String p) {

    final StringTokenizer st = new StringTokenizer(p);
    final float x = Float.parseFloat(st.nextToken());
    final float y = Float.parseFloat(st.nextToken());
    final float z = Float.parseFloat(st.nextToken());

    return new Point3D(x, y, z);
  }

  private Point2f parsePoint2f(final String p) {

    final StringTokenizer st = new StringTokenizer(p);
    final float u = Float.parseFloat(st.nextToken());
    final float v = Float.parseFloat(st.nextToken());

    return new Point2f(u, v);
  }

  public Image getViewport() {
    return viewport;
  }

  public Camera getCamera() {
    camera.setProjection(projection);
    return camera;
  }

  public Group3D getScene() {
    return scene;
  }

  public LightGroup getLights() {
    return lights;
  }

}
