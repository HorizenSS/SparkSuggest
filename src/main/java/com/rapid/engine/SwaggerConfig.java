package com.rapid.engine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact(
            "Ines Akez",
            "https://www.linkedin.com/in/ines-akez-a69996110/", 
            "akez.ines@gmail.com"
    );
    
    List<VendorExtension> vendorExtensions = new ArrayList<>();
	
	ApiInfo apiInfo = new ApiInfo(
			"Autocomplete Engine RESTFUL Web Service documentation",

					"To use the client side, start by feeding suggestions to the app.\n" +
							"Go to to the following link https://reqbin.com/c-pl24wjkw \n" +
							"then click on :Run: to make a post request" +

							"\n" +

							"Finally head to the client side to test it out!\n" ,
			"1.0",
			"https://github.com/HorizenSS/SparkSuggest",
			contact, 
			"Apache 2.0",
			"https://www.apache.org/licenses/LICENSE-2.0",
			vendorExtensions);

	@Bean
	public Docket apiDocket() {

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.protocols(new HashSet<>(Arrays.asList("HTTP","HTTPs")))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.rapid.engine")).paths(PathSelectors.any())
				.build();

		return docket;

	}

}
