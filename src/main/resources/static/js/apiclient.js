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

        /* The `putBlueprint` function in the `apiclient` module is making an AJAX PUT request to the
        specified URL endpoint with the author and name parameters appended to it. The request
        includes the `blueprint` data in JSON format. When the request is successful, it invokes the
        callback function provided with the response data as an argument. If the request fails, it
        invokes the callback function with an error message indicating the failure. */
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


        /* The `getAllBlueprints` function in the `apiclient` module is making an AJAX GET request to the
        specified URL endpoint. When the request is successful, it invokes the callback function
        provided with the retrieved data as an argument. If the request fails, it invokes the
        callback function with an error message indicating the failure. */
        getAllBlueprints: function(callback){
            $.get(url, function(data){
                if(callback) callback(null, data);
            }).fail(function(jqxhr, status, err){
                if(callback) callback(err || status || 'error');
            });
        }
        ,

        /*
        The method `postBlueprint` in the `apiclient` module is making an AJAX POST request to the specified URL
        endpoint with the provided `blueprint` data. The data is sent in JSON format. When the request is
        successful, it invokes the callback function provided with the response data as an argument. If the
        request fails, it invokes the callback function with an error message indicating the failure.
        */
        postBlueprint: function(blueprint, callback){
            $.ajax({
                url: url + "/blueprint",
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(blueprint)
            }).done(function(resp){
                if(callback) callback(null, resp);
            }).fail(function(jqxhr, status, err){
                if(callback) callback(err || status || 'error');
            });
        }
        ,

        /*
         * Delete a blueprint by author and name. Returns a Promise.
         */
        deleteBlueprint: function(author, name){
            return new Promise(function(resolve, reject){
                $.ajax({
                    url: url + "/blueprint" + "/" + name,
                    type: 'DELETE'
                }).done(function(resp){
                    resolve(resp);
                }).fail(function(jqxhr, status, err){
                    const body = jqxhr && jqxhr.responseText ? jqxhr.responseText : (err || status || 'error');
                    reject(body);
                });
            });
        },

        /*
         * Promise-based wrapper around getAllBlueprints
         */
        getAllBlueprintsPromise: function(){
            return new Promise(function(resolve, reject){
                $.get(url, function(data){
                    resolve(data);
                }).fail(function(jqxhr, status, err){
                    const body = jqxhr && jqxhr.responseText ? jqxhr.responseText : (err || status || 'error');
                    reject(body);
                });
            });
        }
    }
})();
