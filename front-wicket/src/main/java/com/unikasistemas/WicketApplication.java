package com.unikasistemas;

import java.sql.Connection;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;


import de.agilecoders.wicket.core.Bootstrap;
import de.agilecoders.wicket.core.settings.BootstrapSettings;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see com.unikasistemas.Start#main(String[])
 */
public class WicketApplication extends WebApplication{

	@Override
	public Class<? extends WebPage> getHomePage(){
		return HomePage.class;
	}

	@Override
	public void init(){
		super.init();

		// instancia as configuracoes do bootstrap
		BootstrapSettings settings = new BootstrapSettings();
		Bootstrap.install(this, settings);
	}


}
