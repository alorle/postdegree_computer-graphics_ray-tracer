package parser;


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

public final class Parser {

  private final Image viewport;
  private final Camera camera;
  private final Projection projection;
  private final Group3D scene;

  public Parser (final File in) throws ParserConfigurationException, SAXException, IOException  {

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
      this.scene = this.parseScene(rootElement);
    
    }
    else {
      throw new SAXException("Elemento <image> no encontrado");
    }

  }
  
  private Image parseViewport (final String tag, final Element doc) throws SAXException {
  
    if (doc.getElementsByTagName("viewport").getLength() > 0) {

      final Element el
              = (Element) doc.getElementsByTagName("viewport").item(0);
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

  private Camera parseCamera (final Element doc) throws SAXException {
    
    if (doc.getElementsByTagName("camera").getLength() > 0) {
      final Element el
              = (Element) doc.getElementsByTagName("camera").item(0);
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

  private Projection parseProjection (final Element doc) throws SAXException {
    
    if (doc.getElementsByTagName("projection").getLength() > 0) {
      final Element el
              = (Element) doc.getElementsByTagName("projection").item(0);

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

  private Group3D parseScene (final Element doc) throws SAXException {
    
    if (doc.getElementsByTagName("scene").getLength() > 0) {

      final Group3D g = new Group3D();

      final Element sceneElement
              = (Element) doc.getElementsByTagName("scene").item(0);
      final NodeList objects = sceneElement.getElementsByTagName("object");
      for (int j = 0; j < objects.getLength(); ++j) {
        final Element el = (Element) objects.item(j);
        g.addObject(this.parseObject(el));
      }

      return g;
      
    } else {
      throw new SAXException("Elemento <scene> no encontrado");
    }   
    
  }

  private Object3D parseObject (final Element el) {

    final String id = el.getAttribute("id");
    
    Color color = null;
    
    final NodeList colorList = el.getElementsByTagName("color");
    if (colorList.getLength() > 0) {
      final String colorElementText = colorList.item(0).getTextContent();
      color = parseColor(colorElementText);
    }
    
    final Object3D object;     
    switch (el.getAttribute("type")) {
      case "sphere": {
        final Point3D center = this.parsePoint3D(
                el.getElementsByTagName("center").item(0).getTextContent());
        final float radius = Float.parseFloat(
                el.getElementsByTagName("radius").item(0).getTextContent());

        object = new Sphere(center, radius, color);
        
      }
      break;

      case "plane": {
        final Point3D point = this.parsePoint3D(
                el.getElementsByTagName("point").item(0).getTextContent());
        final Vector3D normal = this.parseVector3D(
                el.getElementsByTagName("normal").item(0).getTextContent());
        
        object = new Plane(point, normal, color);
        
      }
      break;      

      case "triangle": {
        final Point3D vertex0 = this.parsePoint3D(
                el.getElementsByTagName("vertex0").item(0).getTextContent());
        final Point3D vertex1 = this.parsePoint3D(
                el.getElementsByTagName("vertex1").item(0).getTextContent());
        final Point3D vertex2 = this.parsePoint3D(
                el.getElementsByTagName("vertex2").item(0).getTextContent());

        object = new Triangle(vertex0, vertex1, vertex2, color);

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
        object = new Cylinder(center, axedirection, radius, L,  color);

      }
      break;        

      case "triangular": {
        
        final HashMap<Integer, Point3D> vertices = new HashMap<>();
        final Element verticesElement = (Element) el.getElementsByTagName("vertices").item(0);
        final NodeList vertexList = verticesElement.getElementsByTagName("vertex"); 
        for (int j = 0; j < vertexList.getLength(); ++j) {
          vertices.put(j + 1, this.parsePoint3D(vertexList.item(j).getTextContent()));
        }
       
        final List<String> facets = new ArrayList<>();
        final Element facetsElement = (Element) el.getElementsByTagName("facets").item(0);
        final NodeList facetList = facetsElement.getElementsByTagName("facet");        
        for (int j = 0; j < facetList.getLength(); ++j) {
          facets.add(facetList.item(j).getTextContent());
        }

        if (color != null) {
          object = new TriangularMesh(vertices, facets, color);
        } else {
          object = new TriangularMesh(vertices, facets);
        }

      }
      break;        
      
      default: {
        object = null;
      }
      
    }

    return object;
  }

  private Color parseColor (final String c) {
    final StringTokenizer st = new StringTokenizer(c);
    
    float r = Float.parseFloat(st.nextToken());
    float g = Float.parseFloat(st.nextToken());
    float b = Float.parseFloat(st.nextToken());

    r = r > 1f ? r / 255f : r;
    g = g > 1f ? g / 255f : g;
    b = b > 1f ? b / 255f : b;

    return new Color(r, g, b);
  }

  private Vector3D parseVector3D (final String v) {
    final StringTokenizer st = new StringTokenizer(v);
    final float x = Float.parseFloat(st.nextToken());
    final float y = Float.parseFloat(st.nextToken());
    final float z = Float.parseFloat(st.nextToken());

    return new Vector3D(x, y, z);
  }

  private Point3D parsePoint3D (final String p) {
    final StringTokenizer st = new StringTokenizer(p);
    final float x = Float.parseFloat(st.nextToken());
    final float y = Float.parseFloat(st.nextToken());
    final float z = Float.parseFloat(st.nextToken());

    return new Point3D(x, y, z);
  }
  
  private Point2f parsePoint2f (final String p) {
    final StringTokenizer st = new StringTokenizer(p);
    final float u = Float.parseFloat(st.nextToken());
    final float v = Float.parseFloat(st.nextToken());

    return new Point2f(u, v);
  }  
  
  public Image getViewport () {
    return viewport;
  }
  
  public Camera getCamera () {
    camera.setProjection(projection);
    return camera;
  }
  
  public Group3D getScene () {
    return scene;
  }
  
}