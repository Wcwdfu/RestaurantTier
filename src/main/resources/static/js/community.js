document.addEventListener("DOMContentLoaded", function() {
    var searchIcon = document.getElementById("postSearch");
    var searchModal = document.getElementById("search-modal");
    var overlay = document.getElementById("overlay");

    searchIcon.addEventListener("click", function(event) {
        event.preventDefault();
        searchModal.style.display = "block";
        overlay.style.display = "block";
    });

    overlay.addEventListener("click", function() {
        searchModal.style.display = "none";
        overlay.style.display = "none";
    });
});
