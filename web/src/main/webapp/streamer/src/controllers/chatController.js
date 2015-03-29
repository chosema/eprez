module.exports = [ '$scope', '$q', 'webSocketService', function($scope, $q, webSocketService) {

	$scope.messages = [];
	
	$scope.newMessagesCount = 0;

	webSocketService.on('push:presentation.chat.message', function(message) {
		//console.log('Received new message:');
		//console.log(message);
		
		var userToken = $scope._presentation.session.tokens.filter(function(token) {
			return token.id == message.token;
		});
		
		if (userToken && userToken.length > 0) {
			var newMessage = {
	            user : userToken[0].user,
	            date : new Date(),
	            currentUser : $scope._token == userToken[0].id,
	            text : message.message
	        };
			$scope.messages.push(newMessage);
			if (!newMessage.currentUser) {
				$scope.newMessagesCount++;
			}
			$scope.$apply();
			
			scrollToBottom();
		} else {
			console.log('Received message has unknown user token');
		}
	});

	$scope.onToggleChat = function() {
		$scope.newMessagesCount = 0;
		jQuery('#collapseChatBoxBtn').click();
		$scope.onResetNewMessages();
		$scope.onFocusChat();
	};

	$scope.onFocusChat = function() {
		if (jQuery('#chatBox').hasClass('collapsed-box')) {
			jQuery('#chatMessageInput').focus();
		}
		scrollToBottom();
	};

	$scope.onSend = function() {
		webSocketService.send("send:presentation.chat.message", $scope.message);
		$scope.message = null;
	};
	
	$scope.onResetNewMessages = function() {
		$scope.newMessagesCount = 0;
	};

	$scope.onSubmitMessage = function(event) {
		if (event.keyCode == 13) { // if enter key
			$scope.onSend();
		}
	}

	jQuery('#chatMessageInput').on('focusin', function(e) {
		jQuery('#chatBox').addClass('focused');
	});
	jQuery('#chatMessageInput').on('focusout', function(e) {
		jQuery('#chatBox').removeClass('focused');
	});
	
	function scrollToBottom() {
		jQuery("#chatBox .direct-chat-messages").scrollTop(jQuery("#chatBox .direct-chat-messages")[0].scrollHeight);
	}
}];
