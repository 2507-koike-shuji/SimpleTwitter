//画面のロジックの部分
// ログイン機能を実装
package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/login" })
public class LoginServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public LoginServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		request.getRequestDispatcher("login.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String accountOrEmail = request.getParameter("accountOrEmail");
		String password = request.getParameter("password");

		User user = new UserService().select(accountOrEmail, password);
		if (user == null) {
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインに失敗しました");
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			return;
		}

		HttpSession session = request.getSession();
		// セッションを取得する
		session.setAttribute("loginUser", user);
		//logoinuserという名前でuser型のuserをセッションに入れた

		//①セッションを取得「開始の準備」②入力されたものをloginuser（初出）でuserを入れる ③今後、loginuserを使えるようになる

		//セッションの始まりはどこだ？ログインしたときだ　そこからログアウトするまで

		//セッションの準備（①～③）をloginでして、必要な時に毎回セッションを取得してから、set getする必要がある「それが一つの文になることもある」
		response.sendRedirect("./");
	}
}
