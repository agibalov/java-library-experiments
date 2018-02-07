<#import "layout.ftl" as layout>
<@layout.do title="I am awesome">
	<div ng-controller="AddNumbersController">
		<form ng-submit="run()" class="form-horizontal">
			<legend>Add numbers</legend>
			<div class="control-group">
				<label class="control-label">A</label>
				<div class="controls">				
					<input type="text" ng-model="a">
				</div>
			</div>				
			<div class="control-group">
				<label class="control-label">B</label>
				<div class="controls">				
					<input type="text" ng-model="b">
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button type="submit" class="btn btn-large">Compute</button>
				</div>
			</div>			
		</form>
<h4>GET response:</h4>
<pre>{{getResult|json}}</pre>
<h4>POST response:</h4>
<pre>{{postResult|json}}</pre>
	</div>
</@layout.do>