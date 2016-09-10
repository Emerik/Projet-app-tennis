package emerikbedouin.mytennisrank.dao;

import emerikbedouin.mytennisrank.modele.Profil;

/**
*	Classe d'acc�s unique au profil actuel
**/
public class ProfilSingleton{

	private static ProfilSingleton instance = null;
	private Profil leProfil ;
	
	private ProfilSingleton(){
		leProfil = null;
	}
	
	/**
	* Retourne l'instance unique de ProfilSingleton
	*/
	public static ProfilSingleton getInstance(){
		if(instance == null){
			return instance = new ProfilSingleton();
		}
		else{
			return instance;
		}

	}
	
	/**
	* Initie le profil a la valeur passe en parametre
	* @param profil le nouveau profil a attribuer
	*/
	public void setProfil(Profil profil){
		leProfil = profil;
	}

	/**
	* Renvoi le profil de l'instance
	* @return le profil de l'instance
	*/
	public Profil getProfil(){
		return leProfil;
	}

}