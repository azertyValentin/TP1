package modele;

// Classe gérant une ressource et son versionnage
// 2 attributs : - la ressource - la version de la ressource
public class Versionneur {

	private int version;
	private Object ressource;
	
	public Versionneur(Object d) {
		System.out.println("Initialisation d'un versionneur " + this + " : " + this.getClass());
		System.out.println("- Version initiale : 0");
		System.out.println("- Ressource : " + d + " : " + d.getClass());
		// Version de 0 au départ
		this.version = 0;
		this.ressource = d;
	}

	public Object getRessourceMutable(){
		return this.ressource;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void incrementerVersion(){
		this.version++;
	}

}



