angular.module("app", [], function() {    
});

angular.module("app").factory("testService", function() {
    return {
        "helloWorld": function() {
            return "hello world!";            
        }
    };
});

injector = angular.injector(["app", "ng"]);