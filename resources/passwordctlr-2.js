'use strict';
/**
 * 
 */

(function() {

// var appCommand = angular.module('bonitacommands', ['ui.bootstrap']);  'ngRoute',
alert('Module');

var appPassword = angular.module('passwordModule', ['ngRoute']);
 // , ['ui.bootstrap']);


// appCommand.config();

// Constant used to specify resource base path (facilitates integration into a Bonita custom page)
appPassword.constant('RESOURCE_PATH', 'pageResource?page=custompage_changepassword&location=');

appPassword.controller('ChangePasswordController',

	function () {
	     alert('init controler');
		this.changePassword = function (password1, password2)
		{
			alert('password1='+password1);
		}
	
	});	
	
})();