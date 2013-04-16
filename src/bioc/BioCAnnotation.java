package bioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.Relation;

/**
 * Stand off annotation. The connection to the original text can be made
 * through the {@code offset}, {@code length}, and possibly the {@code text}
 * fields.
 */
public class BioCAnnotation {

  /**
   * Id used to identify this annotation in a {@link Relation}.
   */
  protected String              id;
  protected Map<String, String> infons;
  protected List<BioCLocation>  locations;

  /**
   * Unless something else is defined this should be the annotated text. The
   * length is redundant in this case. Other uses for this field would be the
   * ontology id for the specific disease when the type was "disease."
   */
  protected String              text;

  public BioCAnnotation() {
    id = "";
    infons = new HashMap<String, String>();
    locations = new ArrayList<BioCLocation>();
    text = "";
  }

  public BioCAnnotation(BioCAnnotation annotation) {
    id = annotation.id;
    infons = new HashMap<String, String>(annotation.infons);
    locations = new ArrayList<BioCLocation>(annotation.locations);
    text = annotation.text;
  }

  public void addLocation(BioCLocation location) {
    locations.add(location);
  }

  public String getID() {
    return id;
  }

  public String getInfon(String key) {
    return infons.get(key);
  }

  public Map<String, String> getInfons() {
    return infons;
  }

  public List<BioCLocation> getLocations() {
    return locations;
  }

  public String getText() {
    return text;
  }

  public void putInfon(String key, String value) {
    infons.put(key, value);
  }

  public void setID(String id) {
    this.id = id;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    String s = "id: " + id;
    s += "\n";
    s += infons;
    s += "locations: " + locations;
    s += "\n";
    s += "text: " + text;
    s += "\n";
    return s;
  }
}
