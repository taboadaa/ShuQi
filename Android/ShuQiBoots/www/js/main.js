
var map;
var marker;
var watchID;

$(document).ready(function() {
    document.addEventListener("deviceready", onDeviceReady, false);
    //uncomment for testing in Chrome browser
//    onDeviceReady();
});

function onDeviceReady() {
    $(window).unbind();
    $(window).bind('pageshow resize orientationchange', function(e) {
        max_height();
    });
    max_height();
    google.load("maps", "3.8", {"callback": map, other_params: "sensor=true&language=en"});
}

// Method to open the About dialog
function showAbout() {
    showAlert("Google Maps", "Created with NetBeans 7.4");
}
;

function showAlert(message, title) {
    if (window.navigator.notification) {
        window.navigator.notification.alert(message, null, title, 'OK');
    } else {
        alert(title ? (title + ": " + message) : message);
    }
}
