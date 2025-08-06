package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserCommentDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	public List<UserComment> select(Connection connection, int num) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//	1：SQLの下地を作る
		PreparedStatement ps = null;

		try {

			StringBuilder sql = new StringBuilder();

			sql.append("SELECT ");
			sql.append("    comments.id as id, ");
			sql.append("    comments.text as text, ");
			sql.append("    comments.user_id as user_id, ");
			sql.append("    comments.message_id as message_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    comments.created_date as created_date, ");
			sql.append("    comments.updated_date as updated_date ");
			sql.append("FROM comments ");
			sql.append("INNER JOIN users ");
			sql.append("ON comments.user_id = users.id ");
			sql.append("ORDER BY created_date ASC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ResultSet rs = ps.executeQuery();
			List<UserComment> comments = toUerComment(rs);
			//メソッドを呼び出している「private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException」
			//求めていたモノをすべて表示するため　lsit
			return comments;
			//rerturnで35行目の戻り値　	public List<UserMessage> select(Connection connection, Integer id, int num) {に渡す

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//resultset型[データを扱えない]からList<UserMessage>につめ変える

	private List<UserComment> toUerComment(ResultSet rs) throws SQLException {
		//     戻り値の型         メソッド名     引数

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserComment> comments = new ArrayList<UserComment>();
		try {
			while (rs.next()) {
				//オブジェクトのデータがなくなるまで処理を繰り返すためのループ構文

				//カラムごとに詰め替える
				UserComment comment = new UserComment();
				comment.setId(rs.getInt("id"));
				comment.setText(rs.getString("text"));
				comment.setUserId(rs.getInt("user_id"));
				comment.setMessageId(rs.getInt("message_id"));
				comment.setAccount(rs.getString("account"));
				comment.setName(rs.getString("name"));
				comment.setCreatedDate(rs.getTimestamp("created_date"));
				comment.setUpdatedDate(rs.getTimestamp("updated_date"));
				comments.add(comment);
			}
			return comments;// List<UserMessage> の戻り値の型と一致 messagesは呼び出し元のist<UserMessage> messages = toUserMessages(rs);「」の「 toUserMessages(rs)」へ飛ぶ
		} finally {
			close(rs);
		}
	}
}
