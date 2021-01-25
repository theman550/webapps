function toggleNav() {
    
    $("#navbarNav").toggleClass("collapse");
}

function addEntry() {
    var drinkName = $("#nameField").val();
    $("#drinkList").append("<div class=\"col-auto mt-3\"><div class=\"card drink-entry\"><img src=\"https://storhushall.skanemejerier.se/content/uploads/2019/05/Summer_Sunrise.jpg\" class=\"card-img-top\" alt=\"...\"><div class=\"card-body\"><h5 class=\"card-title\">"+drinkName+"</h5><p class=\"card-text\">This is a longer card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.</p></div></div></div>");
}