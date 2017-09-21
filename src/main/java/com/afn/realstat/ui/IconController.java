package com.afn.realstat.ui;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.afn.realstat.util.Icon;

@Controller
// @RequestMapping(value = "/icon")
public class IconController {
	
	@RequestMapping(value = "/icon/{iconId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody
        // byte[] getIcon(@PathVariable String id) {
		byte[] getIcon(@PathVariable int iconId, @RequestParam(required=false) String text) {
        byte[] b;
        Icon icon = new Icon(iconId, text);
        b =icon.getIcon();
        return b;
    } 


}


/*
public String getIcon(ModelMap model) {
	model.addAttribute("message", "Hello Spring MVC Framework!");

	/*
	 * String url = req.getRequestURI(); System.out.println(url);
	 * resp.getWriter().println("Hello, world");
	 */
/*
	return "star32.png";
}
*/
