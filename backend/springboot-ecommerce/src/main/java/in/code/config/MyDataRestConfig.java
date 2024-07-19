package in.code.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import in.code.entity.Country;
import in.code.entity.Order;
import in.code.entity.Product;
import in.code.entity.ProductCategory;
import in.code.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

	private EntityManager entityManager;

	@Value("${allowed.origins}")
	private String[] theAllowedOrigins;

	@Autowired
	public MyDataRestConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {

		HttpMethod[] theUnsupportedActions = { HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PATCH };

		// Disable the HTTP methods for product : PUT,POST,DELETE
		disableHttpMethods(Product.class, config, theUnsupportedActions);

		// Disable the HTTP methods for productCategory : PUT,POST,DELETE
		disableHttpMethods(ProductCategory.class, config, theUnsupportedActions);
		disableHttpMethods(Country.class, config, theUnsupportedActions);
		disableHttpMethods(State.class, config, theUnsupportedActions);
		disableHttpMethods(Order.class, config, theUnsupportedActions);

		// call internal helper method
		exposeIds(config);

		// configure CORS mapping
		cors.addMapping(config.getBasePath() + "/**").allowedOrigins(theAllowedOrigins);
	}

	private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config,
			HttpMethod[] theUnsupportedActions) {
		config.getExposureConfiguration().forDomainType(theClass)
				.withItemExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
				.withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
	}

	private void exposeIds(RepositoryRestConfiguration config) {

		// expose entity Id's
		// get list of all entity classes from entity manager
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

		// - create arraylist of entity types
		List<Class> entityClasses = new ArrayList<>();

		for (EntityType tempEntityTypes : entities) {
			entityClasses.add(tempEntityTypes.getJavaType());
		}

		// expose the entity ids for the array of entity/domain types
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);

	}

}
