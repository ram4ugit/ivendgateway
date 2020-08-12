package com.limitlessmobility.iVendGateway.services.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpringMVCController {

	@RequestMapping(value = "/downloadXLS")
	public void downloadXLS(HttpServletResponse response) {

		try {
			response.setContentType("application/vnd.ms-excel");
			String reportName = "JavaHonk_XLS_Report_.xls";
			response.setHeader("Content-disposition", "attachment; filename=" + reportName);
			ArrayList<String> rows = new ArrayList<String>();
			rows.add("First Name");
			rows.add("\t");
			rows.add("Last name");
			rows.add("\t");
			rows.add("Test");
			rows.add("\n");

			for (int i = 0; i < 5; i++) {
				rows.add("Java");
				rows.add("\t");
				rows.add("Honk");
				rows.add("\t");
				rows.add("Success");
				rows.add("\n");
			}
			Iterator<String> iter = rows.iterator();
			while (iter.hasNext()) {
				String outputString = (String) iter.next();
				response.getOutputStream().print(outputString);
			}
			
			response.getOutputStream().flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@RequestMapping("/pdf/{fileName:.+}")
    public void downloadPDFResource( HttpServletRequest request, 
                                     HttpServletResponse response, 
                                     @PathVariable("fileName") String fileName) 
    {
        //If user is not authorized - he should be thrown out from here itself
         
        //Authorized user will download the file
        String dataDirectory = request.getServletContext().getRealPath("D:/downloads/pdf");
        Path file = Paths.get(dataDirectory, fileName);
        if (Files.exists(file)) 
        {
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "attachment; filename="+fileName);
            try
            {
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            } 
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
