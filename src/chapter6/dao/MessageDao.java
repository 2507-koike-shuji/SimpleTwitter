//Dao：DBへの窓口　
//つぶやきに関するDBの操作を行うDao
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

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//つぶやきの投稿
	public void insert(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//つぶやきの削除
	public void delete(Connection connection, String id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM messages ");
			sql.append("   WHERE id = ? ");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, id);

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//つぶやきの更新画面の表示
	public Message select(Connection connection, Integer id) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//	1：SQLの下地を作る
		PreparedStatement ps = null;

		//2:SQL文の作成
		try {
			//もし、ユーザーIDが押下されたら、ユーザーのものだけを示す
			//青文字をそのまま、MYSQLで使うと、検索結果が出る　今回はここを該当のユーザーだけをだした
			StringBuilder sql = new StringBuilder();

			sql.append("SELECT * FROM messages ");
			sql.append("WHERE id = ?");

			//3：SQLを実行できるようにする
			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, id);

			//5:SQｌを実行、結果はrsに入る
			ResultSet rs = ps.executeQuery();

			//結果を詰め替える
			List<Message> message = toUserMessages(rs);
			//メソッドを呼び出している「private List<UserMessage> toUserMessages(ResultSet rs) throws SQLException」
			//求めていたモノをすべて表示するため lsit
			if (message.isEmpty()) {
				return null;
			} else {
				return message.get(0);
			}

			//ないのに０番目を返そうとしている
			//→userdaoが参考になる
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
	//型をbeansで設定していた
	private List<Message> toUserMessages(ResultSet rs) throws SQLException {
		//     戻り値の型         メソッド名     引数

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<Message> messages = new ArrayList<Message>();
		try {
			while (rs.next()) {
				//オブジェクトのデータがなくなるまで処理を繰り返すためのループ構文

				//カラムごとに詰め替える
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setUserId(rs.getInt("user_id"));
				message.setText(rs.getString("text"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setUpdatedDate(rs.getTimestamp("updated_date"));
				messages.add(message);
			}
			return messages;// List<UserMessage> の戻り値の型と一致 messagesは呼び出し元のist<UserMessage> messages = toUserMessages(rs);「」の「 toUserMessages(rs)」へ飛ぶ
		} finally {
			close(rs);
		}
	}

	//つぶやきの更新
	public void update(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE messages SET ");
			sql.append("text = ?, ");
			sql.append("updated_date = CURRENT_TIMESTAMP ");
			sql.append("WHERE id = ?");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, message.getText());
			ps.setInt(2, message.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}
}