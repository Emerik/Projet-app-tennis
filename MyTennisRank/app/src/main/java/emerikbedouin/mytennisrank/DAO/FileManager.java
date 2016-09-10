package emerikbedouin.mytennisrank.dao;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import emerikbedouin.mytennisrank.modele.Profil;

/**
 * Created by emerikbedouin on 25/03/16.
 */


public class FileManager {


    /**
     * Enregistrement d'un profil sur la mémoire interne
     * @param activity
     * @param profil
     * @return
     */
    public static boolean saveProfil(Activity activity, Profil profil, String fileName){

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            System.out.println("Emplacement : " + activity.getFileStreamPath(fileName));
            fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
            System.out.println("Emplacement : "+fileName+" : " + activity.getFileStreamPath(fileName));
            oos = new ObjectOutputStream(fos);
            oos.writeObject(profil);


            if (oos != null)
                oos.close();
            if (fos != null)
                fos.close();

            return true;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Erreur FileNotFound "+e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Erreur IO"+e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteProfil(Activity activity, String fileName){
        boolean deleted = false;
        File file = activity.getFileStreamPath(fileName);

        if(file != null) deleted = file.delete();

        return deleted;
    }

    public static Profil loadProfil(Activity activity, String fileName){

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        Profil profil = null;
        try
        {
            fis = activity.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            profil = (Profil) ois.readObject();
            System.out.println("Emplacement : " + fileName + " : " + activity.getFileStreamPath(fileName));

            if (ois != null)
                ois.close();
            if (fis != null)
                fis.close();

            return profil;
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Erreur FileNotFound "+e.getMessage());
            e.printStackTrace();
        }
        catch (IOException e)
        {
            System.out.println("Erreur IO"+e.getMessage());
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            System.out.println("Erreur ClassFound "+e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
