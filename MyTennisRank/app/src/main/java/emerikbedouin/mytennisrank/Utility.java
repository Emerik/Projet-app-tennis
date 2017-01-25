package emerikbedouin.mytennisrank;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import emerikbedouin.mytennisrank.modele.Joueur;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;

/**
 * Created by emerikbedouin on 20/12/2016.
 */

public class Utility {

    private static final String PROFIL = "profil";
    private static final String NAME_PROFIL = "nom";
    private static final String NAME_JOUEUR = "nom";
    private static final String JOUEUR_PROFIL = "joueur";
    private static final String CLASSEMENT_PROFIL = "classement";
    private static final String CLASSEMENT_FUTUR_PROFIL = "classement_futur";
    private static final String MATCHS_PROFIL = "matchs";
    private static final String SCORE_PROFIL = "score";
    private static final String SURFACE_PROFIL = "surface";
    private static final String RESULTAT_PROFIL = "resultat";
    private static final String BONUS_PROFIL = "bonus";
    private static final String WO_PROFIL = "wo";

    /**
     * Transforme le JSON en objet Profil
     * @param json
     * @return
     */
    public static Profil getProfilFromJSON(String json){

        if (json != null) {
            Profil profil = new Profil();

            try {

                JSONObject jsonObject = new JSONObject(json);

                JSONObject profilJSON = jsonObject.getJSONObject(PROFIL);
                profil.setNom(profilJSON.getString(NAME_PROFIL));

                // Joueur
                JSONObject joueurJSON = profilJSON.getJSONObject(JOUEUR_PROFIL);
                Joueur j = new Joueur(0,
                        joueurJSON.getString(NAME_JOUEUR),
                        joueurJSON.getInt(CLASSEMENT_PROFIL),
                        joueurJSON.getInt(CLASSEMENT_FUTUR_PROFIL),
                        0);
                profil.setJoueurProfil(j);

                // Matchs
                JSONArray matchsJSON = profilJSON.getJSONArray(MATCHS_PROFIL);
                Match temp;
                Joueur j1, j2;
                for (int i = 0 ; i < matchsJSON.length() ; i++){
                    j1 = new Joueur(0,
                            matchsJSON.getJSONObject(i).getJSONObject("j1").getString(NAME_JOUEUR),
                            matchsJSON.getJSONObject(i).getJSONObject("j1").getInt(CLASSEMENT_PROFIL),
                            matchsJSON.getJSONObject(i).getJSONObject("j1").getInt(CLASSEMENT_FUTUR_PROFIL),
                            0);

                    j2 = new Joueur(0,
                            matchsJSON.getJSONObject(i).getJSONObject("j2").getString(NAME_JOUEUR),
                            matchsJSON.getJSONObject(i).getJSONObject("j2").getInt(CLASSEMENT_PROFIL),
                            matchsJSON.getJSONObject(i).getJSONObject("j2").getInt(CLASSEMENT_FUTUR_PROFIL),
                            0);

                    temp = new Match(j1, j2,
                            matchsJSON.getJSONObject(i).getString(SCORE_PROFIL),
                            matchsJSON.getJSONObject(i).getString(SURFACE_PROFIL),
                            matchsJSON.getJSONObject(i).getInt(RESULTAT_PROFIL),
                            null,//TODO Epreuve
                            matchsJSON.getJSONObject(i).getInt(BONUS_PROFIL),
                            matchsJSON.getJSONObject(i).getInt(WO_PROFIL));

                    profil.addMatch(temp);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

            return profil;
        }
        else{
            return null;
        }
    }

    /**
     * Transforme un objet Profil au format JSON
     * @param profil
     * @return
     */
    public static String getJSONfromProfil(Profil profil){
        String json= "{";

        //Profil
        json += "\""+PROFIL+"\": {";

        // Nom
        json += "\""+ NAME_PROFIL +"\": \""+profil.getNom()+"\", ";

        // Joueur
        json += "\""+JOUEUR_PROFIL+"\": {"+
                "\""+ NAME_JOUEUR +"\":\""+profil.getJoueurProfil().getNom()+"\", " +
                "\""+CLASSEMENT_PROFIL+"\":\""+profil.getJoueurProfil().getClassement()+"\", " +
                "\""+CLASSEMENT_FUTUR_PROFIL+"\":\""+profil.getJoueurProfil().getFuturClassement()+"\" ";
        json += "}, ";

        //Matchs
        json += "\""+MATCHS_PROFIL+"\":[";

        Match tempM;
        for (int i = 0 ; i < profil.getMatchs().size() ; i++){
            tempM = profil.getMatchs().get(i);
            json += "{ "+
                    "\"j1\":{"+
                    "\""+ NAME_JOUEUR +"\":\""+tempM.getJ1().getNom()+"\", " +
                    "\""+CLASSEMENT_PROFIL+"\":\""+tempM.getJ1().getClassement()+"\", " +
                    "\""+CLASSEMENT_FUTUR_PROFIL+"\":\""+tempM.getJ1().getFuturClassement()+"\" " +
                    "}, " +
                    "\"j2\":{"+
                    "\""+ NAME_JOUEUR +"\":\""+tempM.getJ2().getNom()+"\", " +
                    "\""+CLASSEMENT_PROFIL+"\":\""+tempM.getJ2().getClassement()+"\", " +
                    "\""+CLASSEMENT_FUTUR_PROFIL+"\":\""+tempM.getJ2().getFuturClassement()+"\" " +
                    "}, " +
                    "\""+SCORE_PROFIL+"\":\""+tempM.getScore()+"\", " +
                    "\""+RESULTAT_PROFIL+"\":\""+tempM.getResultat()+"\", " +
                    "\""+SURFACE_PROFIL+"\":\""+tempM.getSurface()+"\", " +
                    "\""+BONUS_PROFIL+"\":\""+tempM.getBonusChpt()+"\", " +
                    "\""+WO_PROFIL+"\":\""+tempM.getWo()+"\"" +
                    "}";

            if ( i+1 < profil.getMatchs().size() ) json += ", ";
        }

        json += "] ";

        // End of profil
        json += "}";

        json += "}";

        return json;
    }


}
