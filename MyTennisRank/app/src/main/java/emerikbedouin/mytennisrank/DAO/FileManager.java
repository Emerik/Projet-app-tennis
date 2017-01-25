package emerikbedouin.mytennisrank.dao;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import emerikbedouin.mytennisrank.Utility;
import emerikbedouin.mytennisrank.modele.Joueur;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 25/03/16.
 */


public class FileManager {


    private static final String LOG_TAG = "FILEMANAGER";


    /**
     * Enregistrement d'un profil sur la mémoire interne
     * @param context
     * @param profil
     * @return
     */
    @Deprecated
    public static boolean saveProfil(Context context, Profil profil, String fileName){

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            System.out.println("Emplacement : " + context.getFileStreamPath(fileName));
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            System.out.println("Emplacement : "+fileName+" : " + context.getFileStreamPath(fileName));
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

    @Deprecated
    public static Profil loadProfil(Context context, String fileName){

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        Profil profil = null;
        try
        {
            fis = context.openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            profil = (Profil) ois.readObject();
            System.out.println("Emplacement : " + fileName + " : " + context.getFileStreamPath(fileName));

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

    /**
     * Enregistrement d'un profil sur la mémoire interne au format JSON
     * @param context
     * @param profil
     * @return
     */
    public static boolean saveProfilJSON(Context context, Profil profil, String fileName){

        String data = Utility.getJSONfromProfil(profil);

        File file = new File(context.getFilesDir(), fileName);

        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Chargement d'un profil mémoire stocké au format JSON
     * @param context
     * @param fileName
     * @return
     */
    public static Profil loadProfilJSON(Context context, String fileName){

        String json = null;

        File file = new File(context.getFilesDir(), fileName);

        FileInputStream inputStream;

        try{
            inputStream = context.openFileInput(fileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            json = r.readLine();

            r.close();
            inputStream.close();

            return Utility.getProfilFromJSON(json);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }


}
