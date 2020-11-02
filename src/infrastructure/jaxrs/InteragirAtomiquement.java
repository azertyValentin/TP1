package infrastructure.jaxrs;

import java.util.concurrent.atomic.AtomicInteger;

import infrastructure.jaxrs.annotations.AtomiciteRequeteReponseServeur;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;

@Provider
@AtomiciteRequeteReponseServeur
@Priority(Priorities.AUTHORIZATION + 10)
public class InteragirAtomiquement implements ContainerRequestFilter,
		ContainerResponseFilter {

	private ReadWriteLock verrou;
	// compteur permettant de compter le nombre de lecteurs
	private AtomicInteger nombreLecteurs = new AtomicInteger(0);
	// compteur permettant de compter le nombre souhaitant modifier la ressource
	private AtomicInteger nombreScribes = new AtomicInteger(0);

	public InteragirAtomiquement() {
		verrou = new ReentrantReadWriteLock();
		System.out.println("* Initialisation du filtre " + this + " : " + this.getClass());
	}
	
	// Filter utilisé lorsque l'on repond au client
	@Override
	public void filter(ContainerRequestContext requete,
			ContainerResponseContext reponse) throws IOException {
		// Précondition: requete de type PUT
		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			// On décrémente le nombre de scribes et on débloque l'accès en écriture
			nombreScribes.decrementAndGet();
			verrou.writeLock().unlock();
			return;
		}
		// Précondition: requete de type GET
		if (requete.getMethod().equalsIgnoreCase("GET")) {
				// On décrémente le nombre de lecteurs et on débloque l'accès en lecture
				nombreLecteurs.decrementAndGet();
				verrou.readLock().unlock();
				return;
		}
	}

	// Filter utilisé lorsque l'on recoit la requete du client
	@Override
	public void filter(ContainerRequestContext requete) throws IOException {
		// Précondition: requete de type PUT
		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			// On incrémente le nombre de scribes et on bloque l'accès en écriture
			verrou.writeLock().lock();
			nombreScribes.incrementAndGet();
			System.out.println("Scribes : " + nombreScribes.get());
			return;
		}
		// Précondition: requete de type GET
		if (requete.getMethod().equalsIgnoreCase("GET")) {
				// On incrémente le nombre de lecteurs et on bloque l'accès en lecture
				verrou.readLock().lock();
				nombreLecteurs.incrementAndGet();
				System.out.println("Lecteurs : " + nombreLecteurs.get());
			return;
		}
	}

}
