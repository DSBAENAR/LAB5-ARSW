var apiclient = (function(){
    const url = "http://localhost:8080/api/blueprints";

    return {
        getBlueprintsByAuthor: function(author, callback){
            $.get(url + "/" + author, function(data){
                callback(data);
                console.log(data)
            }).fail(function() {
                alert("Error retrieving blueprints for author: " + author);
            });
        },

        getBlueprintsByNameAndAuthor: function(author, name, callback){
            $.get(url + "/" + author + "/" + name, function(data){
                callback(data);
            }).fail(function() {
                alert("Error retrieving blueprint: " + name + " for author: " + author);
            });
        }
    }
})();
