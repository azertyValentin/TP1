package infrastructure.jaxrs;

import java.util.concurrent.atomic.AtomicInteger;
import infrastructure.jaxrs.annotations.StatRequetes;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
@StatRequetes
@Priority(Priorities.AUTHORIZATION)
public class CompterRequetes implements ContainerRequestFilter {
	
	// compteur permettant de compter le nombre de requêtes get et put
	private AtomicInteger in = new AtomicInteger(0);
	
	// compteur permettant de compter le nombre de requêtes get
	private AtomicInteger reqGet = new AtomicInteger(0);
	// compteur permettant de compter le nombre de requêtes put
	private AtomicInteger reqPut = new AtomicInteger(0);

	public CompterRequetes() {
		System.out.println("* Initialisation du filtre " + this + " : " + this.getClass());
	}

	@Override
	public void filter(ContainerRequestContext requete) throws IOException {
		// Incrementation du nombre de requete PUT et GET
		in.incrementAndGet();
		// Précondition: requete de type PUT
		if (requete.getMethod().equalsIgnoreCase("PUT")) {
			// Incrementation du nombre de requete PUT
			reqPut.incrementAndGet();
		}
		// Précondition: requete de type GET
		if (requete.getMethod().equalsIgnoreCase("GET")) {
			// Incrementation du nombre de requete GET
			reqGet.incrementAndGet();
		}
		System.out.println(this + " - Requêtes - total : " + in.get()
				+ " dont PUT : " + reqPut.get() 
				+ " , GET : " + reqGet.get());
	}

}
