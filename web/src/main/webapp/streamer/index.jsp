<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>ePrez - Streamer</title>

<!-- Bootstrap -->
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet" type="text/css">
<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css"> -->
<!-- Font Awesome Icons -->
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<!-- AdminLTE Theme style -->
<link href="../resources/adminLTE/css/AdminLTE.min.css" rel="stylesheet" type="text/css">
<!-- AdminLTE Skins. Choose a skin from the css/skins folder instead of downloading all of them to reduce the load. -->
<link href="../resources/adminLTE/css/skins/_all-skins.min.css" rel="stylesheet" type="text/css">

<link href="eprez.css" rel="stylesheet" type="text/css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

<%
	final String _streamerHttpPath = WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getEnvironment().resolvePlaceholders("${streamer.http.url}");
	final String _streamerWsPath = WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getEnvironment().resolvePlaceholders("${streamer.ws.url}");
%>

<script type="text/javascript">
	var _contextPath = '<%=request.getContextPath()%>';
	var _streamerHttpPath = '<%=_streamerHttpPath%>';
	var _streamerWsPath = '<%=_streamerWsPath%>';
</script>
</head>
<body ng-controller="rootController" class="skin-blue sidebar-collapse fixed">

	<div id="contentWrapper" class="wrapper" style="display: none;">

		<header class="main-header">
			<a href="index.jsp" class="logo"> <!-- LOGO --> <b>Eprez</b>Streamer</a>
			<!-- Header Navbar: style can be found in header.less -->
			<nav class="navbar navbar-static-top" role="navigation">
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</a>
				<form class="navbar-form navbar-left">
					<div class="form-group" ng-controller="recorderController" ng-show="_userIsPresenter != null && _userIsPresenter">
						<select class="form-control" ng-model="samplerate" ng-options="'Sample Rate: ' + s + ' Hz' for s in samplerates"></select>
						<select class="form-control" ng-model="bitrate" ng-options="'Bit Rate: ' + b + ' kbps' for b in bitrates"></select>
						<div class="btn-group">
							<button class="btn btn-default btn-sm" ng-click="startRecording()" ng-disabled="recording">
								<i class="fa fa-play" style="color: green;"></i>
							</button>
							<button class="btn btn-default btn-sm" ng-click="stopRecording()" ng-disabled="!recording">
								<i class="fa fa-stop" style="color: red;"></i>
							</button>
						</div>
						<span ng-show="recording" style="color: white;">Recording...</span>
					</div>
					<div style="height: 20px;" ng-controller="playerController" ng-show="_userIsPresenter !=null && !_userIsPresenter">
						<audio id="player" controls>
							<source id="playerSource" />
						</audio>
					</div>
				</form>
				<!-- Navbar Right Menu -->
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<!-- Messages: style can be found in dropdown.less-->
						<li class="dropdown messages-menu" ng-controller="chatController">
							<a href="#" ng-click="onToggleChat()">
								<i class="fa fa-envelope-o"></i> <span class="label label-success" ng-show="newMessagesCount > 0">{{newMessagesCount}}</span>
							</a>
						</li>
						<!-- User Account: style can be found in dropdown.less -->
						<li class="dropdown user user-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown">
								<img ng-src="{{_user.identicon}}" class="user-image" alt="User Image" />
								<span class="hidden-xs">{{_user.displayName}}</span>
							</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header">
									<img ng-src="{{_user.identicon}}" alt="User Image" />
									<p>
										{{_user.displayName}} <small>{{_user.email}}</small>
									</p>
								</li>
								<!-- Menu Body -->
								<li class="user-body">
									<div class="col-xs-4 text-center">
										<a href="#">Followers</a>
									</div>
									<div class="col-xs-4 text-center">
										<a href="#">Sales</a>
									</div>
									<div class="col-xs-4 text-center">
										<a href="#">Friends</a>
									</div>
								</li>
								<!-- Menu Footer-->
								<li class="user-footer">
									<div class="pull-left">
										<a href="#" class="btn btn-default btn-flat">Profile</a>
									</div>
									<div class="pull-right">
										<a href="<%=request.getContextPath()%>/logout" class="btn btn-default btn-flat">Sign out</a>
									</div>
								</li>
							</ul>
						</li>
					</ul>
				</div>
			</nav>
		</header>

		<aside class="main-sidebar">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar" ng-controller="listenersController">
				<!-- Sidebar user panel -->
				<div class="user-panel" ng-repeat="userToken in _presentation.session.tokens | filter: { presenter: true }">
					<div class="pull-left image">
						<img ng-src="{{userToken.user.identicon}}" alt="User Image">
					</div>
					<div class="pull-left info">
						<p>{{userToken.user.displayName}}</p>
						<a href="#" ng-show="userToken.online"><i class="fa fa-circle text-success"></i> Online</a>
						<a href="#" ng-show="!userToken.online"><i class="fa fa-circle" style="color: rgb(187, 187, 187);"></i> Offline</a>
					</div>
				</div>
				<!-- search form -->
				<form action="#" method="get" class="sidebar-form">
					<div class="input-group">
						<input type="text" name="q" class="form-control" placeholder="Search..."> <span class="input-group-btn">
							<button type="submit" name="seach" id="search-btn" class="btn btn-flat">
								<i class="fa fa-search"></i>
							</button>
						</span>
					</div>
				</form>
				<!-- /.search form -->
				<!-- sidebar menu: : style can be found in sidebar.less -->
				<ul class="sidebar-menu">
					<li class="header">Connected users</li>
					<li ng-repeat="userToken in _presentation.session.tokens | filter: { presenter: false } | limitTo: 10">
						<div class="user-panel">
							<div class="pull-left image">
								<img ng-src="{{userToken.user.identicon}}" alt="User Image">
							</div>
							<div class="pull-left info">
								<p>{{userToken.user.displayName}}</p>
								<a href="#" ng-show="userToken.online"><i class="fa fa-circle text-success"></i> Online</a>
								<a href="#" ng-show="!userToken.online"><i class="fa fa-circle" style="color: rgb(187, 187, 187);"></i> Offline</a>
							</div>
						</div>
					</li>
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<div class="content-wrapper">
			<!-- Chat -->
			<div id="chatBox" class="box box-primary direct-chat direct-chat-primary collapsed-box" style="position: absolute; z-index: 1; width: 400px; margin: 10px;"
				ng-controller="chatController">
				<div class="box-header with-border">
					<h3 class="box-title">Direct Chat</h3>
					<div class="box-tools pull-right">
						<span data-toggle="tooltip" data-placement="bottom" title="" class="badge bg-light-blue"
							data-original-title="{{newMessagesCount}} New Message{{newMessagesCount == 1 ? '' : 's'}}" ng-show="newMessagesCount > 0">{{newMessagesCount}}</span>
						<button id="collapseChatBoxBtn" class="btn btn-box-tool" data-widget="collapse" ng-click="onFocusChat()">
							<i class="fa fa-plus"></i>
						</button>
						<!-- <button class="btn btn-box-tool" data-widget="remove">
							<i class="fa fa-times"></i>
						</button> -->
					</div>
				</div>
				<!-- /.box-header -->
				<div class="box-body" style="display: none;">
					<!-- Conversations are loaded here -->
					<div class="direct-chat-messages">
						<!-- Message. Default to the left -->
						<div ng-repeat="message in messages" class="direct-chat-msg" ng-class="message.currentUser ? 'right' : ''">
							<div class="direct-chat-info clearfix">
								<span class="direct-chat-name" ng-class="message.currentUser ? 'pull-right' : 'pull-left'">{{message.user.displayName}}</span>
								<span class="direct-chat-timestamp" ng-class="message.currentUser ? 'pull-left' : 'pull-right'">{{message.date | date: 'd MMM hh:mm'}}</span>
							</div> <!-- /.direct-chat-info -->
							<img class="direct-chat-img" ng-src="{{message.user.identicon}}" alt="message user image">
							<!-- /.direct-chat-img -->
							<div class="direct-chat-text">{{message.text}}</div>
							<!-- /.direct-chat-text -->
						</div>	<!-- /.direct-chat-msg -->
					</div>	<!--/.direct-chat-messages-->
				</div> <!-- /.box-body -->
				<div class="box-footer" style="display: none;">
					<form action="#" method="post">
						<div class="input-group">
							<input id="chatMessageInput" type="text" name="message" placeholder="Type Message ..." class="form-control" onkeydown="if(event.keyCode == 13){return false;}"
								ng-model="message" ng-focus="onResetNewMessages()" ng-keydown="onSubmitMessage($event)">
							<span class="input-group-btn">
								<button type="button" class="btn btn-primary btn-flat" ng-click="onSend()">Send</button>
							</span>
						</div>
					</form>
				</div> <!-- /.box-footer-->
			</div>

			<!-- Main content -->
			<div id="document-carousel" class="carousel slide" ng-controller="documentCarouselController">
				<!-- Indicators -->
				<ol class="carousel-indicators">
					<li ng-repeat="page in _presentation.document.pages" title="Slide: {{page.index + 1}}" ng-class="_presentation.session.currentPageIndex == $index ? 'active' : ''"
						ng-click="_userIsPresenter && onSlide($index)" style="margin-right: 10px;"></li>
				</ol>
		
				<!-- Wrapper for slides -->
				<div class="carousel-inner" role="listbox">
					<div class="item" ng-class="$index == 0 ? 'active' : ''" ng-repeat="page in _presentation.document.pages">
						<img ng-src="{{page.src}}" class="img-responsive center-block">
						<div class="carousel-caption" style="color: gray;">Current slide: {{page.index + 1}}</div>
					</div>
				</div>

				<!-- Controls -->
				<a class="left carousel-control" href="" role="button" ng-click="onPreviousSlide()"
					ng-show="_userIsPresenter && _presentation.session.currentPageIndex > 0">
					<span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
					<span class="sr-only">Previous</span>
				</a>
				<a class="right carousel-control" href="" role="button" ng-click="onNextSlide()"
					ng-show="_userIsPresenter && _presentation.session.currentPageIndex < _presentation.document.pages.length - 1">
					<span class="glyphicon glyphicon-chevron-right"	aria-hidden="true"></span>
					<span class="sr-only">Next</span>
				</a>
			</div>
			<!-- /.content -->
		</div>
	</div>

	<!-- jQuery 1.11.2 -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js" type="text/javascript"></script>
	<!-- FastClick plugin for mobile devices (optimize 300ms delay) -->
    <!-- <script src='../resources/adminLTE/plugins/fastclick/fastclick.min.js'></script> -->
    <!-- SlimScroll plugin -->
    <script src='../resources/adminLTE/plugins/slimScroll/jquery.slimscroll.min.js'></script>
    <!-- Bootstrap 3.3.4 JS -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js" type="text/javascript"></script>
	<!-- AngularJS 1.3.14 -->
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js" type="text/javascript"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.min.js" type="text/javascript"></script>
	<!-- AdminLTE App -->
    <script src="../resources/adminLTE/js/app.min.js" type="text/javascript"></script>
    <!-- Identicon library with PNG dependency -->
    <script src="pnglib.js" type="text/javascript"></script>
    <script src="identicon.js" type="text/javascript"></script>
	<!-- JavaScript main eprez bundle -->
	<script src="eprez.js" type="text/javascript"></script>

	<div id="loadingIndicator" class="box box-default box-solid" style="position: absolute;  top: 0; left: 0; width: 100%; height: 100%;"
		ng-controller="loadingController">
		<div class="box-header with-border" style="text-align: center;">
			<h3 class="box-title">Loading...</h3>
		</div>
		<div class="box-body"></div>
		<!-- /.box-body -->
		<!-- Loading (remove the following to stop the loading)-->
		<div class="overlay">
			<i class="fa fa-refresh fa-spin"></i>
		</div> <!-- end loading -->
	</div>
</body>
</html>
