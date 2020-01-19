import elements.robot;
import org.junit.jupiter.api.Test;
import utils.Point3D;

import static org.junit.jupiter.api.Assertions.*;

class robotTest {
    Point3D p = new Point3D(1,2);
    robot r = new robot(1,2,3,4,5,p);


    @Test
    void getSrc() {
        assertEquals(r.getSrc(), 2);
    }

    @Test
    void setPos() {
        Point3D pd = new Point3D(0,0);
        r.setPos(pd);
        assertEquals(r.getPos(), pd);
    }

    @Test
    void setSrc() {
        int src = 5;
        r.setSrc(src);
        assertEquals(r.getSrc(), 5);
    }

    @Test
    void setDest() {
        int dest = 6;
        r.setDest(dest);
        assertEquals(r.getDest(), 6);
    }

    @Test
    void getId() {
        assertEquals(r.getId(), 1);
    }

    @Test
    void getDest() {
        assertEquals(r.getDest(), 3);
    }

    @Test
    void getPos() {
        assertEquals(r.getPos().toString(), p.toString());
    }

}