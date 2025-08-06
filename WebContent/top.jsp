<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>簡易Twitter</title>
<link href="./style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="header">
		<c:if test="${ empty loginUser }">
			<a href="login">ログイン</a>
			<a href="signup">登録する</a>
		</c:if>
		<c:if test="${ not empty loginUser }">
			<a href="./">ホーム</a>
			<a href="setting">設定</a>
			<a href="logout">ログアウト</a>
		</c:if>
		<c:if test="${ not empty loginUser }">
			<div class="profile">
				<div class="name">
					<h2>
						<c:out value="${loginUser.name}" />
					</h2>
				</div>
				<div class="account">
					@
					<c:out value="${loginUser.account}" />
				</div>
				<div class="description">
					<c:out value="${loginUser.description}" />
				</div>
			</div>
		</c:if>
		<div class = "confine">
			<form action="./" method="get">
				<input type="date" name="start">
				～
				<input type="date" name="end">
				<input type="submit" value="絞り込み">
			</form>
		</div>
	</div>
	<c:if test="${ not empty errorMessages }">
		<div class="errorMessages">
			<ul>
				<c:forEach items="${errorMessages}" var="errorMessage">
					<li><c:out value="${errorMessage}" />
				</c:forEach>
			</ul>
		</div>
		<c:remove var="errorMessages" scope="session" />
	</c:if>
	<!--つぶやき-->
	<div class="form-area">
		<c:if test="${ isShowMessageForm }">
			<form action="message" method="post">
				いま、どうしてる？<br />
				<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
				<br /> <input type="submit" value="つぶやく">（140文字まで）
			</form>
		</c:if>
	</div>
	<!--今までのつぶやきの表示-->
	<div class="messages">
		<c:forEach items="${messages}" var="message">
			<!-- var 要素を格納する変数名 messagesはTOPservletから-->
			<!-- top.jsp → top.servlet   初期表示の時は、getでURLに行くと、WebServlet(urlPatterns = { "/index.jsp" })に紐づくものが動く　そういう設定 -->
			<div class="message">
				<div class="account-name">
					<span class="account"> <a
						href="./?user_id=<c:out value="${message.userId}"/> "> <c:out value="${message.account}" /></a>
						<!--user_id=　　パラメータ「これがあると、URLに表記される　→postにあらず→　get」-->
						<!--リンクが押されて、ユーザーIDを渡して、その人のモノを下記で示す　（リンクを触らなければ全部）-->
					</span> <span class="name"> <c:out value="${message.name}" />
					</span>
				</div>
				<div class="text">
					<pre><c:out value="${message.text}" /></pre>
				</div>
				<div class="date">
					<fmt:formatDate value="${message.createdDate}"
						pattern="yyyy/MM/dd HH:mm:ss" />
				</div>
				<!--削除表示-->
				<c:if test="${message.userId == loginUser.id}">
					<form action="deleteMessage" method="post">
						<input type="submit" value="削除">
						 <input name="id" value="${message.id}" id="id" type="hidden" />
						<!-- input　いろいろな機能をすでに持つ　form内で同時に遅れる-->
						<!-- vale 初期値　id HTML要素に一意な名前を付けるための属性 -->
						<!-- nameサーバーに送ったデータを識別する際の識別子-->
					</form>
					<!--更新表示-->
					<form action="edit" method="get">
						<input type="submit" value="編集">
						<input name="id" value="${message.id}" id="id" type="hidden" />
					</form>
				</c:if>

				<!--返信の表示用-->
				<c:forEach items="${comments}" var="comment">
					<c:if test="${message.id == comment.messageId}">
					<!--message.id「editでも使っている」comment.messageId「同じクラスの中の同じメッセージ」-->
						<div class="name">
			 				<c:out value="${comment.name}" />
						</div>
						<div class="account">
			 				<c:out value="${comment.account}" />
						</div>
						<div class="text">
		 					<c:out value="${comment.account}" />
						</div>							<div class="text">
			 				<pre><c:out value="${comment.text}" /></pre>
						</div>
						<div class="date">
							<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
						</div>
					</c:if>
				</c:forEach>

				<!--返信用-->
				<div class="replaydisplay">
					<c:if test="${ isShowMessageForm }">
						<form action="comment" method="post">
							返信する？<br /><textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
							<br /> <input type="submit" value="返信">（140文字まで）
							<input name="id" value="${message.id}" id="id" type="hidden" />

						</form>
					</c:if>
				</div>
			</div>
		</c:forEach>
	</div>
</body>
</html>