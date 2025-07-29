//トップ画面にあるリンク「登録する」をクリック した先の画面を実装 [ロジック担当][top.jspの「登録する」からここに飛ぶ]

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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public SignUpServlet() {
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

		request.getRequestDispatcher("signup.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			//<form action="signup" method="post">２６行目
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();
		//エラーメッセージの空っぽの配列を作る

		User user = getUser(request);
		//userリクエストできたもの　入力された情報「HttpServletRequest request」を引数にしている　getuserはメソッド
		if (!isValid(user, errorMessages)) {
			//isValidは呼び出し元 結果は(isValid(user, errorMessages))にはいる 　今回ならばtrue or  false
			//errorMessagesはまだ空白で宣言したばかり
			//
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("signup.jsp").forward(request, response);
			return;
			//falseがかえってきたら、!あるので、if文を実行する　signup.jspへ飛ぶ
		}
		new UserService().insert(user);
		//trueがかえってきたら
		response.sendRedirect("./");
		//トップに戻る
	}

	private User getUser(HttpServletRequest request) throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		User user = new User();
		user.setName(request.getParameter("name"));
		//画面からきた情報をuserにせっとしてるセットしてる
		user.setAccount(request.getParameter("account"));
		user.setPassword(request.getParameter("password"));
		user.setEmail(request.getParameter("email"));
		user.setDescription(request.getParameter("description"));

		return user;
		//getUser(request);にわたる
	}

	//isvalid 画面から入力されたものを取り込み、文字数などのエラーがなければ、signup.jspへ飛ぶ
	private boolean isValid(User user, List<String> errorMessages) {
		//呼び出され先
		//if

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		String name = user.getName();
		//ユーザーが入植したものが格納されているuserを取得し、nameに入れる
		String account = user.getAccount();
		String password = user.getPassword();
		String email = user.getEmail();
		User UserCheck = new UserService().select(account);

		if (!StringUtils.isEmpty(name) && (20 < name.length())) {
			errorMessages.add("名前は20文字以下で入力してください");
		}
		//エラーがあれば格納する

		if (StringUtils.isEmpty(account)) {
			errorMessages.add("アカウント名を入力してください");
		} else if (20 < account.length()) {
			errorMessages.add("アカウント名は20文字以下で入力してください");
		}
		if (UserCheck != null) {
			errorMessages.add("このアカウント名はすでに使われております");
		}

		if (StringUtils.isEmpty(password)) {
			errorMessages.add("パスワードを入力してください");
		}

		if (!StringUtils.isEmpty(email) && (50 < email.length())) {
			errorMessages.add("メールアドレスは50文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
			//エラーが０ならば、呼び出されたboolean isValidはfalse
		}
		return true;
	} //そうでなければ、true これを
}
