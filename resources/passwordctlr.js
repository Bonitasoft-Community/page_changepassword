'use strict';
/**
 * 
 */

(function() {

// var appCommand = angular.module('bonitacommands', ['ui.bootstrap']);


var appCommand = angular.module('changepassword', ['ui.bootstrap', 'ngCookies']);



// appCommand.config();

// Constant used to specify resource base path (facilitates integration into a Bonita custom page)
appCommand.constant('RESOURCE_PATH', 'pageResource?page=custompage_cmdmanage&location=');

appCommand.controller('ChangePasswordController', ['$cookies', 
	function ($cookies) { 
     
		this.showError = 0;
		this.showErrorBadPassword = 0;
		this.showMessage=0;
		this.message='Type your password twice';
		this.pass = {
             pass1: '',
             pass2: ''
        };


		this.setMessage = function (message, error)
		{
			this.showMessage = 0;
			this.showError = 0;
			this.showErrorBadPassword=0;
			if (error == 1)
			{
				this.showError = 1;
			}
			else 
			{
				this.showMessage=1;
			}
			this.message=message;
		};
	   this.changePassword = function ()
		{
			this.showError=0;
			this.showErrorBadPassword=0;
			this.showMessage=0;
			this.message= "";
			if (this.pass.pass1 != this.pass.pass2)
			{			
				this.showErrorBadPassword=1;
			}
			else
			{			

				this.setMessage("Change in progress...",0);
				var me = this;
				var passwordEncoded=encodeURIComponent(this.pass.pass1);
				
				var csrfToken = $cookies['X-Bonita-API-Token'];
				var additionalHeaders = {};
				if (csrfToken) {
					additionalHeaders ['X-Bonita-API-Token'] = csrfToken;
				}
				$.ajax({
					method : 'GET',
					headers: additionalHeaders,
					url : '?page=custompage_changepassword&action=changepassword&password='+ passwordEncoded,
					data : this.pass1,
					contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
					success : function ( result ) {
								me.setMessage(result,0);
								},
					error: function ( result ) {
								me.setMessage('Error, password is not set',1);
								}
					});




			}
				
		};
	

	}]);

	   
	
})();
