import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/api")
public class EmentoController {

    @Get("/ping")
    @Produces(MediaType.TEXT_JSON)
    public String ping() {
        return "{status:OK}";
    }
}