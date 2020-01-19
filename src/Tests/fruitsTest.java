
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.NodeData;
import elements.fruits;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class fruitsTest {
    Point3D p = new Point3D(0,0);
    fruits f = new fruits(12.5, 1, p);
    Point3D p1 = new Point3D(1,1);
    fruits f1 = new fruits(7, -1, p1);
    DGraph g = new DGraph();
    EdgeData e = new EdgeData(2,3,5);
    NodeData n = new NodeData(2, p);



    @Test
    void getValue() {
        assertEquals(f.getValue(), 12.5);
    }

    @Test
    void getType() {
        assertEquals(f.getType(), 1);
    }

    @Test
    void getPos() {
        assertEquals(f.getPos(), p);
    }

}