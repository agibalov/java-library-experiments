package conf;

import me.loki2302.controllers.ApiController;
import me.loki2302.controllers.HomeController;
import ninja.AssetsController;
import ninja.Router;
import ninja.application.ApplicationRoutes;

public class Routes implements ApplicationRoutes {
	@Override
	public void init(Router router) {
		router.GET().route("/").with(HomeController.class, "index");
		router.GET().route("/angular").with(HomeController.class, "angular");
		router.GET().route("/robots.txt").with(HomeController.class, "robots");
		
		router.GET().route("/api/addNumbersGet").with(ApiController.class, "addNumbersGet");
		router.POST().route("/api/addNumbersPost").with(ApiController.class, "addNumbersPost");
		
		router.GET().route("/assets/.*").with(AssetsController.class, "serve");
	}		
}