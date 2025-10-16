var apiclient = (function(){
    const url = "https://blueprints-e2f7gcd4c0aqcsg2.canadacentral-01.azurewebsites.net/api/blueprints";

    return {
        /* The `getBlueprintsByAuthor` function in the `apiclient` module is making an AJAX GET request
        to the specified URL endpoint with the author parameter appended to it. When the request is
        successful, it invokes the callback function provided with the retrieved data as an
        argument. Additionally, it logs the data to the console. If the request fails, it displays
        an alert message indicating an error in retrieving blueprints for the specified author. */
        getBlueprintsByAuthor: function(author, callback){
            $.get(url + "/" + author, function(data){
                callback(data);
                console.log(data)
            }).fail(function() {
                alert("Error retrieving blueprints for author: " + author);
            });
        },

        /* The `getBlueprintsByNameAndAuthor` function in the `apiclient` module is making an AJAX GET
        request to the specified URL endpoint with both the author and name parameters appended to
        it. When the request is successful, it invokes the callback function provided with the
        retrieved data as an argument. If the request fails, it displays an alert message indicating
        an error in retrieving the blueprint with the specified name for the specified author. */
        getBlueprintsByNameAndAuthor: function(author, name, callback){
            $.get(url + "/" + author + "/" + name, function(data){
                callback(data);
            }).fail(function() {
                alert("Error retrieving blueprint: " + name + " for author: " + author);
            });
        }
        ,

        putBlueprint: function(author, name, blueprint, callback){
            $.ajax({
                url: url + "/" + author + "/" + name,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(blueprint)
            }).done(function(resp){
                if(callback) callback(null, resp);
            }).fail(function(jqxhr, status, err){
                if(callback) callback(err || status || 'error');
            });
        },

        getAllBlueprints: function(callback){
            $.get(url, function(data){
                if(callback) callback(null, data);
            }).fail(function(jqxhr, status, err){
                if(callback) callback(err || status || 'error');
            });
        }
    }
})();
