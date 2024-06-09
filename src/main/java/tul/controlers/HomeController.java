package tul.controlers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root REST API controller.
 */
@RestController
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    String showHome() {
        return "home";
    }
}
