import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class EcrireFichier {

    public static void saveStringIntoFile( String filePath, String content ) {
        try {
            File f = new File(filePath);
            FileUtils.writeStringToFile(f, content, "UTF-8");
        }
        catch(IOException e){
            System.err.println("Erreur! Impossible de sauvegarder le fichier!");
        }
    }
}
