package gameClient;

import java.io.*;
import java.time.LocalDateTime;

public class KML_Logger {

    private StringBuilder string;
    private int level_num;


    public KML_Logger() {
        string = new StringBuilder();
        startKML();
    }

    public String getString() {
        return string.toString();
    }

    /**
     * The function brings up the specific KML file start format.
     */
    public void startKML() {
        string.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        string.append("<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n");
        string.append("  <Document>\r\n");
        string.append("  <name>" + "Game stage :" + this.level_num + "</name>" + "\r\n");
        string.append(" <Style id=\"node\">\r\n");
        string.append("      <IconStyle>\r\n");
        string.append("        <Icon>\r\n");
        string.append("          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n");
        string.append("        </Icon>\r\n");
        string.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        string.append("      </IconStyle>\r\n");
        string.append("    </Style>");
        string.append(" <Style id=\"banana\">\r\n");
        string.append("      <IconStyle>\r\n");
        string.append("        <Icon>\r\n");
        string.append("          <href>http://maps.google.com/mapfiles/kml/pal5/icon49.png</href>\r\n");
        string.append("        </Icon>\r\n");
        string.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        string.append("      </IconStyle>\r\n");
        string.append("    </Style>");
        string.append(" <Style id=\"apple\">\r\n");
        string.append("      <IconStyle>\r\n");
        string.append("        <Icon>\r\n");
        string.append("          <href>http://maps.google.com/mapfiles/kml/pal5/icon56.png</href>\r\n");
        string.append("        </Icon>\r\n");
        string.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        string.append("      </IconStyle>\r\n");
        string.append("    </Style>");
        string.append(" <Style id=\"robot\">\r\n");
        string.append("      <IconStyle>\r\n");
        string.append("        <Icon>\r\n");
        string.append("          <href>http://maps.google.com/mapfiles/kml/pal4/icon26.png></href>\r\n");
        string.append("        </Icon>\r\n");
        string.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        string.append("      </IconStyle>\r\n");
        string.append("    </Style>\r\n");
    }

    /**
     * The function gets an update from fruits and Robots
     * and to anyone writes the created time and its location
     * and with that you can see it on the map
     *
     * @param id
     * @param position
     */
    public void addPlaceMark(String id, String position) {
        LocalDateTime time = LocalDateTime.now();
        string.append("        <Placemark>\n" +
                "            <open>1</open>\n" +
                "            <description>Mac: \n" +
                "           </description>\n" +
                "            <TimeSpan>\n" +
                "                <begin>" + time + "</begin>\n" +
                "                <end>" + time + "</end>\n" +
                "            </TimeSpan>\n" +
                "            <Style>\n" +
                "                <IconStyle>\n" +
                "                    <color>ff007db3</color>\n" +
                "                    <scale>1.0</scale>\n" +
                "                    <heading>1.0</heading>\n" +
                "                    <Icon>\n" +
                "                        <href>" + id + "</href>\n" +
                "                        <refreshInterval>1.0</refreshInterval>\n" +
                "                        <viewRefreshTime>1.0</viewRefreshTime>\n" +
                "                        <viewBoundScale>1.0</viewBoundScale>\n" +
                "                    </Icon>\n" +
                "                </IconStyle>\n" +
                "            </Style>\n" +
                "            <Point>\n" +
                "                <coordinates>" + position + "</coordinates>\n" +
                "            </Point>\n" +
                "        </Placemark>");
    }

    /**
     * The function brings up the specific KML file end format.
     *
     * @throws IOException
     */
    public void endKML() throws IOException {
        string.append("  </Document>\r\n");
        string.append("</kml>");
        try {
            PrintWriter kml = new PrintWriter(new File(level_num + ".kml"));
            kml.write(string.toString());
            kml.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}