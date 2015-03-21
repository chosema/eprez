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
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
<!-- Font Awesome Icons -->
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">

<link href="eprez.css" rel="stylesheet">

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
<body ng-controller="rootController">

	<nav class="navbar navbar-default navbar-fixed-top" style="max-width: 1100px; margin: 0 auto;">
		<div class="container" ng-controller="recorderController" ng-show="_userIsPresenter">
			<label>Sample Rate: <select class="form-control" ng-model="samplerate"
				ng-options="s + ' Hz' for s in samplerates"></select></label> <label>Bit Rate: <select class="form-control"
				ng-model="bitrate" ng-options="b + ' kbps' for b in bitrates"></select></label>
			<button class="btn btn-default" ng-click="startRecording()" ng-disabled="recording">
				<i class="fa fa-play" style="color: green;"></i> Start presentation
			</button>
			<button class="btn btn-default" ng-click="stopRecording()" ng-disabled="!recording">
				<i class="fa fa-stop" style="color: red;"></i> Stop presentation
			</button>
			<span ng-show="recording">Recording...</span>
		</div>
		<div class="container" ng-controller="playerController" ng-show="!_userIsPresenter">
			<audio id="player" style="margin-top: 8px;" controls>
				<source id="playerSource" />
			</audio>
		</div>
	</nav>

	<div id="document-carousel" class="slide" style="height: 100%;" ng-controller="documentCarouselController">
		<!-- Indicators -->
		<ol class="carousel-indicators">
			<li ng-repeat="page in _presentation.document.pages" title="Slide: {{page.index + 1}}" ng-class="_presentation.session.currentPageIndex == $index ? 'active' : ''"
				ng-click="_userIsPresenter && onSlide($index)" style="margin-right: 10px;"></li>
		</ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			<div class="item" ng-class="$index == 0 ? 'active' : ''" ng-repeat="page in _presentation.document.pages" ng-style="page.style">
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

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular-resource.min.js"></script>
	<!-- JavaScript main eprez bundle -->
	<script src="eprez.js"></script>
</body>
</html>
