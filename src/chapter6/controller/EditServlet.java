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

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			//サーバーが受信したブラウザからのリクエストに対応するオブジェクトが 「HttpServletRequest」
			//getブラウザはサーバーに対してページの取得を要求「画面表示」	post入力したデータをサーバーに転送する際に使用[更新] requstにユーザーが入力したものが入っている
			//top.jsp → top.servlet  初期表示の時は、getでURLに行くと、WebServlet(urlPatterns = { "/index.jsp" })に紐づくものが動く設定
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		boolean isShowMessageForm = false;
		//セッションで得たloginUserの情報をuserに入れてる	getSessionメソッドを呼び出すことで、セッションが利用できるようになる　「呼び出す＋getを１文にする」
		User user = (User) request.getSession().getAttribute("loginUser");

		if (user != null) {
			isShowMessageForm = true;
		}

		String id = request.getParameter("id");
		List<String> errorMessages = new ArrayList<String>();
		HttpSession session = request.getSession();

		if (StringUtils.isBlank(id) || !id.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			//("./");top.servletに飛ぶ
			return;
		}

		//    下へ    MessageServiceにuserIdを渡す　及びかえって来る
		Message message = new MessageService().select(Integer.parseInt(id)); 	//[この行を変更いたしました（selection　→select）※コードレビュー後にこのコメントは削除」
		if (message == null) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		//top.jspに渡す${messages} リクエストから値を取り出す際の基本構文  <%= request.getAttribute("messages") %>
		request.setAttribute("message", message);

		//第一引数に格納する名前(key)[テーブル]、第二引数に格納する値(value)「60行」を渡す
		request.setAttribute("isShowMessageForm", isShowMessageForm);

		//getRequestDispatcherメソッドの引数に呼び出したいJSP名指定してRequestDispatcherオブジェクトに渡すことで、画面の指定
		request.getRequestDispatcher("/edit.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<String> errorMessages = new ArrayList<String>();

		Message message = new Message();
		//edit.jspからメッセージIDを受けている（(request.getParameter("id"）を受けてる
		message.setId(Integer.parseInt(request.getParameter("id")));
		message.setText(request.getParameter("description"));

		String messageCheck = message.getText();
		if (!Valid(messageCheck, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);

			request.setAttribute("message", message);
			//画面表示　＝　リソースをもう一度forward
			request.getRequestDispatcher("/edit.jsp").forward(request, response);
			return;
		}
		new MessageService().update(message);
		response.sendRedirect("./");
	}

	private boolean Valid(String messageCheck, List<String> wrongMessages) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(messageCheck)) {
			wrongMessages.add("メッセージを入力してください");
		} else if (140 < messageCheck.length()) {
			wrongMessages.add("140文字以下で入力してください");
		}

		if (wrongMessages.size() != 0) {
			//メッセージのエラーが０個でないならば＝1個以上ならば、
			return false;
		}
		return true;
	}

}