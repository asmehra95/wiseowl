//ready the dom.

/*$(document).ready(function(){
  
  //when the search box is entered
/*function searchit(e) {
      //slideDown the results div
e.preventDefault();
	alert("help me");
    $(".result").slideDown(200);
    //slideDown the loading gif
    //$(".gif").slideDown(200);
    //animate the form to the top
    $(".form").animate({
      top:"-200px",
    });
    //fadeOut out the yioop menu
    //$(".yioop").fadeOut(500);
    //fadeIn the seearch results
    $(".res li").fadeIn(1000);
  }*/

/*.blur(function(){
    //slideUp the results div
    $(".result").slideUp(200);
    //slideUp the loading gif
    //$(".gif").slideUp(200);
    //animate the form back to its original position
    $(".form").animate({
      top:"0px",
    });
    //fadeIn the yioop menu
   // $(".yioop").fadeIn(1000);
    //fadeOut the search results
    $(".res li").fadeOut(500);
  });
  
});
$('a[href^="#"]').on('click', function(event) {

    var target = $(this.getAttribute('href'));

    if( target.length ) {
        event.preventDefault();
        $('html, body').stop().animate({
            scrollTop: target.offset().top
        }, 10000);
    }

});*/
$('a[href*="#"]:not([href="#"])').click(function() {
  if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
    var target = $(this.hash);
    target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
    if (target.length) {
      $('html, body').animate({
        scrollTop: target.offset().top
      }, 10000);
      return false;
    }
  }
});
