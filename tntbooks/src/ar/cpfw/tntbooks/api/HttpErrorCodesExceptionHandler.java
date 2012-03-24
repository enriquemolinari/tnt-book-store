package ar.cpfw.tntbooks.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

/**
 * @author Enrique Molinari
 */
public class HttpErrorCodesExceptionHandler extends
		DefaultHandlerExceptionResolver implements Ordered {

	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return new ModelAndView().addObject("error",
				"There is no such request method handler... ");
	}

	@Override
	protected ModelAndView handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {

		response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return new ModelAndView()
				.addObject("error",
						"That request method is not supported for the URL you are using ... ");
	}

	@Override
	protected ModelAndView handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex,
			HttpServletRequest request, HttpServletResponse response,
			Object handler) throws IOException {

		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return new ModelAndView().addObject("error",
				"That was a bad request ... ");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
