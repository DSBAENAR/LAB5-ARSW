var App = (function(){
    let blueprints = []
    let currentAuthor = "";

    function printBlueprints(){
        const table = document.getElementById("blueprintTable")
        table.innerHTML = `
            <tr>
                <th>Blueprint Name</th>
                <th>Number of points</th>
            </tr>
        `;
        blueprints.forEach(bp => {
            let row = `<tr>
                        <td> ${bp.name}</td>
                        <td>${bp.points}</td>
                        </tr>`
            table.innerHTML += row
        })
    }

    return {
        setAuthor : function(author){
            currentAuthor = author

           apimock.getBlueprintsByAuthor(author, function(bps){
            if (!bps) {
                blueprints = [];
            } else {
                blueprints = bps.map(bp => ({
                    name: bp.name,
                    points: bp.points.length
                }));
            }
            printBlueprints();
    });
        },

        setBlueprints : function(blprnts){
            blueprints = blprnts
        },

        getAuthor: function(){
            return currentAuthor
        },

        getBlueprints: function(){
            return blueprints
        }
    }
})();


