package ar.cpfw.tntbooks.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

//TODO: sobrescribir lo que falta ... 
public class HttpErrorCodesExceptionHandler extends DefaultHandlerExceptionResolver
		implements Ordered {

	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {

		return new ModelAndView().addObject("error", "the error msg ... ");
	}

	@Override
	protected ModelAndView handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {
		return new ModelAndView().addObject("error", "the error msg ... ");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
