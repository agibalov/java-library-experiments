<#macro do title>
<html>
<head>
	<title>${title}</title>
	<link rel="stylesheet" href="/assets/bootstrap/css/bootstrap.min.css" />
<head>
<body>
	<div class="container">
		<ul class="nav nav-pills">
			<li><a href="/">Index</a></li>
			<li><a href="/second">Second</a></li>
			<li><a href="/add/1/2">Add 1 and 2</a></li>
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