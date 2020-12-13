import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

class TestJsonReader {

    JsonReader jsReader1, jsReader2, jsReader3;
    JSONObject jsobj1, jsobj2;
    JSONArray jsarray1;
    ArrayList<String> clesAttendus1, clesAttendusTest1, clesAttendus2, clesAttendusTest2;



    @BeforeEach
    void setUp() {
        jsReader1 = new JsonReader();
        jsReader2 = new JsonReader();
        jsReader3 = new JsonReader("fileName");

        jsobj1 = new JSONObject();
        jsobj1.accumulate("cle1","donnee1");
        jsobj1.accumulate("cle1a","donnee1a");
        jsobj1.accumulate("cle1b","donnee1b");
        jsReader1.setJsObj(jsobj1);

        jsarray1 = new JSONArray();
        jsarray1.add(jsobj1);
        jsarray1.add(jsobj1);

        clesAttendus1 = new ArrayList<>(Arrays.asList("cle1","cle1a","cle1b"));

        jsobj2 = new JSONObject();
        jsobj2.accumulate("cleA",jsobj1);
        jsobj2.accumulate("cleB", jsarray1);
        jsReader2.setJsObj(jsobj2);

        clesAttendus2 = new ArrayList<>(Arrays.asList("cleA","cleB"));
    }

    @Test
    void JsonReader(){
        assertTrue(jsReader3 instanceof JsonReader);
    }

    @Test
    void load(){
        Throwable e = null;
        try {
            jsReader3.load();
        } catch (Throwable ex) {
            e = ex;
        }
        assertTrue(e instanceof IOException);
    }


    @Test
    void getJsObjValue() {
        assertEquals("donnee1a", jsReader1.getJsObjValue("cle1a"));
    }

    @Test
    void getJsObjArray() {
        assertTrue(jsarray1.equals(jsReader2.getJsObjArray("cleB")));
    }

    @Test
    void getJsObjArray1() {
        Throwable e = null;
        try {
            jsReader1.getJsObjArray("cle1");
        } catch (Throwable ex) {
            e = ex;
        }
        assertTrue(e instanceof JSONException);
    }



    @Test
    void getListJsObjValue() {
        assertEquals("donnee1a",jsReader2.getListJsObjValue("cleB","cle1a").get(0) );
    }

    @Test
    void getJsObj() {
        assertTrue(jsobj2.equals(jsReader2.getJsObj()));
    }

}