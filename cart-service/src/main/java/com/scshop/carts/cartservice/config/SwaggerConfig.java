package com.scshop.carts.cartservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket orderApis() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("cart-apis").apiInfo(metaData()).select()
				//.apis(RequestHandlerSelectors.basePackage("com.scshop.orders.orderservice"))
				.paths(pathsToCatpure()).build();
	}


	private Predicate<String> pathsToCatpure() {
		return PathSelectors.ant("/api/**");
	}

	private ApiInfo metaData() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("Cart Service APIs")
				.description("Cart Service APIs for managing cart")
				.termsOfServiceUrl("http://sc-shop.com/tnc/notyetavailable")
				.contact(new Contact("Sushant", "http://sushantac.com", "sushantc.developer@gmail.com"))
				.licenseUrl("http://sc-shop.com/license/notyetavailable").version("1.0").build();

		return apiInfo;
	}
}