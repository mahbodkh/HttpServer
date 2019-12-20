package app.example;

import app.core.anotation.Controller;
import app.core.anotation.InitParameter;
import app.core.http.enums.MethodTypes;

/**
 * Created by Mahbod with ❤️ on 20 December 2019.
 */

@Controller(method = MethodTypes.POST, urlPatten = "/post", initParameters = {
        @InitParameter(key = "name", value = "POST_Ebrahim"),
        @InitParameter(key = "family", value = "POST_Kh")
})
public class TestPOSTController {

}
