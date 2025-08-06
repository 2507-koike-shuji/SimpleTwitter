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

import chapter6.beans.UserMessage;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserMessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public UserMessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//DBからデータを引っ張ってくる
	public List<UserMessage> select(Connection connection, String startTime, String endTime, Integer id, int num) {
		//DBUtilsでconnectionsのメソッドを作っている

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//	1：SQLの下地を作る
		PreparedStatement ps = null;

		//2:SQL文の作成
		try {
			//もし、ユーザーIDが押下されたら、ユーザーのものだけを示す
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT ");
			sql.append("    messages.id as id, ");
			sql.append("    messages.text as text, ");
			sql.append("    messages.user_id as user_id, ");
			sql.append("    users.account as account, ");
			sql.append("    users.name as name, ");
			sql.append("    messages.created_date as created_date ");
			sql.append("FROM messages ");
			sql.append("INNER JOIN users ");
			sql.append("ON messages.user_id = users.id ");
			sql.append("WHERE messages.created_date BETWEEN ? AND ? ");
			if (id != null) {
				sql.append("AND messages.user_id = ? ");
			}
			sql.append("ORDER BY created_date DESC limit " + num);

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, startTime);
			ps.setString(2, endTime);
			if (id != null) {
				ps.setInt(3, id);
			}
			//topから持ってきたリンクの人のidを?に入れる

			//5:SQｌを実行、結果はrsに入る
			ResultSet rs = ps.executeQuery();

			//結果を詰め替える
			List<UserMessage> messages = toUserMessages(rs);
			//メソッドを呼び出している「private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException」
			//求めていたモノをすべて表示するため　lsit
			return messages;
			//rerturnで35行目の戻り値　	public List<UserMessage> select(Connection connection, Integer id, int num) {に渡す

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException {
		//     戻り値の型         メソッド名     引数

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<UserMessage> messages = new ArrayList<UserMessage>();
		try {
			while (rs.next()) {
				//オブジェクトのデータがなくなるまで処理を繰り返すためのループ構文

				//カラムごとに詰め替える
				UserMessage message = new UserMessage();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("user_id"));
				message.setAccount(rs.getString("account"));
				message.setName(rs.getString("name"));
				message.setCreatedDate(rs.getTimestamp("created_date"));

				messages.add(message);
			}
			return messages;// List<UserMessage> の戻り値の型と一致 messagesは呼び出し元のist<UserMessage> messages = toUserMessages(rs);「」の「 toUserMessages(rs)」へ飛ぶ
		} finally {
			close(rs);
		}
	}
}
