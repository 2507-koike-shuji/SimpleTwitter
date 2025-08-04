//Beans：データをまとめる入れもの
//つぶやき情報をひとまとめに扱う

package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

	private int id;
	private int userId;
	private int messageId;
	private String text;
	private Date createdDate;
	private Date updatedDate;

	// getter/setterは省略されているので、自分で記述しましょう。
	//id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//useId
	public int getUserId() {
		return userId;
	}

	public void setUserId(int useId) {
		this.userId = useId;
	}
	//messageId
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	//text
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	//createdDate
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	//updatedDate
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}