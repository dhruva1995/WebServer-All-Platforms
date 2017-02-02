package service;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.client.HttpAsyncClient;

import constants.Constants;

public class LEDService {

	
	
	
	public static void makeAsynchronousHttpCall(Color selectedColor, JComponent parentForErr){
		HttpAsyncClient asyncClient = HttpAsyncClientBuilder.create().build();
		HttpGet get = new HttpGet(getURI(selectedColor, parentForErr));
		System.out.println("Making http call to : " + get.getURI());
		asyncClient.execute(get, getFutureHandler(parentForErr));
	}

	private static FutureCallback<HttpResponse> getFutureHandler(JComponent parentForErr) {
		return new FutureCallback<HttpResponse>() {
			
			@Override
			public void failed(Exception ex) {
				bangException(parentForErr, ex);
			}
			
			@Override
			public void completed(HttpResponse result) {
				try {
					System.out.println(IOUtils.toString(result.getEntity().getContent(), Charset.defaultCharset()));
				} catch (UnsupportedOperationException | IOException e) {
					bangException(parentForErr, e);
				}
			}
			
			@Override
			public void cancelled() {
			}
		};
	}

	private static URI getURI(Color selectedColor, JComponent parentForErr) {
		URIBuilder builder = null;
		URI url = null;
		try {
			builder = new URIBuilder(getURLWithoutQueryParams(parentForErr));
		} catch (URISyntaxException e) {
			bangException(parentForErr, e);
		} 
		builder.addParameter(Constants.COLOR_RED, String.valueOf(selectedColor.getRed()));
		builder.addParameter(Constants.COLOR_GREEN, String.valueOf(selectedColor.getGreen()));
		builder.addParameter(Constants.COLOR_BLUE, String.valueOf(selectedColor.getBlue()));
		try{
			url =  builder.build();
		} catch (URISyntaxException e) {
			bangException(parentForErr, e);
		}
		return url;
	}

	private static String getURLWithoutQueryParams(JComponent parent) {
		Properties configProps = new Properties();
		try(InputStream config = LEDService.class.getResourceAsStream("/config.properties")){
			configProps.load(config);
		} catch (IOException e) {
			bangException(parent, e);
		}
		return StrSubstitutor.replace(Constants.URL_BASE, configProps);
	}



	private static void bangException(JComponent parent, Exception ex){
		JOptionPane.showInternalMessageDialog(parent, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
	}
	
}
