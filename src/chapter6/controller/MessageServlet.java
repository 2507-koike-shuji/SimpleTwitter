//「つぶやき機能｣のロジック
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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/message" })
public class MessageServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());
		//セッションを利用するには必要
		HttpSession session = request.getSession();
		//空の配列をつくる
		List<String> errorMessages = new ArrayList<String>();
		//リクエストから値を取り出す際の基本構文
		String text = request.getParameter("text");
		//呼び出している（）は呼び出し先に渡している
		if (!isValid(text, errorMessages)) {
			//問題あるならば、セッションに入れる
			session.setAttribute("errorMessages", errorMessages);
			//メイン画面に戻す(top.jspのエラーの記載のところに飛び)　この先には進まない
			//セッションに入れないと、画面が飛んだ瞬間に無くなる
			response.sendRedirect("./");
			return;
		}

		Message message = new Message();
		message.setText(text);

		User user = (User) session.getAttribute("loginUser");
		message.setUserId(user.getId());

		new MessageService().insert(message);
		response.sendRedirect("./");
	}

	//呼び出された（つぶやきで送られてきた内容の審査）
	private boolean isValid(String text, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(text)) {
			errorMessages.add("メッセージを入力してください");
		} else if (140 < text.length()) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}
}
