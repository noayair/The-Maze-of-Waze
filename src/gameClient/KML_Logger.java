package gameClient;

import java.io.*;
import java.time.LocalDateTime;

public class KML_Logger {

    private StringBuilder string;
    private int level_num;


    public  KML_Logger(){
        string = new StringBuilder();
        startKML();
    }

    public String getString() {
        return string.toString();
    }

    public void startKML()
    {
        string.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" );
        string.append(               "<kml xmlns=\"http://earth.google.com/kml/2.2\">\r\n" );
        string.append(     "  <Document>\r\n" );
        string.append("  <name>" + "Game stage :" + this.level_num + "</name>" +"\r\n");
        string.append(" <Style id=\"node\">\r\n");
        string.append(   "      <IconStyle>\r\n" );
        string.append(    "        <Icon>\r\n" );
        string.append(    "          <href>http://maps.google.com/mapfiles/kml/pal3/icon35.png</href>\r\n");
        string.append(    "        </Icon>\r\n" );
        string.append( "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n");
        string.append(  "      </IconStyle>\r\n");
        string.append( "    </Style>" );
        string.append(     " <Style id=\"banana\">\r\n" );
        string.append(    "      <IconStyle>\r\n" );
        string.append(    "        <Icon>\r\n" );
        string.append(    "          <href>http://maps.google.com/mapfiles/kml/pal5/icon49.png</href>\r\n" );
        string.append("        </Icon>\r\n" );
        string.append( "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" );
        string.append(  "      </IconStyle>\r\n" );
        string.append(  "    </Style>" );
        string.append(" <Style id=\"apple\">\r\n" );
        string.append( "      <IconStyle>\r\n" );
        string.append( "        <Icon>\r\n" );
        string.append( "          <href>http://maps.google.com/mapfiles/kml/pal5/icon56.png</href>\r\n" );
        string.append( "        </Icon>\r\n" );
        string.append(  "        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" );
        string.append("      </IconStyle>\r\n" );
        string.append( "    </Style>" );
        string.append(" <Style id=\"robot\">\r\n" );
        string.append( "      <IconStyle>\r\n" );
        string.append("        <Icon>\r\n" );
        string.append( "          <href>http://maps.google.com/mapfiles/kml/pal4/icon26.png></href>\r\n" );
        string.append( "        </Icon>\r\n" );
        string.append("        <hotSpot x=\"32\" y=\"1\" xunits=\"pixels\" yunits=\"pixels\"/>\r\n" );
        string.append( "      </IconStyle>\r\n");
        string.append(  "    </Style>\r\n");
    }



    public void addPlaceMark(String id, String position)
    {
        LocalDateTime time = LocalDateTime.now();
        string.append("    <Placemark>\r\n" );
        string.append("       <open>1<open>\r\n");
//        string.append("         <description>Mac:\r\n");
//        string.append("             </description>\r\n");
        string.append("       <TimeSpan>\r\n" );
        string.append("        <when>" + time+ "</when>\r\n" );
        string.append("       </TimeStamp>\r\n" );
        string.append("       <Style>");
        string.append("            <IconStyle>");
        string.append("                  <color>ff007db3</color>");
        string.append("                  <scale>1.0</scale>");
        string.append("                  <heading>1.0</heading>");
        string.append("                  <Icon>");
        string.append("                       <href>" + id + "</href>");
        string.append("                       <refreshInterval>1.0</refreshInterval>");
        string.append("                       <viewRefreshTime>1.0</viewRefreshTime");
        string.append("                       <viewBoundScale>1.0</viewBoundScale>");
        string.append("                  </Icon>");
        string.append("             </IconStyle>");
        string.append("        </Style>");
//        string.append(       "      <styleUrl>#" + id + "</styleUrl>\r\n" );
        string.append("        <Point>\r\n" );
        string.append("            <coordinates>" + position + "</coordinates>\r\n" );
        string.append("        </Point>\r\n" );
        string.append("   </Placemark>\r\n");
    }

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