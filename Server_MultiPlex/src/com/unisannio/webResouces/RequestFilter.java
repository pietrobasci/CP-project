package com.unisannio.webResouces;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.engine.header.Header;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

public class RequestFilter extends Filter {

	  public RequestFilter(Context context) {
        super(context);
	  }

	  
	@Override
	  protected int beforeHandle(Request request, Response response) {
		
		int result = STOP;
	    @SuppressWarnings({ "unchecked" })
	    Series<Header> responseHeaders = (Series<Header>) request.getAttributes().get("org.restlet.http.headers");

	    System.out.println(responseHeaders.getValues("User-Agent"));
	    
	    if(responseHeaders.getValues("User-Agent").equals("Restlet-Framework/2.2.3,ClientJava") || responseHeaders.getValues("User-Agent").equals("Restlet-Framework/2.2.3,ClientAndroid"))
	    	result = CONTINUE;
	    
	    return result;
	    
	   }
	
	
}
