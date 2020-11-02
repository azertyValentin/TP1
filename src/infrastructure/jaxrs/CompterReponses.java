package infrastructure.jaxrs;

import java.util.concurrent.atomic.AtomicInteger;
import infrastructure.jaxrs.annotations.StatRequetes;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
@StatRequetes
@Priority(Priorities.AUTHORIZATION)
public class CompterReponses implements ContainerResponseFilter {
	
	// compteur permettant de compter le nombre de réponse envoyée au client
	private AtomicInteger out = new AtomicInteger(0);
	
	// compteur permettant de compter le nombre de réponse 200 suite à un get envoyée au client
	private AtomicInteger repOK200GET = new AtomicInteger(0);
	// compteur permettant de compter le nombre de réponse 200 suite à un put envoyée au client
	private AtomicInteger rep0K200PUT = new AtomicInteger(0);
	// compteur permettant de compter le nombre de réponse 304 envoyée au client
	private AtomicInteger repNotModified304 = new AtomicInteger(0);
	// compteur permettant de compter le nombre de réponse 412 envoyée au client
	private AtomicInteger repPreconditionFailed412 = new AtomicInteger(0);
	// compteur permettant de compter le nombre de réponse 428 suite à un get envoyée au client
	private AtomicInteger repPreconditionRequired428GET = new AtomicInteger(0);
	// compteur permettant de compter le nombre de réponse 428 suite à un put envoyée au client
	private AtomicInteger repPreconditionRequired428PUT = new AtomicInteger(0);

	public CompterReponses() {
		System.out.println("* Initialisation du filtre " + this + " : " + this.getClass());
	}

	@Override
	public void filter(ContainerRequestContext requete,
			ContainerResponseContext reponse) throws IOException {
		// Incrementation du nombre de réponse envoyée au client
		out.incrementAndGet();
		int statut = reponse.getStatus();
		// Précondition: status code de la reponse = OK
		if (statut == Response.Status.OK.getStatusCode()) {
			// Précondition: requete de type GET
			if(requete.getMethod().equalsIgnoreCase("GET")){
				repOK200GET.incrementAndGet();				
			}
			// Précondition: requete de type PUT
			if(requete.getMethod().equalsIgnoreCase("PUT")){
				rep0K200PUT.incrementAndGet();
			}
		}
		// Précondition: status code de la reponse = NOT MODIFIED
		if (statut == Response.Status.NOT_MODIFIED.getStatusCode()) {
			repNotModified304.incrementAndGet();
		}
		// Précondition: status code de la reponse = PRECONDITION FAILED
		if (statut == Response.Status.PRECONDITION_FAILED.getStatusCode()) {
			repPreconditionFailed412.incrementAndGet();
		}
		
		// Précondition: status code de la reponse = PRECONDITION REQUIRED
		if (statut == StatutRFC6585.PRECONDITION_REQUIRED.getCodeStatut()) {
			// Précondition: requete de type GET
			if(requete.getMethod().equalsIgnoreCase("GET")){
				repPreconditionRequired428GET.incrementAndGet();
			}
			// Précondition: requete de type PUT
			if(requete.getMethod().equalsIgnoreCase("PUT")){
				repPreconditionRequired428PUT.incrementAndGet();
			}
		}
		
		System.out.println(this + " - Réponses - total : " + out.get()
				+ " dont 200 (GET) : " + repOK200GET.get() 
				+ " , 20O (PUT) : " + rep0K200PUT.get()
				+ " , 304 (GET) : " + repNotModified304.get() 
				+ " , 412 (PUT) : " + repPreconditionFailed412.get()
				+ " , 428 (GET) : " + repPreconditionRequired428GET.get()
				+ " , 428 (PUT) : " + repPreconditionRequired428PUT.get());
	}

}
