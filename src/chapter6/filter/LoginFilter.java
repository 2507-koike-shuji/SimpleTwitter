package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

@WebFilter(urlPatterns = { "/edit", "/setting" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//double d = 100.0;　[元の形]
		//int i = (int)d;　[こうなりたい]

		//型変換
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		HttpSession session = httpRequest.getSession();
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser != null) {
			//chain.doFilter でリクエストのあった画面に遷移
			chain.doFilter(request, response);
		} else {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインをしてください");
			session.setAttribute("errorMessages", errorMessages);
			httpResponse.sendRedirect("./login");
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}
}
