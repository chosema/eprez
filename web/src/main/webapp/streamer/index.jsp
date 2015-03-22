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
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css">
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

	<div class="wrapper">

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
				<form class="navbar-form navbar-left" ng-controller="recorderController" ng-show="_userIsPresenter">
					<div class="form-group">
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
				</form>
				<div class="container" ng-controller="playerController" ng-show="!_userIsPresenter">
					<audio id="player" style="margin-top: 8px;" controls>
						<source id="playerSource" />
					</audio>
				</div>
				<!-- Navbar Right Menu -->
				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<!-- Messages: style can be found in dropdown.less-->
						<li class="dropdown messages-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-envelope-o"></i> <span
								class="label label-success">4</span>
							</a>
							<ul class="dropdown-menu">
								<li class="header">You have 4 messages</li>
								<li>
									<!-- inner menu: contains the actual data -->
									<ul class="menu">
										<li>
											<!-- start message -->
											<a href="#">
												<div class="pull-left">
													<img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />
												</div>
												<h4>
													Sender Name <small><i class="fa fa-clock-o"></i> 5 mins</small>
												</h4>
												<p>Message Excerpt</p>
											</a>
										</li>
										<!-- end message -->
									</ul>
								</li>
								<li class="footer">
									<a href="#">See All Messages</a>
								</li>
							</ul>
						</li>
						<!-- Notifications: style can be found in dropdown.less -->
						<li class="dropdown notifications-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-bell-o"></i> <span
								class="label label-warning">10</span>
							</a>
							<ul class="dropdown-menu">
								<li class="header">You have 10 notifications</li>
								<li>
									<!-- inner menu: contains the actual data -->
									<ul class="menu">
										<li>
											<a href="#"> <i class="ion ion-ios-people info"></i> Notification title
											</a>
										</li>
									</ul>
								</li>
								<li class="footer">
									<a href="#">View all</a>
								</li>
							</ul>
						</li>
						<!-- Tasks: style can be found in dropdown.less -->
						<li class="dropdown tasks-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-flag-o"></i> <span
								class="label label-danger">9</span>
							</a>
							<ul class="dropdown-menu">
								<li class="header">You have 9 tasks</li>
								<li>
									<!-- inner menu: contains the actual data -->
									<ul class="menu">
										<li>
											<!-- Task item -->
											<a href="#">
												<h3>
													Design some buttons <small class="pull-right">20%</small>
												</h3>
												<div class="progress xs">
													<div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20"
														aria-valuemin="0" aria-valuemax="100">
														<span class="sr-only">20% Complete</span>
													</div>
												</div>
											</a>
										</li>
										<!-- end task item -->
									</ul>
								</li>
								<li class="footer">
									<a href="#">View all tasks</a>
								</li>
							</ul>
						</li>
						<!-- User Account: style can be found in dropdown.less -->
						<li class="dropdown user user-menu">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown"> <img src="dist/img/user2-160x160.jpg"
								class="user-image" alt="User Image" /> <span class="hidden-xs">Alexander Pierce</span>
							</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header">
									<img src="dist/img/user2-160x160.jpg" class="img-circle" alt="User Image" />
									<p>
										Alexander Pierce - Web Developer <small>Member since Nov. 2012</small>
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
										<a href="#" class="btn btn-default btn-flat">Sign out</a>
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
			<section class="sidebar">
				<!-- Sidebar user panel -->
				<div class="user-panel">
					<div class="pull-left image">
						<img src="../../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image">
					</div>
					<div class="pull-left info">
						<p>Alexander Pierce</p>

						<a href="#"><i class="fa fa-circle text-success"></i> Online</a>
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
					<li class="header">MAIN NAVIGATION</li>
					<li class="treeview">
						<a href="#"> <i class="fa fa-dashboard"></i> <span>Dashboard</span> <i class="fa fa-angle-left pull-right"></i>
						</a>
						<ul class="treeview-menu">
							<li>
								<a href="../../index.html"><i class="fa fa-circle-o"></i> Dashboard v1</a>
							</li>
							<li>
								<a href="../../index2.html"><i class="fa fa-circle-o"></i> Dashboard v2</a>
							</li>
						</ul>
					</li>
					<li>
						<a href="../../documentation/index.html"><i class="fa fa-book"></i> Documentation</a>
					</li>
					<li class="header">LABELS</li>
					<li>
						<a href="#"><i class="fa fa-circle-o text-danger"></i> Important</a>
					</li>
					<li>
						<a href="#"><i class="fa fa-circle-o text-warning"></i> Warning</a>
					</li>
					<li>
						<a href="#"><i class="fa fa-circle-o text-info"></i> Information</a>
					</li>
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<div class="content-wrapper">
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
	<!-- JavaScript main eprez bundle -->
	<script src="eprez.js"></script>
</body>
</html>
