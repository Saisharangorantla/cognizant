package org.cognizant;

import org.slf4j.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger logger =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        displayCountry();
    }

	private static void displayCountry() {
		// TODO Auto-generated method stub
		ApplicationContext ctx=new ClassPathXmlApplicationContext("country.xml");
		
		Country country=(Country)ctx.getBean("country", Country.class);
		
		 logger.debug("Country : {}", country);
		
	}

}
