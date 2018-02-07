var calculatorService = (function() {
    var addNumbers = function(a, b) {
        return a + b;
    };

    var subNumbers = function(a, b) {
        return a - b;
    };

    var mulNumbers = function(a, b) {
        return a * b;
    };

    var divNumbers = function(a, b) {
        return a / b;
    };

    return {
        "addNumbers": addNumbers,
        "subNumbers": subNumbers,
        "mulNumbers": mulNumbers,
        "divNumbers": divNumbers
    };
})();