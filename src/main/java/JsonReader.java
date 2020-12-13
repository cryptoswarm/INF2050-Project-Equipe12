import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JsonReader {
    private String fileName;
    private JSONObject jsObj;

    public JsonReader(){
    }

    public JsonReader( String fileName ) {

        this.fileName = fileName;
        this.jsObj = new JSONObject();
    }


    public void load() throws JSONException, IOException {

        try {
            String jsObjString = IOUtils.toString( new FileInputStream( this.fileName ), "UTF-8" );
            this.jsObj = ( JSONObject ) JSONSerializer.toJSON( jsObjString );
        } catch ( JSONException e ) {
            System.err.println( "Fichier JSON invalide!" );
        } catch ( IOException e ) {
            throw new IOException("Fichier n'existe pas ou Erreur IOException!");
        }
    }


    public String getJsObjValue( String jsObjKey ) {

        return this.jsObj.getString( jsObjKey );
    }


    public JSONArray getJsObjArray( String jsObjKey ) throws JSONException {

        JSONArray jsArray;
        try {
            jsArray = ( JSONArray ) JSONSerializer.toJSON( getJsObjValue( jsObjKey ) );
        } catch ( JSONException e ) {
            throw new JSONException( "Resultat n'est pas un Array!" );
        }
        return jsArray;
    }

    //
    // Obtenir une liste des valeurs en STRING des JSON objets dans un JSONArray
    //
    public ArrayList<String> getListJsObjValue( String jsArrayName, String jsObjKey ) {

        ArrayList<String> list = new ArrayList<>();
        JSONArray jsArray = getJsObjArray( jsArrayName );
        for ( int i = 0; i < jsArray.size(); i++ ) {
            list.add( jsArray.getJSONObject( i ).getString( jsObjKey ) );
        }
        return list;
    }

    public JSONObject getJsObj() {

        return this.jsObj;
    }

    public void setJsObj(JSONObject jsObj) {

        this.jsObj = jsObj;
    }

}
