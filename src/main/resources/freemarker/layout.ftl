<#macro do title>
<!DOCTYPE html>
<html ng-app="app">
<head>
	<title>${title}</title>
	<link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css" />
	<script type="text/javascript" src="/assets/angular.min.js"></script>
	<script type="text/javascript" src="/assets/app.js"></script>
<head>
<body>
	<div class="container">
		<ul class="nav nav-pills">
			<li><a href="/">Index</a></li>
			<li><a href="/second">Second</a></li>
			<li><a href="/add/1/2">Add 1 and 2</a></li>
			<li><a href="/api/hello/loki2302">JSON response</a></li>
			<li><a href="/angular">Hello Angular</a></li>
		</ul>
		<div class="page-header">
			<h2>${title} <small>is a title</small></h2>
		</div>
		<#nested>
		<hr/>
		&copy; <a href="http://loki2302.me" target="_blank">loki2302</a>
	</div>
</body>
</html>
</#macro>