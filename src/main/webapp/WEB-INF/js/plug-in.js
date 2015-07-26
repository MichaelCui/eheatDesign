(function($){
	$.getUrlParam = function(name)
	{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null;
	}
	$.fn.nextDes = function(element){
		var $v=$(this.nextAll(element)[0]);
		return $v;
	}
	$.fn.parentDes = function(element){
		var $v=$(this.parents(element)[0]);
		return $v;
	}

})(jQuery);