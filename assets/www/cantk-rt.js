/*
 * File:   cantk-rt.js
 * Author: Li XianJing <xianjimli@hotmail.com>
 * Brief:  cantk runtime adapter
 * 
 * Copyright (c) 2015 - 2015 Tangram HD Inc.
 * 
 */
 
function CantkRT() {
}

CantkRT.isNative = function() {
	return !!CantkRT.rt;
}

CantkRT.init = function(onReady) {
	function onDeviceReady() {
		try {
			CantkRT.rt = cordova.require("com.tangide.cantk.CantkRuntime");	
		}catch(e) {
			console.log("com.tangide.cantk.CantkRuntime not found.");
		}

		if(CantkRT.rt) {
			CantkRT.rt.init();
			CantkRT.setShowFPS(true);
		}

		onReady();
	}

	if(window.cordova) {
		document.addEventListener('deviceready', onDeviceReady, false);
	}
	else {
		window.addEventListener('load', onReady, false);
	}

	return;
}

CantkRT.getViewPort = function() {
	var width;
	var height;

	if(CantkRT.rt) {
		return CantkRT.rt.getViewPort();
	}
	else {
		if (typeof window.innerWidth != 'undefined'){
			width = window.innerWidth;
			height = window.innerHeight;
		}
		else if (typeof document.documentElement != 'undefined'
		&& typeof document.documentElement.clientWidth !=
		'undefined' && document.documentElement.clientWidth != 0)
		{
			width = document.documentElement.clientWidth;
			height = document.documentElement.clientHeight;
		}
		else{
			width = document.getElementsByTagName('body')[0].clientWidth;
			height = document.getElementsByTagName('body')[0].clientHeight;
		}

	}
	
	return {width:width, height:height};
}

CantkRT.getMainCanvas = function() {
	var canvas = null;

	if(CantkRT.canvas) {
		return CantkRT.canvas;
	}

	if(CantkRT.rt) {
		canvas = CantkRT.rt.createCanvas();
		console.log("CantkRT.rt.createCanvas");
	}
	else {
		canvas = document.createElement("canvas");
		
		canvas.id = "main-canvas";
		canvas.width = 100;
		canvas.height = 100;
		canvas.style.zIndex = 0;
		document.body.appendChild(canvas);
	
		canvas.flush = function() {}
	}

	CantkRT.canvas = canvas;

	return canvas;
}

CantkRT.isResSupportCrossOrgin = function(src) {
	return src && (src.indexOf("upaiyun.com") > 0 || src.indexOf("bcs.duapp.com") > 0);
}

CantkRT.createImage = function(src, onLoad, onError) {
	var image = null;
	if(CantkRT.rt) {
		image = CantkRT.rt.createImage(src, onLoad, onError);
	}
	else {
		image = new Image();
		image.onload = function() {
			if(onLoad) {
				onLoad(image);
			}
		}
		
		image.onerror = function() {
			if(onError) {
				onError(image);
			}
		}
		
		if(CantkRT.isResSupportCrossOrgin(src)) {
			image.crossOrigin = "Anonymous";
		}

		image.src = src;
	}

	return image;
}

CantkRT.createImageFromCanvas = function(canvas, onLoad, onError) {
	if(!canvas) {
		if(onError) {
			onError();
		}

		return null;
	}

	if(CantkRT.rt) {
		return CantkRT.rt.createImage(canvas.toDataURL(), onLoad, onError);
	}
	else {
		if(onLoad) {
			onLoad(canvas);
		}
		return canvas;
	}
}

CantkRT.setShowFPS = function(value) {
	console.log("CantkRT.setShowFPS");
	if(CantkRT.rt) {
		return CantkRT.rt.setShowFPS(value);
	}
	else {
		return;
	}
}

CantkRT.createSoundEffect = function(url, onLoad, onError) {
	if(CantkRT.rt) {
		return CantkRT.rt.createSoundEffect(url, onLoad, onError);
	}

	return null;
}

CantkRT.createSoundMusic = function(url, onLoad, onError) {
	if(CantkRT.rt) {
		return CantkRT.rt.createSoundMusic(url, onLoad, onError);
	}

	return null;
}

CantkRT.createSingleLineTextEditor = function() {
	if(CantkRT.rt) {
		return CantkRT.rt.createSingleLineTextEditor();
	}

	return null;
}

CantkRT.createMultiLineTextEditor= function() {
	if(CantkRT.rt) {
		return CantkRT.rt.createMultiLineTextEditor();
	}

	return null;
}

window.requestAnimationFrame = window.requestAnimationFrame 
	|| window.mozRequestAnimationFrame 
	|| window.webkitRequestAnimationFrame 
	|| window.msRequestAnimationFrame || function(callback) {
		return setTimeout(callback, 16);
	}

CantkRT.requestAnimFrame = function(callback) {
	return window.requestAnimationFrame(callback);
}

