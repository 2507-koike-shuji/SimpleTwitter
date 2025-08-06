//Webサーバで動く用のJavaプログラム「Servlet ＋ JSP」の合体で表記　　
//サーバー型topservlet　画面側jsp

package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
	//HttpServletは、リクエストを受信し、レスポンスを返すための受け口
	//extendはhttpにもともと設定されていることを使うことを示す

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
		//chapter6.logging[InitApplication]で読み込みした設定ファイル読み込み機能をこちらでも使う
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
		String fromTime = request.getParameter("fromTime");
		String byTime = request.getParameter("byTime");
		//リンクを押されて該当のユーザーのみを示すときに使う　
		String userId = request.getParameter("user_id");

		//          下へ 				MessageServiceにuserIdを渡す　及びかえって来る
		List<UserMessage> messages = new MessageService().select(fromTime, byTime, userId);

		List<UserComment> comments = new CommentService().select();
		//全てを持ってくるから条件はなく、空でよい

		//top.jspに渡す${messages} リクエストから値を取り出す際の基本構文  <%= request.getAttribute("messages") %>
		request.setAttribute("messages", messages);

		request.setAttribute("comments", comments);
		//第一引数に格納する名前(key)[テーブル]、第二引数に格納する値(value)「60行」を渡す
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		//getRequestDispatcherメソッドの引数に呼び出したいJSP名指定してRequestDispatcherオブジェクトに渡すことで、画面の指定
		request.getRequestDispatcher("/top.jsp").forward(request, response);
		//getRequestDispatcherメソッドの引数に呼び出したいJSP名指定してRequestDispatcherオブジェクトに渡すことで、画面の指定
	}
}